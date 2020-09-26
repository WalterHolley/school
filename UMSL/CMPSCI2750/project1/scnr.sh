bash
#!/bin/bash

function scanDirFiles {
  echo "find Files in: $1"
  files=$(find $1 -type f -maxdepth 1 | sort)
  for file in $files
    do
      text="|-${file}"
      echo $text
    done
}

function scanDir {
  echo "search dir: $1"
  local internalDirs=()
  local offset=$(( $2 + 1 ))

  while IFS= read -r -d $'\0'
  do
    internalDirs+=("$REPLY")
  done < <(find $1 -type d -maxdepth 1 -print0| sort )
  local size=${#internalDirs[@]}
  echo "${internalDirs}"
  echo $size

  if [[ size -gt 0 ]]
  then
    internalDirs=$(find $1 -type d -maxdepth 1| sort| tail -$size)
    echo "dir list: $internalDirs"
    for directory in $internalDirs
    do
      if [[ $directory == $1 ]]
      then
        continue
      fi
      text=$(echo $directory | tr [$1] [""])
      echo $text
      scanDir $directory $offset
    done
  fi
    scanDirFiles $1 $offset
}


echo "Scanner"
echo $1
dirs=$(find $1 -type d -maxdepth 0| sort)

if [[ -z $dirs ]]
then
  echo "Directory not found"
else
  scanDir $dirs 0
fi
