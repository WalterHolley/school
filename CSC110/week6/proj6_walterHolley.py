#project 6 - Walter Holley III
#8/4/2016
#File I/O and exception handling
'''
Manages customer data by accepting user input
and saving to files.
CustData.txt - Holds customer information
CustCount.txt - Holds customer count
'''
#CONSTANTS
CUST_DATA_FILE = "CustData.txt"
CUST_COUNT_FILE = "CustCount.txt"


#entry point for program
def main():
    custFile = None
    countFile = None
    customersAdded = 0

    custFile = getFile(CUST_DATA_FILE, 'a')
    countFile = getFile(CUST_COUNT_FILE, 'r+')
    if custFile is not None and countFile is not None:
        customersAdded = addNewCusts(custFile)
        custFile.close()
        try:
            count = int(str(countFile.readline()))
            count += customersAdded
            countFile.seek(0,0)
            countFile.write(str(count))
        except ValueError:
            print("The value  for the number of customers isn't formatted properly.")
            countFile.close()
            if fixCustomerCountFile():
                print("File repaired.")
        finally:
            countFile.close()
        
    #check for open files and close them
    if custFile is not None:
        custFile.close()
    if countFile is not None:
        countFile.close()

    #testing purposes
    #dumpCustData()
    print("The program has been terminated")


##==========UTILITY FUNCIONS==========##

#produces readable customer name
def formatCustomerName(nameRecord):
    name = ''
    for i in range(0, len(nameRecord)):
        name += nameRecord[i] + ' '

    return str(name).strip()

#retreive a file with the given permissions
def getFile(fileName, permissions):
    file = None
    try:
        file = open(fileName, permissions)
    except FileNotFoundError:
        print("{} was not found.  Please make sure the file is in the same directory as this script.".format(fileName))
    except PermissionError:
        print("{}: Access Denied. Disable read-only access on this file or the containing directory in order to continue".format(fileName))

    return file
    
#makes an attempt to repair the value in the customer count file
def fixCustomerCountFile():
    custFile = getFile(CUST_DATA_FILE, 'r')
    countFile = getFile(CUST_COUNT_FILE, 'w')
    fixSuccessful = 0
    
    if custFile is not None and countFile is not None:

        #get line count for customer file
        lineCount = 0
        line  = custFile.readline()

        while line != '':
            lineCount += 1
            line = custFile.readline()

        countFile.write(str(lineCount))
        fixSuccessful = 1

    if custFile is not None:
        custFile.close()
    
    return fixSuccessful    
    

##========OPERATIONS FUNCTIONS=====##
            
#add new customer 
def addNewCusts(custFile):
    addCustomer = 1
    customersAdded = 0

    while addCustomer:          
        custName = str(input("Enter Customer Name:")).strip()

        if custName != '':
            custNumber = inputCustNum()
            custBalance = inputCustBal()
            custFile.write("{1:}{0:}{2:.2f}{0:}{3:}{4:}".format(' ', custNumber, custBalance, custName,'\n'))
            customersAdded += 1                 
        else:
            addCustomer = 0
    return customersAdded

#collects customer number from user
def inputCustNum():
    validInput = 0
    custNumber = 0

    while not validInput:
        try:
            custNumber = int(input("Enter Customer Number:"))
            validInput = 1
        except ValueError:
            print("For Customer Number, use humbers only(ex: 12345)")

    return custNumber

#collects customer balance from user
def inputCustBal():
    validInput = 0
    custBalance = 0

    while not validInput:
        try:
            custBalance = float(input("Enter Customer Balance:"))
            validInput = 1
        except ValueError:
            print("For Balances, use numbers or decimals(ex: 23, 43.65)")

    return custBalance          
        
#dumps customer file contents to console
def dumpCustData():
    countFile = None
    custFile = None
    filesFound = 1
    
    countFile = getFile(CUST_COUNT_FILE,'r')
    custFile = getFile(CUST_DATA_FILE, 'r')

    if countFile is not None and custFile is not None:

        #output customer count
        custCount = int(countFile.readline())
        print("Number of customers:{:<2}{}".format(' ', custCount))

        #output Customer list
        line = custFile.readline()
        while line != '':
            record = line.split(' ')
            name = formatCustomerName(record[2:])
            print("Number{0:^3}{1:}, Name{0:^3}{2:}, Balance{0:^3}{3:}".format('=', record[0], name, record[1]))
            line = custFile.readline()

    #close file objects
    if countFile is not None:
        countFile.close()
    if custFile is not None:
        custFile.close()
    
    
##========MAIN EXECUTION===========##
main()




#TESTING
'''
TEST
Executing Directory has been set to read-only:
RESULT
CustData.txt: Access Denied. Disable read-only access on this file or the containing directory in order to continue
The program has been terminated

TEST
Setting CustCount.txt to read-only
RESULT
CustCount.txt: Access Denied. Disable read-only access on this file or the containing directory in order to continue
The program has been terminated

TEST
Enter multiple users with incorrect formats for numbers adn balances:
RESULT
Enter Customer Name:Walter H
Enter Customer Number:123
Enter Customer Balance:65
Enter Customer Name:Mark W
Enter Customer Number:dfhg
For Customer Number, use humbers only(ex: 12345)
Enter Customer Number:4t.5
For Customer Number, use humbers only(ex: 12345)
Enter Customer Number:1234
Enter Customer Balance:sgh
For Balances, use numbers or decimals(ex: 23, 43.65)
Enter Customer Balance:45.67
Enter Customer Name:Marvin D Martian
Enter Customer Number:sdglhs
For Customer Number, use humbers only(ex: 12345)
Enter Customer Number:2356.79
For Customer Number, use humbers only(ex: 12345)
Enter Customer Number:4562
Enter Customer Balance:balance
For Balances, use numbers or decimals(ex: 23, 43.65)
Enter Customer Balance:1234.56
Enter Customer Name:
Number of customers:  9
Number = 59372, Name = Marvin deMarcien, Balance = 143.97
Number = 95738, Name = Doff E. Duckett, Balance = 571.13
Number = 79264, Name = Huey D. Louie, Balance = -5.25
Number = 3451, Name = Marvin D Martian, Balance = -1200.45
Number = 4, Name = Phil T Rich, Balance = 555555555.00
Number = 34, Name = goodspaceguy, Balance = 234.56
Number = 123, Name = Walter H, Balance = 65.00
Number = 1234, Name = Mark W, Balance = 45.67
Number = 4562, Name = Marvin D Martian, Balance = 1234.56
The program has been terminated

TEST
CustCount.txt name changed to CustCount2.txt
RESULT
CustCount.txt was not found.  Please make sure the file is in the same directory as this script.
The program has been terminated

TEST
CustCount.txt is readable, but the value can't be converted to an integer
RESULT
Enter Customer Name:
The value  for the number of customers isn't formatted properly.
File repaired.
Number of customers:  9
Number = 59372, Name = Marvin deMarcien, Balance = 143.97
Number = 95738, Name = Doff E. Duckett, Balance = 571.13
Number = 79264, Name = Huey D. Louie, Balance = -5.25
Number = 3451, Name = Marvin D Martian, Balance = -1200.45
Number = 4, Name = Phil T Rich, Balance = 555555555.00
Number = 34, Name = goodspaceguy, Balance = 234.56
Number = 123, Name = Walter H, Balance = 65.00
Number = 1234, Name = Mark W, Balance = 45.67
Number = 4562, Name = Marvin D Martian, Balance = 1234.56
The program has been terminated

TEST
CustCount.txt is readable, but the value can't be converted to an integer.  Multiple entries added
RESULT
Enter Customer Name:Luke Sywalker
Enter Customer Number:3463
Enter Customer Balance:66.00
Enter Customer Name:obi W Kenobi
Enter Customer Number:5968
Enter Customer Balance:999.99
Enter Customer Name:
The value  for the number of customers isn't formatted properly.
File repaired.
Number of customers:  10
Number = 59372, Name = Marvin deMarcien, Balance = 143.97
Number = 95738, Name = Doff E. Duckett, Balance = 571.13
Number = 79264, Name = Huey D. Louie, Balance = -5.25
Number = 3451, Name = Marvin D Martian, Balance = -1200.45
Number = 4, Name = Phil T Rich, Balance = 555555555.00
Number = 34, Name = goodspaceguy, Balance = 234.56
Number = 123, Name = Walter H, Balance = 65.00
Number = 1234, Name = Mark W, Balance = 45.67
Number = 3463, Name = Luke Sywalker, Balance = 66.00
Number = 5968, Name = obi W Kenobi, Balance = 999.99
The program has been terminated
'''

#SUMMARY
'''
Like the other projects, I started with what was known(files and their contents, required functions, general program flow)
and went from there.  After getting the base functionality up and running(update data file and customer count file), I started
implementing the code for the individual error cases laid out in the instructions.  I tested a few cases individually as I went
along.  By that, I mean I attemped to produce the error case before writing the exception, got the exception error, and used
the traceback information to populate the error types in my exception cases.

During testing(dumpTestData is currently commented out), particularly around file access testing, I figured the process I used for testing the custcount file would be
useful for the customer data file as well, so I subjected it to the same rigors as the count file, with the exception of the
content check that's performed against custcount.txt.  I also thought it may be a good idea to make an attempt at fixign the
customer count issue using the data in the customer data file.  This wasn't required, but I'll admit that it bothered me a
little to not try and correct something like that when you have the data available to do so.  The correction works, but
I will readily admit that a count of a data file's lines may not be the best approach if the file isn't maintained properly.

The big take-away from this assignment is that files, while necessary, aren't really the best means for storing data that's constantly
updating and changing.  They would be great for storing settings to an application, but not great for storing bank records.
Also, being specific around your exceptions is very good for handling specific problems, but it's not a wide net for catching issues.
I could see there possibly being a good reason for being broad with your exceptions, but those cases may be better served at the
'top' of the program, when all of your case-specific exceptions have been bypassed.  Our next lesson deals with lists, so I'd
expect that we'll be combining that lesson with everything we've done here.  I expect to take the knowledge we've gained around exceptions
and files into that.  As for improvements, I'm hoping we can include the lessons around lists with files.  It should make
it a little easier to manage the data we pull from files.
'''
