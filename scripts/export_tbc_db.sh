#/bin/bash

# Dumps items needed for sim from the mangos TBC DB: https://github.com/cmangos/tbc-db
# This database should be running locally, with the default user of 'mangos', and password 'mangos'
# MySQL Shell and jq should be installed and on your PATH

# Get all reasonable items
items_columns="entry,class,subclass,name,displayid,Quality,AllowableClass,AllowableRace,ItemLevel,"
items_columns+="stat_type1,stat_value1,stat_type2,stat_value2,stat_type3,stat_value3,stat_type4,stat_value4,stat_type5,stat_value5,dmg_min1,dmg_max1,dmg_type1,dmg_min2,dmg_max2,dmg_type2,dmg_min3,dmg_max3,dmg_type3,dmg_min4,dmg_max4,dmg_type5,dmg_min5,dmg_max5,dmg_type5,"
items_columns+="armor,holy_res,fire_res,nature_res,frost_res,shadow_res,arcane_res,delay,ammo_type,"
items_columns+="spellid_1,spelltrigger_1,spellcharges_1,spellppmrate_1,spellcooldown_1,spellcategorycooldown_1,"
items_columns+="spellid_2,spelltrigger_2,spellcharges_2,spellppmrate_2,spellcooldown_2,spellcategorycooldown_2,"
items_columns+="spellid_3,spelltrigger_3,spellcharges_3,spellppmrate_3,spellcooldown_3,spellcategorycooldown_3,"
items_columns+="spellid_4,spelltrigger_4,spellcharges_4,spellppmrate_4,spellcooldown_4,spellcategorycooldown_4,"
items_columns+="spellid_5,spelltrigger_5,spellcharges_5,spellppmrate_5,spellcooldown_5,spellcategorycooldown_5,"
items_columns+="description,itemset,block,"
items_columns+="socketColor_1,socketColor_2,socketColor_3,socketBonus,"
items_columns+="ArmorDamageModifier"

# Only select rare, epic, and legendary items with a reasonable itemlevel
items_where="Quality in (3, 4, 5) and ItemLevel >= 115"

# TODO: Cherry-pick some vanilla items for big Naxx pumpers

items_output_path=src/main/resources/items.json
command="use tbcmangos; select $items_columns from item_template where $items_where;"
echo "$command" | mysqlsh --sql --result-format=json/array --host='localhost' --port=3306 --user='mangos' --password='mangos' --log-level='none' > "$items_output_path"

# Get any spells associated with the selected items
spell_ids=$(cat $items_output_path | jq -r '.[] | "\(.spellid_1)\n\(.spellid_2)\n\(.spellid_3)\n\(.spellid_4)\n\(.spellid_5)"' | sort | uniq | tr '\n' ',' | sed -e 's/,$//')

spells_output_path=src/main/resources/itemprocs.json
command="use tbcmangos; select * from spell_template where Id in ($spell_ids);"
echo "$command" | mysqlsh --sql --result-format=json/array --host='localhost' --port=3306 --user='mangos' --password='mangos' --log-level='none' > "$spells_output_path"
