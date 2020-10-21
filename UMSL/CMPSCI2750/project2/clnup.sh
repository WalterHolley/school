#!/bin/bash

function mainMenu {
  echo "***********"
  echo "* Cleanup *"
  echo "***********"
  echo "1>>Clear empty files and directories"
  echo "2>>Move Files"
  echo "3>>Compress files"
  echo "4>>Change Directory"
  echo "5>>Exit"

  read selection

  echo "you entered $selection"
}

mainMenu
