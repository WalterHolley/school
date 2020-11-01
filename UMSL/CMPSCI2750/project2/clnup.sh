#!/bin/bash
workingDirectory=$PWD
clearConsole=$(printf "\033c")

function mainMenu {
  echo "Working Directory: $workingDirectory"
  echo "Make a selection"
  echo "1>>Clear empty files and directories"
  echo "2>>Move Files"
  echo "3>>Compress files"
  echo "4>>Change Directory"
  echo "5>>Exit"

  read selection
  echo $clearConsole
  case $selection in
    [1]*)
    #clear Files
    cleanDir
    ;;
    [2]*)
    #move Files
    ;;
    [3]*)
    #compress Files
    ;;
    [4]*)
    #change working directory
    changeDir
    ;;
    [5]*)
    echo "Exiting tool."
    exit 0
    ;;
  esac
}

#TODO create move files function

function changeDir {
  echo "Enter Directory:"
  read selection
  dirs=$( find $selection -maxdepth 0 -type d | sort )

  if [[ -z $dirs ]]
  then
    echo "$selection: Directory not found"
  else
    workingDirectory=$dirs
    cd $workingDirectory
    echo $clearConsole
    echo "Working directory updated"
    dirs=$(getDirList $workingDirectory)
    files=$(getFiles $workingDirectory)

    if [[ ${#dirs[@]} -gt 0 ]]
    then
        echo "===Directories==="
        for dir in $dirs
        do
          echo $dir | sed 's_'$workingDirectory'__g'
        done
    fi

    if [[ ${#files[@]} -gt 0 ]]
    then
        echo "======Files======"
        for file in $files
        do
          echo $file | sed 's_'$workingDirectory'__g'
        done
    fi
  fi
  mainMenu
}

function getDirList {
  local internalDirs=()

  while IFS= read -r -d $'\0'
  do
    files+=("REPLY")
  done < <(find $1 -maxdepth 1 -mindepth 1 -type d -print0| sort )

  internalDirs=$(find $1 -maxdepth 1 -mindepth 1 -type d | sort )
  echo $internalDirs
}

function getFiles {

  local files=()

  while IFS= read -r -d $'\0'
  do
    files+=("REPLY")
  done < <(find $1 -maxdepth 1 -type f -print0| sort )
  files=$(find $1 -maxdepth 1 -type f | sort )
  echo $files

}

#remove empty files and directories
function cleanDir {
  #remove empty files from working directory
  find $workingDirectory -type f -size 0 -exec rm {} ';'

  #remove empty directories forom working directory
  find $workingDirectory -type d -size 0 -exec rm {} ';'

}

#TODO: Complete compression menu
function compressionMenu {
  echo "Select compression type"
  echo "1>>gzip(tar.gz)"
  echo "2>>bzip2(tar.bz2)"
  echo "3>>xz(tar.xz)"

  read selection

  case $selection in
    [1]*)
    ;;
    [2]*)
    ;;
    [3]*)
    ;;
  esac
}

#TODO fix function syntax
function compressFiles {
  echo $clearConsole
  echo "Working Directory: $workingDirectory"

  echo "Enter files(ex: file.txt /dir1/file2.txt):"

  #get file list
  read fileList

  #verify files
  for file in $fileList
  do
  filepath="${workingDirectory}/${file}"
  found=$(find $filepath -type f | wc -l)

  #leave on failed verification
  if [[ $found -eq 0 ]]
  then
    echo $clearConsole
    echo "file not found: $filepath"
    mainMenu
  done

  #ask compression preferences
  tar czf dirarchive.tar.gz "${1[@]}"

  #do compression
  #move to working directory
}

echo $clearConsole
mainMenu
