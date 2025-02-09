#Lab 6 - Walter Holley III
'''
Shows the number of calories burned per minute
based on the calories per minute set in the program.
'''

minutes = 10
caloriesPerMinute = 4.2
print("Calories burned per minute:", caloriesPerMinute)
print("Calories burned after:")

while minutes < 31:
    print(minutes, "minutes:",int(caloriesPerMinute * minutes),"calories")
    minutes += 5
