bash
#!/bin/bash

echo "Scanner"
echo $0

function scanDir {

  for directory in ls -F 
  do
    text="|-${directory}"
    echo text
    scanDirFiles directory $2
  done


}

function scanDirFiles{
  for file in find $1 -type f
    do
      text="|-${file}"
      echo text
    done
}
