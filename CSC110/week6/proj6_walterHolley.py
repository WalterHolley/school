#project 7 - Walter Holley III
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

        try:
            count = int(str(countFile.readline()))
            count += customersAdded
            countFile.seek(0,0)
            countFile.write(str(count))
        except ValueError:
            print("The value  for the number of customers isn't formatted properly.")
            if fixCustomerCountFile(countFile, custFile):
                print("File repaired.")
        finally:
            countFile.close()
        dumpCustData()
            

    #check for open files and close them
    if custFile is not None:
        custFile.close()
    if countFile is not None:
        countFile.close()   


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
        print("{} was not found.  Please make sure the file is in the same directory as this script.".format(CUST_DATA_FILE))
    except PermissionError:
        print("{}: Access Denied. Disable read-only access on this file or the containing directory in order to continue".format(CUST_DATA_FILE))

    return file
    
#makes an attempt to repair the value in the customer count file
def fixCustomerCountFile(countFile, dataFile):
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
            custFile.write("{1:}{0:}{2:.2f}{0:}{3:}{4:}".format(' ', custNumber, custBalance, custName, '\n'))
            customersAdded += 1                 
        else:
            addCustomer = 0
            custFile.close()
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
