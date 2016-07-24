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
    printInventoryLine("Part#:", fixPartNumber(data[2][0:8]))
    printInventoryLine("Description:", fixDescription(data[2][9: -1]))
    printInventoryLine("Qty:", fixQuantity(data[3]))
    printInventoryLine("Loc:", fixLocation(data[4]))
    printInventoryLine("Cost:", fixCost(data[5]))
    printInventoryLine("Price:", fixPrice(data[6]))

    return itemType
    


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
                


def fixQuantity(quantity):
    return int(quantity)



#removes all whitespaces from location string
def fixLocation(location):
    return location.strip().replace(' ','')



#strips dollar sign and commas from cost amount
def fixCost(cost):
    return cost.replace('$','').replace(',','')



#strips dollar sign and commas from price amount
def fixPrice(price):
    return fixCost(price)




def fixMake(make):
    return make



def fixModel(model):
    return model


#returns first half of year string
def fixStartYear(yearString):
    return yearString[0:4]


#returns second half of year string
def fixEndYear(yearString):
    return yearString[4:8]
    


##================MAIN EXECUTION=======================##

main()
