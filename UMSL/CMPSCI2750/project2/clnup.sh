#!/bin/bash

#INSTRUCTIONS
#Search and remove all empty files and directories within the working directory.
#Provide a series of filenames to move into their own directory (also provided by the user)
#Provide a series of filenames to be combined via tar, along with a choice of what method of compression the user would like to use.
#Navigate to another directory (move to the directory, then print the working directory and list all the files and directories).
#Exit the program
#If a user provides a file or directory name that does not exist, prompt the user with an error message and reprompt the user for the proper file names.
#Bonus: Capture SIGINT and call a function to say goodbye to the user.

workingDirectory=$PWD
clearConsole=$(printf "\033c")
fileList=""



#exits the program when SIGINT is detected
function exitOnSigint {
  echo " SIGINT detected.  Goodbye!"
  exit 0
}

#main loop for script
function mainMenu {
  runScript=1
  until [[ $runScript -eq 0 ]]
  do
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
      1)
      #clear Files
      cleanDir
      ;;
      2)
      #move Files
      moveFiles
      ;;
      3)
      #compress Files
      getFilesToCompress
      ;;
      4)
      #change working directory
      changeDir
      ;;
      5)
      echo "Exiting tool."
      runScript=0
      ;;
      *)
      echo "Invalid selection"
      ;;
    esac
  done
  exit 0
}

#moves files within working directory
function moveFiles {

  getFilesFromUser

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

      if [[ $yesno != "y" ]]
      then
        echo $clearConsole
        echo "File move aborted"
        return 0
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
  until [[ $found -eq 1 ]]
    do
        echo "Working Directory: $workingDirectory"
        echo "Enter files[ex: file.txt dir1/file2.txt]:"

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
      done
}

#changes the working directory
function changeDir {
  found=0

  until [[ found -ne 0 ]]
  do
    echo "Working Directory: $workingDirectory"
    echo "Enter exact Directory[ex: /dir1/dir2]:"
    read selection
    found=$( find $selection -maxdepth 0 -type d | wc -l )

    echo $clearConsole

    if [[ $found -eq 0 ]]
    then
      echo "$selection: Directory not found"
    else
      workingDirectory=$selection
      echo "Working directory updated"
      showWorkingDirectory
    fi
  done
}

#gets a list of directories in the first level of the working directory
function getDirList {

  internalDirs=$(find $1 -maxdepth 1 -type d | sort )
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
    1)
    file="archize.tar.gz"
    selection="-c -z -f"
    ;;
    2)
    file="archive.tar.bz2"
    selection="-c -j -f"
    ;;
    3)
    file="archive.tar.xz"
    selection="-c -J -f"
    ;;
    4)
    file="archive.tar"
    selection="-c -f"
    ;;
    5)
    echo $clearConsole
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
    tar $selection $archive $fileList
    echo $clearConsole
    echo "${archive} created"
  fi
}

#entry point for file compression process
function getFilesToCompress {
  echo $clearConsole
  found=0
  getFilesFromUser

  #do compression
  if [[ $fileList != "-1" ]]
  then
    compressionMenu
  fi
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
        if [[ $dir != $workingDirectory ]]
        then
          echo $dir | sed 's_'$workingDirectory'__g'
        fi
      done
  fi

}

trap exitOnSigint SIGINT

echo $clearConsole
mainMenu
