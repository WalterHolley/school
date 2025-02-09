#Project 2 - Walter Holley
'''
7/5/2016
2D & 3D Geometry Calculator
This application takes in a value and unit
of measure.  Then, runs that information against
different geometrical formulas, and prints the results

DEPENDENCIES: p4geom.py
'''
from p4geom import *

##UTILITY FUNCTIONS##

#Prints a header section for the Calculations
def printCalculationSectionHeader(headerName):
    print("-" * 50)
    print("{} Calculations".format(headerName))
    print("-" * 50)

#fixes the precision of floating point numbers
#adjusts value so that floating point
#number is preserved to the hundredth(.01) decimal place
def fixPrecision(value):
    if value - int(value) == 0.00:
        return value
    
    result = value - 0.005
    if result < 0:
        return 0.00

    return result

##DISPLAY FUNCTIONS##

#Display sphere results report
#radius: radius of sphere
#unit: unit of measure
def displaySphereResults(radius, unit):
    volume = fixPrecision(sphereVolume(radius))
    area = fixPrecision(sphereSurfArea(radius))


    #print sphere results
    printCalculationSectionHeader('Sphere')
    print("{}{: 20.2f} {} {}".format("Volume:", volume, "cubic", unit))
    print("{}{: 14.2f} {} {}".format("Surface area:", area, "square", unit))
    print()
    


#Display circle results report
#radius: radius of circle
#unit: unit of measure
def displayCircleResults(radius, unit):
    circumference = fixPrecision(circleCircum(radius))
    area = fixPrecision(circleArea(radius))

    #print circle results
    printCalculationSectionHeader('Circle')
    print("{}{: 13.2f} {} {}".format("Circumference:", circumference, "linear", unit))
    print("{}{: 22.2f} {} {}".format("Area:", area, "square", unit))
    print()
    

#Display cube results report
#size: length of side
#unit: unit of measure
def displayCubeResults(size, unit):
    volume = fixPrecision(cubeVolume(size))
    area = fixPrecision(cubeSurfArea(size))

    #print cube results
    printCalculationSectionHeader('Cube')
    print("{}{: 20.2f} {} {}".format("Volume:", volume, "cubic", unit))
    print("{}{: 14.2f} {} {}".format("Surface area:", area, "square", unit))
    print()

#Display Square results report
#size: length of side
#unit: unit of measure
def displaySquareResults(size, unit):
    perimeter = fixPrecision(squarePerim(size))
    area = fixPrecision(squareArea(size))

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

    #gather input from user    
    value, unit = getInput()

    #display results
    displaySquareResults(value, unit)
    displayCubeResults(value, unit)
    displayCircleResults(value, unit)
    displaySphereResults(value, unit)


##MAIN EXECUTION##
main()


#TESTING
'''
input: 3, inches
EXPECTED OUTPUT

Square	
Perimeter	12.00
Area	         9.00
	
Cube	
Volume	        27.00
S. Area	        54.00
	
Circ	
Circum	     18.84
Area	     28.27
	
Sphere	
Volume	    113.09
S. Area	    113.09

RESULT
Square	
Perimeter	12.00
Area	         9.00
	
Cube	
Volume	        27.00
S. Area	        54.00
	
Circ	
Circum	     18.84
Area	     28.27
	
Sphere	
Volume	    113.09
S. Area	    113.09
###########################

input: 100 inches
EXPECTED OUTPUT
Square	
Perimeter	400
Area	      10000
	
Cube	
Volume	    1000000
S. Area	      60000
	
Circ	
Circum	     628.31
Area	   31415.92
	
Sphere	
Volume	 4188790.20
S. Area	  125663.70

RESULT
Square	
Perimeter	400
Area	      10000
	
Cube	
Volume	    1000000
S. Area	      60000
	
Circ	
Circum	     628.31
Area	   31415.92
	
Sphere	
Volume	 4188790.20
S. Area	  125663.70

'''

#SUMMARY
'''
Starting this project, I took the same approach as the previous one;
I started with what was known(formulas for shape calculations, call hierarchy),
and moved into the unknown, which really wasn't anything for this particular
project.  After formatting, I tested the calculations in the application by
putting the formulas in excel, and comparing the results in my application.
I found issues with rounding due to the format function I've been using for
organizing my print statements(you mentioned in the previous project that format
does round float values).  I believe I got over this by writing the fixPrecision
function, which accounts for the rounding possibilities of the format function.
As for errors, when you enter zero as the size value, you get a ZeroDivisionError.
This is due to, simply, dividing by zero in one of the equations(the sphereVolume
calculation).  This could be fixed with a conditional statement, but I decided to
leave this alone, and wait until the discussion around error handling.  In
the UI, the applicaton fails when non-numeric input is entered into the first question.
Like the previous project, I don't like the lack of error handling.  The repitition
was fixed by functions.
As for this assignment, I can't say I learned a lot, but I did look forward to
using functions.  As for the next project, I'm looking forward to using more conditionals.
I used them in this project, though they may not be allowed.
'''

