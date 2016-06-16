#CSC110 Lab 2 - Walter Holley III

#user input
measurement = float(input("Enter length(feet): "))

#calculation
inches = round(float((measurement - int(measurement)) * 12),1)

#formatting
if round(inches - int(inches),1) == 0:
    inches = int(inches)

if round(measurement - int(measurement),1) == 0:
    measurement = int(measurement)

#output
print(str(measurement),"feet is equivalent to", int(measurement),"feet and", inches, "inches.")
