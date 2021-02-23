#!/usr/bin/env bash

_parse_image_html() {
  data=$(cat -)
  expr=".+icon$1-generic.+appendChild.+create\(.([a-zA-Z0-9_]+).+"
  if [[ $data =~ $expr ]]; then
    local filename=$(echo "${BASH_REMATCH[1]}" | awk '{print tolower($0) ".jpg"}')
    echo "\"$1\": \"$filename\","
  else
    echo "Failed ID $1" >&2
  fi
}

_fetch_image() {
  curl -s "https://tbcdb.com/?item=$1" | _parse_image_html $1
}

export -f _fetch_image
export -f _parse_image_html

# Fetch every ID from the items.json file, then fetch its image
results='{'
results+=$(jq -r '.[] | "\(.entry)"' 'src/jvmMain/resources/items.json' | xargs -I{}  bash -c "_fetch_image {}")
# echo 28437 | xargs -I{}  bash -c "_fetch_image {}"

# Strip the last comma and terminate the array
results=$(echo $results | sed 's/,$//')
results+='}'
echo $results > 'src/jvmMain/resources/item_icons.json'
