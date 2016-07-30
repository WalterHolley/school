#Project 5 - Inventory Data Fixup
#Walter Holley III
'''
Parses through inventory data and
presents the entry in a readable format.
Record order is pre-determined, so the program
only handles a specific sequence of data.
Currently parses parts and accessories data
'''



#main execution of program
def main():
    inventoryString = str(input("Enter inventory string:")).split("#")
    itemType = fixCommon(inventoryString)

    if itemType == "Part":
        fixPartSpecific(inventoryString)
        

##================UTILITY FUNCTIONS====================##


#writes formatted inventory line
def printInventoryLine(itemDescription, itemValue):
    print("{:<15}{}".format(itemDescription, itemValue))

        





##================OPERATIONS FUNCTIONS=================##



#prints parts related elements of a record
def fixPartSpecific(data):
    printInventoryLine("Make:", fixMake(data[7]))
    printInventoryLine("Model:", fixModel(data[8]))
    printInventoryLine("Year Start:", fixStartYear(data[9]))
    printInventoryLine("Year End:", fixEndYear(data[9]))



#prints common elements all of records
def fixCommon(data):
    itemType = fixType(data[1])
    printInventoryLine("Type:", itemType)
    printInventoryLine("Part#:", fixPartNumber(data[2][:9]))
    printInventoryLine("Description:", fixDescription(data[2][9:])) 
    printInventoryLine("Qty:", fixQuantity(data[3]))
    printInventoryLine("Loc:", fixLocation(data[4]))
    printInventoryLine("Cost:", fixCost(data[5]))
    printInventoryLine("Price:", fixPrice(data[6]))

    return itemType
    

#capitalizes the type value
def fixType(typeValue):
    
    if typeValue.lower() == "part":
        typeValue = typeValue.capitalize()
    else:
        typeValue = "Accessory"

    return typeValue



#fixes part number.  Replaces the letter 'o' w/zeros, and 'l' with 1
def fixPartNumber(partNumber):
    return partNumber.lower().replace(' ','').replace('o', '0').replace('l', '1').upper()
    


#puts spaces between capital letters in the item description
def fixDescription(partNumber):
    oldPartNumber = partNumber.strip()
    newPartNumber = ''
    
    for i in range(0, len(oldPartNumber)):
        if i > 0:
            if oldPartNumber[i].isupper():
                newPartNumber += ' '

        newPartNumber += oldPartNumber[i]

    return newPartNumber
                

#converts quantity value to integer
def fixQuantity(quantity):
    return int(quantity)



#removes all whitespaces from location string
def fixLocation(location):
    return location.strip().replace(' ','')



#strips dollar sign and commas from cost amount
def fixCost(cost):
    return float(cost.replace('$','').replace(',',''))



#strips dollar sign and commas from price amount
def fixPrice(price):
    return fixCost(price)




def fixMake(make):
    return make



def fixModel(model):
    return model


#returns first half of year string
def fixStartYear(yearString):
    return int(yearString[:4])


#returns second half of year string
def fixEndYear(yearString):
    return int(yearString[4:])
    


##================MAIN EXECUTION=======================##

#blocks main execution when imported by other files
if __name__ == "__main__":
    main()



#TESTING
#See proj5_tests_walterHolley.py
'''
I worked on a test harness for this project. You'll see the
results inside of the above mentioned file.  I know it's not required,
but I decided to give it a shot by writing code that executed the
'fix' functions, and displayed what I thought the result should be, versus
what was actually produced.  This helped me pick up on a few issues
with some of the string slices I was doing earlier.
'''

#SUMMARY
'''
The approach for this was much like the other projects; implement
what's known, and then fill in what isn't.  There wasn't much to
really think about here, as most of the material was provided,
all that needed to be done was implement the requirements for the
fields.

What I don't particularly like about this project is that it doesn't
handle anything beyond the specific formatting of the inventory lines.
There's plenty of room for bad data, and potentially many errors that can
crash the program.

The test harness is the bif take-away here.  It took a little bit of digging
to understand how to get things working properly; especially to prevent execution
of main for this program.
Honorable mention goes to string slicing.  I haven't spent too much
time with it, so it was good to get some practice; particularly around
handling the start/end date and Part/description fields.

For the next project, it looks like we'll be dealing with files.  I'm
sure the string manipulation we did in this project will come in handy
there, so I'll look to continue with string manipulation in that
project.
'''
