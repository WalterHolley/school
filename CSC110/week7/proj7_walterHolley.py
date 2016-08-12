#Project 7 - Python Keywords
#8/11/2016
'''
This program maintains a list of python keywords
to a file.  Allows the user to Search, add and remove keywords
from the keyword file.
'''
KEYWORD_FILE = 'PythonKeywords.txt'
KEYWORD_LIST = []

#main entry point of program
def main():
    readKeywords()

    if len(KEYWORD_LIST) != 0:
        showMenu()
        sortKeywords()
        writeKeywords()


##=============HELPER FUNCTIONS=============##

#finds the keyword index, returns -99 if not found
def findKeywordIndex(keyword):
    result = -99

    for i in range(0, len(KEYWORD_LIST)):
          if keyword == KEYWORD_LIST[i]:
              result = i
              break

    return result

##==========OPERATIONS FUNCTIONS============##

#returns list of keywords from keyword file
def readKeywords():
    file = None

    try:
        file = open(KEYWORD_FILE, 'r')
    except FileNotFoundError:
        print('Keyword file not found')

    if file is not None:
        line = file.readline()

        while line is not '':
            KEYWORD_LIST.append(str(line).replace('\n', ''))
            line = file.readline()
        file.close()

#writes keywords list to file
def writeKeywords():
    file = None
    
    try:
        file = open(KEYWORD_FILE, 'w')
    except PermissionError:
        print('Access Denied:  Cannot write to keywords file')

    if file is not None:
        for i in range(0, len(KEYWORD_LIST)):
            file.write(str(KEYWORD_LIST[i]) + '\n')
        file.close()

#sorts keyword list in alpha order
def sortKeywords():
    minWord = ''
    for i in range(0, len(KEYWORD_LIST)):
        minWord = KEYWORD_LIST[i]

        for c in range(0, len(KEYWORD_LIST)):
            if minWord < KEYWORD_LIST[c]:
                oldMinWord = minWord
                minWord = KEYWORD_LIST[c]
                KEYWORD_LIST[i] = minWord
                KEYWORD_LIST[c] = oldMinWord

#displays menu
def showMenu():
    menuOptions = ['Search for keyword', 'Add keyword', 'Delete keyword', 'Display keyword list']
    optionFunctions = {1: doKeywordSearch, 2: doKeywordAdd, 3: doKeywordDelete, 4: displayList} 
    leaveMenu = 0
    selectedOption = None

    while not leaveMenu:
        validSelection = 0
        
        #display menu
        print()
        print('='*30)
        print('Keywords Menu')
        print('='*30)
        for i in range(0, len(menuOptions)):
                print('{}.{:<2}{}'.format(i + 1,' ', menuOptions[i]))
        print('{}.{:<2}{}'.format(0,' ', 'Exit'))

        #select option
        while not validSelection:
            try:
                selectedOption = int(input("Enter Selection: "))
                if selectedOption < 0 or selectedOption > 4:
                    print('Invalid Option')
                else:
                    validSelection = 1
            except ValueError:
                print('Invalid Option')
                continue
            
        if selectedOption > 0 and selectedOption < 5:
            optionFunctions[selectedOption]()
        elif selectedOption == 0:
            leaveMenu = 1             
          
#searches for a keyword
def doKeywordSearch():
    keyword = str(input('Enter keyword: '))

    result = findKeywordIndex(keyword)

    if result == -99:
          print('Keyword not found')
    else:
          print('Keyword found')

#adds keyword to list
def doKeywordAdd():
    keyword = str(input('Enter keyword: '))

    result = findKeywordIndex(keyword)

    if result != -99:
          print('keyword already exists')
    else:
          KEYWORD_LIST.append(keyword)
          print(keyword, 'added to keyword list')

#deletes keyword from list
def doKeywordDelete():
    keyword = str(input('Enter keyword: '))

    result = findKeywordIndex(keyword)
    
    if result != -99:
         del KEYWORD_LIST[result]
         print('keyword removed')
    else:
         print('keyword not found')
              
#displays list of keywords
def displayList():
    sortKeywords()
    for word in KEYWORD_LIST:
          print(word)


##=========MAIN EXECUTION==========##
main()
