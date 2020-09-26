bash
#!/bin/bash

function getOffset {
  local offsetText=$(printf "|--"'%.0s' $(seq $1))
  local space=" "

  local result=${offsetText//-/"  "}
  echo $result
}

function scanDirFiles {
  files=$(find $1 -type f -maxdepth 1 | sort)
  for file in $files
    do
      local fileName=$(echo $file | rev | cut -d'/' -f 1 | rev)
      local dirSpacing=$(getOffset $2)
      text="${dirSpacing}-${fileName}"
      echo $text
    done
}

function scanDir {
  local internalDirs=()
  local offset=$(( $2 + 1 ))

  while IFS= read -r -d $'\0'
  do
    internalDirs+=("$REPLY")
  done < <(find $1 -type d -maxdepth 1 -print0| sort )

  local size=${#internalDirs[@]}

  if [[ size -gt 0 ]]
  then
    internalDirs=$(find $1 -type d -maxdepth 1| sort| tail -$size)
    for directory in $internalDirs
    do
      if [[ $directory == $1 ]]
      then
        continue
      fi
      local dirName=$(echo $directory | rev | cut -d'/' -f 1 | rev)
      local dirSpacing=$(getOffset $2)
      echo $dirSpacing-$dirName
      scanDir $directory $offset
    done
  fi
    scanDirFiles $1 $offset
}


dirs=$(find $1 -type d -maxdepth 0| sort)

if [[ -z $dirs ]]
then
  echo "$1: Directory not found"
else
  scanDir $dirs 0
fi
