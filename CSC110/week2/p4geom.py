'''
Walter Holley III
7/5/2016
Project 2 - 2D & 3D Geometry Calculator
This file provides calculations for various 2D and 3D geometrical
shapes.
'''
from math import pi

#SQUARE FUNCTIONS

#returns the perimeter of a square
def squarePerim(sideLength):
    return sideLength * 4

#returns the area of a square
def squareArea(sideLength):
    return sideLength**2

#CUBE FUNCTIONS

#returns the volume of a cube
def cubeVolume(sideLength):
    return sideLength**3

#returns the surface area of a cube
def cubeSurfArea(sideLength):
    return 6 * sideLength**2


#CIRCLE FUNCTIONS

#returns the circumference of a circle
def circleCircum(radius):
    return pi*(2*radius)

#returns the area of a circle
def circleArea(radius):
    return pi*radius**2
