#!/bin/bash
workingDirectory=$PWD

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
    printf "\033c"
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

mainMenu
