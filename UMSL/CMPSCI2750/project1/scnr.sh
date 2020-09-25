bash
#!/bin/bash

function scanDirFiles {
  for file in find $1 -type f
    do
      text="|-${file}"
      echo $text
    done
}

function scanDir {
  echo "search dir: ${1}"
  internalDirs=$(find $1 -type d)

  for directory in $internalDirs
  do
    text="|-${directory}"
    echo $text
    scanDir directory
  done
}


echo "Scanner"
echo $1
dirs=$(find $1 -type d | sort)
echo $dirs

if [[ ${dirs[@]} -eq 0 ]]
then
  echo "Directory not found"
else
  scanDir $dirs[0]
fi
