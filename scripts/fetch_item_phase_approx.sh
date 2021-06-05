#!/usr/bin/env bash

_parse_phase_html() {
  data=$(cat -)

  name_expr=".+<name><\!\[CDATA\[(.+)\]\]><\/name>.+"
  quality_expr=".+<quality id=\"([0-9])\">.+"
  ilvl_expr=".+<level>([0-9]+)</level>.+"
  nv_expr=".+Nether Vortex.+"
  is_set_expr=".+item-set=([0-9]+).+"

  name=''
  quality=-1
  ilvl=-1
  is_set=-1

  # Otherwise, take a best guess
  if [[ $data =~ $name_expr ]]; then
    name="${BASH_REMATCH[1]}"
  fi

  if [[ $data =~ $quality_expr ]]; then
    quality="${BASH_REMATCH[1]}"
  fi

  if [[ $data =~ $ilvl_expr ]]; then
    ilvl="${BASH_REMATCH[1]}"
  fi

  if [[ $data =~ $is_set_expr ]]; then
    is_set="${BASH_REMATCH[1]}"
  fi

  if [ "$quality" == -1 ] && [ "$ilvl" == -1 ]; then
    echo "Failed ID $1" >&2
  else
    # If it needs a nether vortex, it's at least phase 2
    if [[ $data =~ $nv_expr ]]; then
      echo "Needs NV - id: $1, name: $name, quality: $quality, ilvl: $ilvl" >&2
      echo 2
      return
    fi

    # If the quality is less than 4 (not epic), it probably doesn't matter, stick it in p1
    if [[ $quality -lt 4 ]]; then
      echo 1
      return
    fi

    # KT fight legendaries
    if [[ $ilvl == 175 ]]; then
      echo 2
      return
    fi

    # Honor PvP gear
    if [[ $name =~ ^Warlords\'s.+ ]] || [[ $name =~ ^General\'s.+ ]] || [[ $name =~ ^Marshal\'s.+ ]]; then
      # TODO: Is this right?
      echo "S2 honor - id: $1, name: $name, quality: $quality, ilvl: $ilvl" >&2
      echo 2
      return
    fi

    if [[ $name =~ ^Vindicator\'s.+ ]]; then
      echo "S3 honor - id: $1, name: $name, quality: $quality, ilvl: $ilvl" >&2
      echo 3
      return
    fi

    if [[ $name =~ ^Guardian\'s.+ ]]; then
      echo "S4 honor - id: $1, name: $name, quality: $quality, ilvl: $ilvl" >&2
      echo 5
      return
    fi

    # Sets are probably tier sets
    if [[ $is_set != -1 ]]; then
      # T4 is ilvl 120, T5 is 133, T6 is 146
      if [[ $ilvl == 120 ]]; then
        echo "T4 set - id: $1, name: $name, quality: $quality, ilvl: $ilvl" >&2
        echo 1
        return
      elif [[ $ilvl == 133 ]]; then
        echo "T5 set - id: $1, name: $name, quality: $quality, ilvl: $ilvl" >&2
        echo 2
        return
      elif [[ $ilvl == 146 ]]; then
        echo "T6 set - id: $1, name: $name, quality: $quality, ilvl: $ilvl" >&2
        echo 3
        return
      fi
    fi

    # Guess by itemlevel
    if [[ $ilvl -le 141 ]]; then
      echo 2
      return
    elif [[ $ilvl -le 151 ]]; then
      echo 3
      return
    elif [[ $ilvl -le 164 ]]; then
      echo 5
      return
    else
      echo "Failed to guess - id: $1, name: $name, quality: $quality, ilvl: $ilvl" >&2
      return
    fi
  fi
}

_fetch_phase() {
  phase=$(curl -s "https://tbc.wowhead.com/item=$1&xml" | _parse_phase_html $1)
  echo "\"$1\": $phase,"
}

export -f _fetch_phase
export -f _parse_phase_html

results='{'
results+=$(jq -r '.[]' 'src/jvmMain/resources/item_phases_unknown.json' | xargs -I{}  bash -c "_fetch_phase {}")
#echo 28437 | xargs -I{}  bash -c "_fetch_phase {}"

# Strip the last comma and terminate the array
results=$(echo $results | sed 's/,$//')
results+='}'
echo $results > 'src/jvmMain/resources/item_phases_approx.json'
