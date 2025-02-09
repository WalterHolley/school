#project 5 - Test Cases
#Walter Holley III
#Test cases for project 5.  Requires proj5_walterHolley.py

from proj5_walterHolley import *




#enty point for test program
def main():
    testPrices()
    testStrings()



def printResults(testValues, expectedResults, actualResults):
    for i in range(0, len(testValues)):
        print('{}{:<22}{:>12}{:<24}{}{}==>{}'.format('Test Value:', testValues[i], 'Expected:', expectedResults[i], ' ','Result:', actualResults[i]))



def testPrices():
    testValues = ['$20.46', '2,345.76', '$345,654', '$200', '0']
    expectedResults = ['20.46', '2345.76', '345654.0', '200.0', '0.0']
    actualResultsCostPrice = []
    actualResultsFixPrice = []
    
    print('##TESTING PRICES##')
    for i in range(0, len(testValues)):
        actualResultsCostPrice.append(fixCost(testValues[i]))
        actualResultsFixPrice.append(fixPrice(testValues[i]))

    print('Testing Cost Price Result')
    printResults(testValues, expectedResults, actualResultsCostPrice)
    print()
    print('Testing Part Price Result')
    printResults(testValues, expectedResults, actualResultsFixPrice)
    print()


def testStrings():
    yearStrings = ['12345869', '98743652', '52163298']
    descriptionStrings = ['DdDD', 'TestYourMight', 'I mean yeahNot Really']
    typeStrings = ['PART', 'pArT', 'acc']
    partNumberStrings = ['oooollll', 'AIRF13078', 'DRPNO432l']
    locationStrings = [' Nagasaki Japan', 'S R t 45', '234,567']
    
    startYearExpected = ['1234', '9874', '5216']
    endYearExpected = ['5869', '3652', '3298']
    descriptionExpected = ['Dd D D', 'Test Your Might', 'I mean yeah Not Really']
    typeExpected = ['Part', 'Part', 'Accessory']
    partNumberExpected = ['00001111', 'AIRF13078', 'DRPN04321']
    locationExpected = ['Nagasaki,Japan','S,R,t,45', '234,567']

    actualResultsStartYear = []
    actualResultsEndYear = []
    actualResultsDescription = []
    actualResultsType = []
    actualResultsPartNumber = []
    actualResultsLocation = []

    print('##TESTING STRINGS##')
    for i in range(0, 3):
        actualResultsStartYear.append(fixStartYear(yearStrings[i]))
        actualResultsEndYear.append(fixEndYear(yearStrings[i]))
        actualResultsDescription.append(fixDescription(descriptionStrings[i]))
        actualResultsType.append(fixType(typeStrings[i]))
        actualResultsPartNumber.append(fixPartNumber(partNumberStrings[i]))
        actualResultsLocation.append(fixLocation(locationStrings[i]))


    print('Testing Start Year Result')
    printResults(yearStrings, startYearExpected, actualResultsStartYear)
    print()
    print('Testing End Year Result')
    printResults(yearStrings, endYearExpected, actualResultsEndYear)
    print()
    print('Testing Description Result')
    printResults(descriptionStrings, descriptionExpected, actualResultsDescription)
    print()
    print('Testing Type Result')
    printResults(typeStrings, typeExpected, actualResultsType)
    print()
    print('Testing Part Number Result')
    printResults(partNumberStrings, partNumberExpected, actualResultsPartNumber)
    print()
    print('Testing Location Result')
    printResults(typeStrings, typeExpected, actualResultsType)
    print()

    
    



main()
