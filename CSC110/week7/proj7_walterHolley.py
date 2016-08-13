#Project 7 - Python Keywords
#8/11/2016
'''
This program maintains a list of python keywords
to a file.  Allows the user to Search, add and remove keywords
from the keyword file.
'''
#GLOBALS
KEYWORD_FILE = 'PythonKeywords.txt'
KEYWORD_LIST = []
KEYWORD_LIST_SORTED = 0
IS_READONLY = False



#main entry point of program
def main():
    readKeywords()

    if len(KEYWORD_LIST) != 0:
        showMenu()
        if not KEYWORD_LIST_SORTED:
            sortKeywords()
        if not IS_READONLY:
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
    global IS_READONLY
    
    try:
        file = open(KEYWORD_FILE, 'r+')
    except FileNotFoundError:
        print('Keyword file not found')
    except PermissionError:
        try:
            file = open(KEYWORD_FILE, 'r')
            IS_READONLY = True
            print('Keyword file cannot be written to.  Limited options available')
        except PermissionError:
            print('Keyword file cannot be read.  Check permissions for file and containing folder')

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
    global KEYWORD_LIST_SORTED
    
    for i in range(0, len(KEYWORD_LIST)):
        minWord = KEYWORD_LIST[i]

        for c in range(0, len(KEYWORD_LIST)):
            if minWord < KEYWORD_LIST[c]:
                oldMinWord = minWord
                minWord = KEYWORD_LIST[c]
                KEYWORD_LIST[i] = minWord
                KEYWORD_LIST[c] = oldMinWord

        KEYWORD_LIST_SORTED = 1

#displays menu
def showMenu():
    menuOptions = []
    optionFunctions = {}
    leaveMenu = 0
    selectedOption = None

    #check access level
    if IS_READONLY:
        menuOptions = ['Search for keyword', 'Display keyword list']
        optionFunctions = {1: doKeywordSearch, 2: displayList}
    else:
        menuOptions = ['Search for keyword', 'Add keyword', 'Delete keyword', 'Display keyword list']
        optionFunctions = {1: doKeywordSearch, 2: doKeywordAdd, 3: doKeywordDelete, 4: displayList} 


    while not leaveMenu:
        validSelection = 0
        
        #display menu
        print()
        if not IS_READONLY:
            print('NOTE:  Changes will not be saved if the program does not exit through menu selection')
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
                if selectedOption < 0 or selectedOption > len(menuOptions):
                    print('Invalid Option')
                else:
                    validSelection = 1
            except ValueError:
                print('Invalid Option')
                continue
            
        if selectedOption > 0 and selectedOption <= len(menuOptions):
            optionFunctions[selectedOption]()
        else:
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
        for i in range(0, len(KEYWORD_LIST)):
            if KEYWORD_LIST[i] > keyword:
                KEYWORD_LIST.insert(i, keyword)
                break
            
        print(keyword, 'added to keyword list')

    #deletes keyword from list
def doKeywordDelete():
    keyword = str(input('Enter keyword: '))

    result = findKeywordIndex(keyword)
    
    if result != -99:
         del KEYWORD_LIST[result]
         print(keyword,'keyword removed')
    else:
         print('keyword not found')
              
#displays list of keywords
def displayList():

    if not KEYWORD_LIST_SORTED:
        sortKeywords()
    for word in KEYWORD_LIST:
          print(word)


##=========MAIN EXECUTION==========##
main()

#TESTING
'''
TEST: Full Access, invalid option selection input

RESULTS:
==============================
Keywords Menu
==============================
1.  Search for keyword
2.  Add keyword
3.  Delete keyword
4.  Display keyword list
0.  Exit
Enter Selection: dfgj
Invalid Option
Enter Selection: shsh
Invalid Option
Enter Selection: 4356
Invalid Option
Enter Selection: -2324
Invalid Option
Enter Selection: 0


TEST: Read-only access, option selections(Display list, search keyowrd)

RESULT:

Keyword file cannot be written to.  Limited options available

==============================
Keywords Menu
==============================
1.  Search for keyword
2.  Display keyword list
0.  Exit
Enter Selection: 2
as
assert
break
class
continue
def
del
elif
else

==============================
Keywords Menu
==============================
1.  Search for keyword
2.  Display keyword list
0.  Exit
Enter Selection: 1
Enter keyword: yield
Keyword found


TEST: add existing keyword, add new keyword, and view new keyword result


RESULT:
==============================
Keywords Menu
==============================
1.  Search for keyword
2.  Add keyword
3.  Delete keyword
4.  Display keyword list
0.  Exit
Enter Selection: 2
Enter keyword: max
keyword already exists

==============================
Keywords Menu
==============================
1.  Search for keyword
2.  Add keyword
3.  Delete keyword
4.  Display keyword list
0.  Exit
Enter Selection: 2
Enter keyword: min
min added to keyword list

==============================
Keywords Menu
==============================
1.  Search for keyword
2.  Add keyword
3.  Delete keyword
4.  Display keyword list
0.  Exit
Enter Selection: 4
as
assert
break
class
continue
def
del
elif
else
except
exec
finally
for
from
global
if
import
in
is
lambda
max
min


TEST: delete keyword that doesn't exist, delete existing keyword, display results

RESULT

==============================
Keywords Menu
==============================
1.  Search for keyword
2.  Add keyword
3.  Delete keyword
4.  Display keyword list
0.  Exit
Enter Selection: 3
Enter keyword: mark
keyword not found

==============================
Keywords Menu
==============================
1.  Search for keyword
2.  Add keyword
3.  Delete keyword
4.  Display keyword list
0.  Exit
Enter Selection: 3
Enter keyword: assert
assert keyword removed

==============================
Keywords Menu
==============================
1.  Search for keyword
2.  Add keyword
3.  Delete keyword
4.  Display keyword list
0.  Exit
Enter Selection: 4
as
break
class
continue
def
del
elif
else
except
exec

'''




#SUMMARY
'''
Like the previous projects, I sarted with what was known(which in this case, was everything),
stubbed out the call heirarchy, and implemented the functions.  I didn't really get stuck per se,
but I did have to do a little thinking around how the program's access could change when the
text file is read only.  Since the program isn't fully useless in read only, I decided to modify
the code a little bit in order to make the 'read' options available(Searching keyword, listing keywords).

For testing, I went through the scenarios mentioned in the project document.  I also went through some file access
(or lack thereof)scenarios, and input scenarios as well. While going through the input, I found that I was doing
a LOT of sorting where I didn't need to, and managed to reduce my sorting calls by implementing a check to see
if the sort had occurred at least once.  If the file is sorted one time, it will account for
anything that was put out of place manually, or just out of place to begin with.  The file only needs
to be sorted for display, and for writing back to the keyword file.

The big thing I took away from this was actually understanding why Python 'global' variables aren't truly global
in certain cases.  For all of the global immutable types, you can't modify them from other parts of the code
because python ends up creating a new variable that's local to the code.  Even if it has the same name.
A little research(and a hint from the keyword list) produced the global keyword, and I used that to
get over my issue.  As for future projects, I'll want to take additional care in my test cases, as there's much that
can happen.  The Goal is to become more attentive to detail in the programming classes after this.
There's so much you can do when you truly understand what's going on with all the types and functions
that are available to you.


'''
