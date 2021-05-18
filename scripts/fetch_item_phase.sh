#!/usr/bin/env bash

_parse_phase_html() {
  data=$(cat -)
  expr=".+Phase ([0-9]+).+"
  if [[ $data =~ $expr ]]; then
    local phaseNum=$(echo "${BASH_REMATCH[1]}" | awk '{print tolower($0)}')
    echo "\"$1\": $phaseNum,"
  else
    # On failure, emit phase 0 aka unknown
    echo "\"$1\": 0,"
  fi
}

_fetch_phase() {
  curl -s "https://tbc.wowhead.com/item=$1&xml" | _parse_phase_html $1
}

export -f _fetch_phase
export -f _parse_phase_html

# Fetch every ID from the items.json file, then fetch its image
results='{'
results+=$(jq -r '.[] | "\(.entry)"' 'src/jvmMain/resources/items.json' | xargs -I{}  bash -c "_fetch_phase {}")
#echo 28437 | xargs -I{}  bash -c "_fetch_phase {}"

# Strip the last comma and terminate the array
results=$(echo $results | sed 's/,$//')
results+='}'
echo $results > 'src/jvmMain/resources/item_phases.json'
