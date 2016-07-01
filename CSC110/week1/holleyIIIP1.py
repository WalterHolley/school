'''
Project 1 - Walter Holley III
6/17/2016
Takes input for an order of potted petunias
ranging from small to large sized pots, and
outputs the following:
-number of petunia plants needed
-total bags of potting soil needed
-the cost(subtotal, tax, and total price) of the order
'''
from decimal import ROUND_HALF_UP, Decimal

#constants
#unless otherwise noted, all height and diameter constants are in inches
SMALL_POT_DIAMETER = 9
SMALL_POT_HEIGHT = 10
SMALL_POT_PRICE = 9.99
MED_POT_DIAMETER = 12
MED_POT_HEIGHT = 14
MED_POT_PRICE = 15.99
LARGE_POT_DIAMETER = 18
LARGE_POT_HEIGHT = 20
LARGE_POT_PRICE = 24.99
POTTING_SOIL_PRICE = 14.79
POTTING_SOIL_VOLUME_CF = 1.8 #Volume of potting soil bag(cubic feet)
PETUNIA_PRICE = 1.79
PI = 3.14
TAX_RATE = 0.095

#input
order_num = input('Enter order number: ')
pots_small = int(input('Enter number of small pots:  '))
pots_med = int(input('Enter number of medium pots: '))
pots_large = int(input('Enter number of large pots:  '))

#calculations - pot radius
small_pot_radius = SMALL_POT_DIAMETER / 2
med_pot_radius = MED_POT_DIAMETER / 2
large_pot_radius = LARGE_POT_DIAMETER / 2

#calculations - pot and petunia surface area(square inches)
area_small_pot = PI * (small_pot_radius**2)
area_med_pot = PI * (med_pot_radius**2)
area_large_pot = PI * (large_pot_radius**2)
area_petunia = 4 * 4


#calculations - petunias per pot
small_pot_petunias = int(area_small_pot / area_petunia)
med_pot_petunias = int(area_med_pot / area_petunia)
large_pot_petunias = int(area_large_pot / area_petunia)

#calculations - soil
total_soil = ((area_small_pot * (SMALL_POT_HEIGHT / 2)) * pots_small) + ((area_med_pot * (MED_POT_HEIGHT / 2)) * pots_med) + ((area_large_pot * (LARGE_POT_HEIGHT / 2)) * pots_large)
total_soil_cf = float(total_soil / (12**3))
total_soil_bags = int(total_soil_cf / POTTING_SOIL_VOLUME_CF + 0.99)


#calculations - total petunias
total_petunias = (small_pot_petunias * pots_small) + (med_pot_petunias * pots_med) + (large_pot_petunias * pots_large)

#calculations - total prices
total_pot_price = (pots_small * SMALL_POT_PRICE) + (pots_med * MED_POT_PRICE) + (pots_large * LARGE_POT_PRICE)
total_petunia_price = total_petunias * PETUNIA_PRICE
total_soil_price = total_soil_bags * POTTING_SOIL_PRICE
subtotal_price = float(str("{:.2f}".format(total_pot_price + total_petunia_price + total_soil_price + 0.004)))
total_taxes = float(str("{:.2f}".format(subtotal_price * TAX_RATE + 0.004)))
total_price = subtotal_price + total_taxes


#output
print("-" * 50)
print("Supplies and Pricing for Order", str(order_num))
print("\n")
print("Number of small pots:{:>3}{}".format(' ', pots_small))
print("Number of medium pots:{:>2}{}".format(' ', pots_med))
print("Number of large pots:{:3}{}".format(' ', pots_large))
print("\n")
print("Petunias:{:>16} plants".format(total_petunias))
print("Potting soil:{:>12} bags".format(int(total_soil_bags)))
print("\n")
print("Subtotal{:>6}{: 9.2f}".format('$',subtotal_price))
print("Tax{:>11}{: 9.2f}".format('$',total_taxes))
print("TOTAL{:>9}{: 9.2f}".format('$', total_price))
print("-" * 50)


#TESTING
'''
Testing with non-zero, non-negative integers seems to be working fine.  When I use zero for all of the petunia pot fields,
the application charges for 1 bag of soil, as it presumes to need at least one bag.  This is incorrect.  I've made attempts
to correct this in the math, but have failed so far.  All other calculations appear to work fine.  The tests that were
executed are as follows:

1 small, 0 medium, 0 large.  Expected price: $33.01 Actual price: $33.01
2 small, 0 medium, 0 large.  Expected price: $49.83 Actual price: $49.83
0 small, 1 medium, 0 large.  Expected price: $47.43 Actual price: $47.43
0 small, 0 medium, 1 large.  Expected price: $72.96 Actual price: $72.96
1 small, 1 medium, 1 large.  Expected price: $137.21 Actual price: $137.21
0 small, 0 medium, 0 large.  Expected price: $0.00  Actual price: $0.00

ERROR SCENARIOS
Enter text for a number:  ValueError: invalis literal for int() with base 10: 'text entered'
Enter float value for number of pots: ValueError: invalid literal for int() with base 10: <<any float value>>
While it's good the script doesn't accept these values, they should be handled for a better user expeerience.
'''

#SUMMARY
'''
I approached this assignment starting off with what was known(costs, flower pot dimensions, etc.).
From there, I progressed through the script using the formulas provided, from there, it was a matter of
tallying price, and presenting the information.  I did get stuck around the issue of rounding without
using the round() function, it didn't take much in order to find an alternative, but I did have to
consider carrying the .000nth digit with some values, as it could be added and further adjusst the total.
I decided to get rid of it with formatting.  I tested the program by doing the math manually for a few
scenarios,and I'm fairly pleased with how things turned out.
What I don't like is the lack of error handling, and repetition involved with the code.  I repeat most of
the formulas several times, and the script can't parse for numeric values.  I get the impression that we'll
discuss functions and exception handling later on.  The key thing I learned about in this project was the
decimal object(no longer used).  I wasn't aware of it before this project.  For the next project, I'd like to use functions
to reduce code repitition and possibly start filtering input in order to reduce unexpected behavior.
'''
