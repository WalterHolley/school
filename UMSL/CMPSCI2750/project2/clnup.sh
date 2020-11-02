#!/bin/bash
workingDirectory=$PWD
clearConsole=$(printf "\033c")
fileList=""

trap exitOnSigint SIGINT

#exits the program when SIGINT is detected
function exitOnSigint {
  echo " SIGINT detected.  Goodbye!"
  exit 0
}

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
    moveFiles
    ;;
    [3]*)
    #compress Files
    getFilesToCompress
    ;;
    [4]*)
    #change working directory
    changeDir
    ;;
    [5]*)
    echo "Exiting tool."
    exit 0
    ;;
    *)
    echo "Invalid selection"
    ;;
  esac
  mainMenu
}

#moves files within working directory
function moveFiles {

  found=""
  echo "Working Directory: $workingDirectory"

  echo "Enter files[ex: file.txt /dir1/file2.txt]:"

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
      fileList="-1"
      break
    fi
  done

  if [[ $fileList != "-1" ]]
  then
    echo "working directory: $workingDirectory"
    echo "Enter directory where you wish to move the files(example: dir  example: dir1/dir2)"
    read selection
    dir="${workingDirectory}/${selection}"

    exists=$(find $dir -type d | wc -l)

    if [[ $exists -eq 0 ]]
    then
      echo $dir
      echo "Does not exist.  create? y/n"
      read yesno

      if [[ $yesno -ne "y" ]]
      then
        echo $clearConsole
        echo "File move aborted"
        break
      else
        mkdir $dir
      fi
    fi
    mv $fileList $dir
  fi
}

#gets a list of files from the user, and verifies they exist
function getFilesFromUser {
  echo $clearConsole
  found=0
  echo "Working Directory: $workingDirectory"

  echo "Enter files[ex: file.txt /dir1/file2.txt]:"

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
      fileList=-1
      break
    fi
  done

  echo $fileList
}

#changes the working directory
function changeDir {
  echo "Working Directory: $workingDirectory"
  dirs=$(getDirList $workingDirectory)

  echo "Enter Directory:"
  read selection
  dirs=$( find $selection -maxdepth 0 -type d | sort )

  echo $clearConsole

  if [[ -z $dirs ]]
  then
    echo "$selection: Directory not found"
  else
    workingDirectory=$dirs
    cd $workingDirectory

    echo "Working directory updated"
    showWorkingDirectory
  fi
  mainMenu
}

#gets a list of directories in the first level of the working directory
function getDirList {
  local internalDirs=()

  while IFS= read -r -d $'\0'
  do
    files+=("REPLY")
  done < <(find $1 -maxdepth 1 -type d | sort )

  internalDirs=$(find $1 -maxdepth 1 -mindepth 1 -type d | sort )
  echo $internalDirs
}

#gets a list of files in the first level of the working directory
function getFiles {

  files=$(find $1 -maxdepth 1 -type f | sort )
  echo $files

}

#remove empty files and directories
function cleanDir {
  #remove empty files from working directory
  find $workingDirectory -type f -size 0 -delete

  #remove empty directories forom working directory
  find $workingDirectory -type d -empty -delete

  echo "Cleaning process completed"
}

#selects compression and creates archive
function compressionMenu {
  file=""
  echo "Select compression type"
  echo "1>>gzip(archive.tar.gz)"
  echo "2>>bzip2(tarchive.tar.bz2)"
  echo "3>>xz(archive.tar.xz)"
  echo "4>>tar(archive.tar)"
  echo "5>>exit"

  read selection

  #compress files
  case $selection in
    [1]*)
    file="archize.tar.gz"
    selection="-c -z -f"
    ;;
    [2]*)
    file="archive.tar.bz2"
    selection="-c -j -f"
    ;;
    [3]*)
    file="archive.tar.xz"
    selection="-c -J -f"
    ;;
    [4]*)
    file="archive.tar"
    selection="-c -f"
    ;;
    [5]*)
    selection=-1
    ;;
    *)
    echo $clearConsole
    selection=-1
    echo "Invalid selection, returning to main menu"
    ;;
  esac

  if [[ $selection -ne -1 ]]
  then
    archive="${workingDirectory}/${file}"
    tar $selection $archive $1
    echo $clearConsole
    echo "${archive} created"
  fi

  mainMenu
}

#entry point for file compression process
function getFilesToCompress {
  echo $clearConsole
  found=0
  getFilesFromUser

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
      break
    fi
  done

  #do compression
  if [[ $found -eq 1 ]]
  then
    compressionMenu $fileList
  fi
  mainMenu

}

#print first level directory and files of working directory
function showWorkingDirectory {
  files=$(getFiles $workingDirectory)

  echo "Working Directory: $workingDirectory"
  showDirs

  if [[ ${#files[@]} -gt 0 ]]
  then
      echo "======Files======"
      for file in $files
      do
        echo $file | sed 's_'$workingDirectory'__g'
      done
  fi
}

#shows directories in working Directory
function showDirs {
  dirs=$(getDirList $workingDirectory)
  if [[ ${#dirs[@]} -gt 0 ]]
  then
      echo "===Directories==="
      for dir in $dirs
      do
        echo $dir | sed 's_'$workingDirectory'__g'
      done
  fi

}


echo $clearConsole
mainMenu
