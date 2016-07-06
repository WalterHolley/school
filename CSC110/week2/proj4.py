#Project 2 - Walter Holley
'''
7/5/2016
2D & 3D Geometry Calculator
This application takes in a value and unit
of measure.  Then, runs that information against
different geometrical formulas, and prints the results
'''
from p4geom import *

##UTILITY FUNCTIONS##

#Prints a header section for the Calculations
def printCalculationSectionHeader(headerName):
    print("-" * 50)
    print("{} Calculations".format(headerName))
    print("-" * 50)


##DISPLAY FUNCTIONS##
'''
#Display sphere results report
def displaySphereResults(value, unit):
'''
#Display circle results report
def displayCircleResults(radius, unit):
    circumference = circleCircum(radius)
    area = circleArea(radius)

    #print circle results
    printCalculationSectionHeader('Circle')
    print("{}{: 9.2f} {} {}".format("Circumference:", circumference, "linear", unit))
    print("{}{: 14.2f} {} {}".format("Area:", area, "square", unit))
    print()
    

#Display cube results report
#size: length of side
#unit: unit of measure
def displayCubeResults(size, unit):
    volume = cubeVolume(size)
    area = cubeSurfArea(size)

    #print cube results
    printCalculationSectionHeader('Cube')
    print("{}{: 9.2f} {} {}".format("Volume:", volume, "cubic", unit))
    print("{}{: 14.2f} {} {}".format("Surface area:", area, "square", unit))
    print()

#Display Square results report
#size: length of side
#unit: unit of measure
def displaySquareResults(size, unit):
    perimeter = squarePerim(size)
    area = squareArea(size)

    #print square results
    printCalculationSectionHeader('Square')
    print("{}{: 17.2f} {} {}".format("Perimeter:", perimeter, "linear", unit))
    print("{}{: 22.2f} {} {}".format("Area:", area, "square", unit))
    print()
   



#get input from user, and display result reports
def getInput():
    value = float(input('Enter size for side and radius: '))
    unit = input('Enter unit of measure, e.g., inches: ')

    return value, unit

#primary point of execution
def main():
    value, unit = getInput()
    displaySquareResults(value, unit)
    displayCubeResults(value, unit)


##MAIN EXECUTION##
main()
