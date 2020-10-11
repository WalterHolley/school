#!/bin/bash
onintr int
YELLOW_COLOR='\033[1;33m'
GREEN_COLOR='\033[0;32m'
NO_COLOR='\033[0m'

function scanDirFiles {
  files=$(find $1 -maxdepth 1 -type f | sort)
  for file in $files
    do
      local dirSpacing=$(echo $file |sed -e "s/[^\/]*\//--/g")
      printf "${GREEN_COLOR}${dirSpacing}${NO_COLOR}\n"
    done
}

function scanDir {
  local internalDirs=()
  local offset=$(( $2 + 1 ))

  while IFS= read -r -d $'\0'
  do
    internalDirs+=("$REPLY")
  done < <(find $1 -maxdepth 1 -type d -print0| sort )

  local size=${#internalDirs[@]}

  if [[ size -gt 0 ]]
  then
    internalDirs=$(find $1 -maxdepth 1 -type d | sort| tail -$size)

    for directory in $internalDirs
    do
      if [[ $directory == $1 ]]
      then
        scanDirFiles $1 $offset
        continue
      fi

      local dirSpacing=$(echo $directory |sed -e "s/[^\/]*\//--/g")

      printf "${YELLOW_COLOR}$dirSpacing${NO_COLOR}\n"

      scanDir $directory $offset
    done
  fi
}


dirs=$(find $1 -maxdepth 0 -type d | sort)

if [[ -z $dirs ]]
then
  echo "$1: Directory not found"
  exit 1
else
  echo $dirs
  scanDir $dirs 0
  exit 0
fi
int:
  echo "Program halted.  Exiting"
    exit 1
