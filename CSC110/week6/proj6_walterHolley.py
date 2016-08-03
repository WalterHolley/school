#project 7 - Walter Holley III
#File I/O and exception handling
'''
Manages customer data by accepting user input
and saving to files.
CustData.txt - Holds customer information
CustCount.txt - Holds customer count
'''
#CONSTANTS
FILE_NOT_FOUND_ERROR = "One or more files are missing.  Make sure files are in the same directory with this script."
CUST_DATA_FILE = "CustData.txt"
CUST_COUNT_FILE = "CustCount.txt"



#entry point for program
def main():

    customersAdded = inputNewCusts(CUST_DATA_FILE)
    dumpCustData()


##==========UTILITY FUNCIONS==========##

def formatCustomerName(nameRecord):
    name = ''
    for i in range(0, len(nameRecord)):
        name += nameRecord[i] + ' '

    return str(name).strip()
                   

def formatCustomerData(custNumber, balance, custName):
    return str(custNumber, custName, balance)

def updateTotalCustomers(customersAdded):
    file = None
    try:
        file = open(CUST_COUNT_FILE, 'r+')
        count = int(file.readline())
        count += customersAdded
        file.seek(0,0)
        file.write(str(count))
    except FileNotFoundError:
        print(FILE_NOT_FOUND_ERROR)
    finally:
        if file is not None:
            file.close()

#add new customer 
def inputNewCusts(custFile):
    addCustomer = 1
    customersAdded = 0

    while addCustomer:          
            custName = str(input("Enter Customer Name:")).strip()

            if custName != '':
                try:
                    custNumber = int(input("Enter Customer Number:"))
                    custBalance = float(input("Enter Customer Balance"))

                    file = open(custFile, 'a')
                    file.write(formatCustomerData(custNumber, custBalance, custName), + '\n')
                    customersAdded += 1
                except ValueError:
                    print("For Customer Number, use humbers only(ex: 12345)")
                    print("For Balances, use numbers or decimals(ex: 23, 43.65)")
                finally:
                    if file is not None:
                        file.close()
            else:
                addCustomer = 0       
            

#dumps customer file contents to console
def dumpCustData():
    countFile = None
    custFile = None
    filesFound = 1
    
    try:
        countFile = open('CustCount.txt','r')
        custFile = open('CustData.txt', 'r')
    except FileNotFoundError:
        print(FILE_NOT_FOUND_ERROR)
        filesFound = 0
        if countFile:
            countFile.close()

    if filesFound:
        #output customer count
        custCount = int(countFile.readline())
        countFile.close()
        print("Number of customers:{:<2}{}".format(' ', custCount))

        #output Customer list
        line = custFile.readline()
        while line != '':
            record = line.split(' ')
            name = formatCustomerName(record[2:])
            print("Number{0:^3}{1:}, Name{0:^3}{2:}, Balance{0:^3}{3:}".format('=', record[0], name, record[1]))
            line = custFile.readline()
  
        
    
##========MAIN EXECUTION===========##
main()
