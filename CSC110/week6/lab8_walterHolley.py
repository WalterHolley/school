#lab8 - Walter Holley III 8/1/2016
#reading and writing from files
'''
reads a text file supplied by the user which contains a numebr on each line.
Places the even numbers in even.txt
Places the odd numbers in odd.txt
'''
file = None
writeFile = None
positiveTotal = 0
negativeCount = 0
oddCreated = 0
evenCreated = 0

#Get Number file
while file is None:
    try:
        fileName = input("Enter File Name:")
        file =  open(fileName, 'r')

    except FileNotFoundError:
        print("FILE NOT FOUND")

line = file.readline()

#Read Number file
try:
    while line != '':
        number  = float(line)

        if number < 0:
            negativeCount += 1
        else:
            positiveTotal += number

        if not number % 2:
            if not evenCreated:
                writeFile = open('even.txt', 'w')
                evenCreated = 1
            else:
                writeFile = open('even.txt','a')
        else:
            if not oddCreated:
                writeFile = open('odd.txt', 'w')
                oddCreated = 1
            else:
                writeFile = open('odd.txt','a')

        writeFile.write(str(number) + '\n')
        writeFile.close()
        

        line = file.readline()

    print('Positive Number Sum:', positiveTotal)
    print('Negative Number Count:', negativeCount)

except:
    print('An error has occurred')
finally:
    file.close()
     if writeFile is not None:
         writeFile.close()
    
    


