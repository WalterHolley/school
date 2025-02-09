# Lab 3 starter code - Walter Holley III


# CSC 110

import math

def main():
    print("This program is to test functions")
    
    # put your function calls here
    testAreaTrapezoid(4, 5, 8, 36)
    testAreaTrapezoid(2, 7, 9, 40.5)
    testGetHypotenuse(3, 4, 5)
    testGetHypotenuse(3, 7, 7.6)


  
# This function calculates and returns the area of a trapezoid
# parameter: base1, the length of the top of the trapezoid
# parameter: base2, the length of the bottom
# parameter: height, the height of the trapezoid
# See this website for a picture  http://math.com/tables/geometry/areas.htm
def areaTrapezoid(base1, base2, height):

    area = height / 2.0 * (base1 + base2)
    return area

#Returns the hypotenuse of a right triangle based on the length of its legs
#param: leg1, length of first leg
#param: leeg2, length of second leg
def getHypotenuse(leg1, leg2):

    hypotenuse = (leg1**2 + leg2**2)**(1/2)    
    return hypotenuse

#test call for getAreaTrapezoid
def testAreaTrapezoid(base1, base2, height, expectedAnswer):
    print('Calling areaTrapezoid where base1 = {}, base2 = {}, and height = {}.  Expecting {}.'
          .format(base1, base2, height, expectedAnswer))
    answer = areaTrapezoid(base1, base2, height)
    printResult(answer)

#test call for getHypotenuse
def testGetHypotenuse(leg1, leg2, expectedAnswer):
    print('Calling getHypotenuse where leg1 = {}, and leg2 = {}.  Expecting {}.'
          .format(leg1, leg2, expectedAnswer))
    answer = getHypotenuse(leg1,leg2)
    printResult(answer)
   
#print result
def printResult(answer):
    print('Output:{result}'.format(result= int(answer) if answer - int(answer) == 0 else float(str("{:.1f}").format(answer))))

    

##MAIN EXECUTION##
main()
