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
#constants
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
POTTING_SOIL_VOLUME_CF = 1.8
PETUNIA_PRICE = 1.79
PI = 3.14
TAX_RATE = 0.15 #Based on example provided

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

#calculations - soil for pots(cubic inches)
soil_small_pot = area_small_pot * (SMALL_POT_HEIGHT / 2)
soil_med_pot = area_med_pot * (MED_POT_HEIGHT / 2)
soil_large_pot = area_large_pot * (LARGE_POT_HEIGHT / 2)

#calculations - petunias per pot
small_pot_petunias = int(area_small_pot / area_petunia)
med_pot_petunias = int(area_med_pot / area_petunia)
large_pot_petunias = int(area_large_pot / area_petunia)

#calculations - totals
total_soil = (soil_small_pot * pots_small) + (soil_med_pot * pots_med) + (soil_large_pot * pots_large)
total_soil_bags = (total_soil / (12 * 12 * 12)) / POTTING_SOIL_VOLUME_CF
if total_soil_bags - int(total_soil_bags) > 0:
    total_soil_bags += 1
total_soil_bags = int(total_soil_bags)

total_petunias = (small_pot_petunias * pots_small) + (med_pot_petunias * pots_med) + (large_pot_petunias * pots_large)
total_pot_price = (pots_small * SMALL_POT_PRICE) + (pots_med * MED_POT_PRICE) + (pots_large * LARGE_POT_PRICE)
total_petunia_price = total_petunias * PETUNIA_PRICE
total_soil_price = total_soil_bags * POTTING_SOIL_PRICE
subtotal_price = total_pot_price + total_petunia_price + total_soil_price
total_taxes = subtotal_price * TAX_RATE
total_price = subtotal_price + total_taxes


#output
print("-" * 50)
print("Supplies and Pricing for Order ", str(order_num))
print("\n")
print("Number of small pots:{:>3}{}".format(' ', pots_small))
print("Number of medium pots:{:>2}{}".format(' ', pots_med))
print("Number of large pots:{:3}{}".format(' ', pots_large))
print("\n")
print("Petunias:{:>16} plants".format(total_petunias))
print("Potting soil:{:>12} bags".format(total_soil_bags))
print("\n")
print("Subtotal{:>6}{: 9.2f}".format('$', subtotal_price))
print("Tax{:>11}{: 9.2f}".format('$',total_taxes))
print("TOTAL{:>9}{: 9.2f}".format('$', total_price))
print("-" * 50)
