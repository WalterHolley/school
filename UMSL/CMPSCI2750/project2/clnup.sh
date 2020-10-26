#!/bin/bash
workingDirectory=$PWD
clearConsole=$(printf "\033c")

function mainMenu {
  printf "\033c"
  echo "***********"
  echo "* Cleanup *"
  echo "***********"
  echo "Working Directory: $workingDirectory"
  echo "1>>Clear empty files and directories"
  echo "2>>Move Files"
  echo "3>>Compress files"
  echo "4>>Change Directory"
  echo "5>>Exit"

  read selection

  case $selection in
    [1]*)
    #clear Files
    cleanDir $workingDirectory
    ;;
    [2]*)
    #move Files
    ;;
    [3]*)
    #compress Files
    ;;
    [4]*)
    changeDir
    ;;
    [5]*)
    echo $clearConsole
    echo "Exiting tool."
    exit 0
    ;;
  esac
}

function changeDir {
  echo "Enter Directory:"
  read selection
  dirs=$( find $selection -maxdepth 0 -type d | sort )

  if [[ -z $dirs ]]
  then
    mainMenu
  else
    workingDirectory=$dirs
    cd $workingDirectory
    echo "Working directory updated"
  fi
  mainMenu
}

function getDirList {
  local internalDirs=()
  while IFS= read -r -d $'\0'
  do
    internalDirs+=("$REPLY")
  done < <(find $1 -maxdepth 1 -type d -print0| sort )

  echo $internalDirs
}

#remove empty directories
function cleanDir {
  local internalDirs=()
  internalDirs=$(getDirList $1)
  local size=${#internalDirs[@]}
  local result=0

  if [[ size -gt 0 ]]
  then
    for directory in $internalDirs
    do
      cleanDir $directory
      nofiles=$(cleanFiles $directory)

    if [[ $nofiles == 1 ]]
      then
        rm -d $directory
    fi
    done
  fi

  nofiles=$(cleanFiles $1)

  #search again for directories
  internalDirs=$(getDirList $1)
  #return 1 if no directories or files in directory
  if [ $internalDirs -eq $nofiles ] && [ $nofiles -eq 1 ]; then
    result=1
  fi
  echo $result

}

#remove empty files from directory
function cleanFiles {
  result=0
  echo $result
}

function compressFiles {
  tar czf dirarchive.tar.gz "${1[@]}"
}

mainMenu
