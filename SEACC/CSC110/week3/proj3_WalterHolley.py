#Project 3 - Fun with Selection
'''
7/8/2016
Implements several functions that use selection control structures.
Display their results.
'''



def main():
    displayEmpRating()
    displayNscGrade()
    displayIsDivisibleBy5or7or11()
    displayCommission()



##=====================UTILITY FUNCTIONS=========================================##


#prints header section
#headerText: Text for header
def printHeader(headerText):
    print("-" * 50)
    print("{}".format(headerText.upper()))
    print("-" * 50)


#prints the content for the commision display
#position: Employee position
#inStateAmount: Sales amount earned in-state
#outStateAmount: Sales amount earned out of state
#monthsEmployed: Numbe of months with company
def printCommissionDisplay(position, inStateAmount, outStateAmount, monthsEmployed):
    print("{}{:>40}".format("Position:", position))
    print("{}{:>15}{:16,.2f}".format("In State Earnings:", "$", inStateAmount))
    print("{}{:>11}{:16,.2f}".format("Out Of State Earnings:","$", outStateAmount))
    print("{}{:>33}".format("Months Employed:", monthsEmployed))
    print("{}{:>22}{:16,.2f}".format("Commission:", "$", commission(position, inStateAmount, outStateAmount, monthsEmployed)))
    print()





##================================OPERATION FUNCTIONS============================##




#returns a description of an employee's numeric rating
#rating: the employee numeric rating
def empRating(rating):
    description = "ERROR"

    if rating == 10:
        description = "Excellent"
    elif rating == 8 or rating == 9:
        description  = "Good"
    elif rating >= 5 and rating <= 7:
        description = "Acceptable"
    elif rating == 3 or rating == 4:
        description = "Needs Improvement"
    elif rating == 1 or rating == 2:
        description = "Probation"

    return description


#Converts a percentage to an Nsc style grade
#grade: the students percentage grade(ex: 98.4)
def pctToNsc(grade):
    nscGrade = -9.9
    
    if grade <= 100 and grade >= 0:
        nscGrade = ((grade - 60) / 10) + .4

        if nscGrade > 4.0:
            nscGrade = 4.0
        elif nscGrade < 1.0:
            nscGrade = 0.0

    return nscGrade



#Determines if a number is divisible by 5, 7, or 11
#number: numberic value to be checked.
def isDivivsibleBy5or7or11(number):

    return number % 5 == 0 or number % 7 == 0 or number % 11 == 0



#Calculates the commission for an employee
#position: Employee's position
#inStateAmount: Dollar amount sold in-state
#outStateAmount: Dollar amount sold out of state
#monthsAtCompany: number of months employee has been with company
def commission(position, inStateAmount, outStateAmount, monthsAtCompany):
    totalCommission = 0
    bumpRate = 0
    inStateRate = 0
    outStateRate = 0
    position = position.lower()

    #determine in/out state commission rates

    if position == "trainee":
        inStateRate = 0.01
        outStateRate = 0.02
    elif position == "associate":
        inStateRate = 0.03
        outStateRate = 0.05       
    elif position == "lead":
        inStateRate = 0.04
        outStateRate = 0.06
    elif position == "manager":
        inStateRate = 0.05
        outStateRate = 0.08


    #determine 'bump' rate

    if monthsAtCompany >= 120:
        bumpRate = 0.03    
    elif monthsAtCompany >= 48 and monthsAtCompany <= 119:
        bumpRate = 0.02
    elif monthsAtCompany >= 24 and monthsAtCompany <= 47:
        bumpRate = 0.01

    #calculate commission

    totalCommission = (inStateAmount * inStateRate + outStateAmount * outStateRate)
    totalCommission += totalCommission * bumpRate

    return totalCommission






##====================DISPLAY FUNCTIONS==========================================##


#Writes employee rating tests to console
def displayEmpRating():
    printHeader("Employee Rating Results")
    print("{:<8}{:>2}{:^5}{}".format("Rating:", "10", "-->", empRating(10)))
    print("{:<8}{:>2}{:^5}{}".format("Rating:", "0", "-->", empRating(0)))
    print("{:<8}{:>2}{:^5}{}".format("Rating:", "7", "-->", empRating(7)))
    print("{:<8}{:>2}{:^5}{}".format("Rating:", "9", "-->", empRating(9)))
    print("{:<8}{:>2}{:^5}{}".format("Rating:", "1", "-->", empRating(1)))
    print()
    



#Writes Nsc Grade tests to console
def displayNscGrade():
    printHeader("Percentage to NSC Grade results")
    print("{:<12}{:>3}{:^5}{:>4}".format("Percentage:", "-10", "-->", pctToNsc(-10)))
    print("{:<12}{:>3}{:^5}{:>4}".format("Percentage:", "100", "-->", pctToNsc(100)))
    print("{:<12}{:>3}{:^5}{:>4}".format("Percentage:", "63", "-->", pctToNsc(63)))
    print("{:<12}{:>3}{:^5}{:>4}".format("Percentage:", "87", "-->", pctToNsc(87)))
    print("{:<12}{:>3}{:^5}{:>4}".format("Percentage:", "101", "-->", pctToNsc(101)))
    print()




#Writes is divisible by 5, 7, or 11 tests to console   
def displayIsDivisibleBy5or7or11():
    printHeader("Divisible by 5, 7, or 11 results")
    print("{:<8}{:<}{:^5}{}".format("Number:", "33", "-->", isDivivsibleBy5or7or11(33)))
    print("{:<8}{:<}{:^5}{}".format("Number:", "47", "-->", isDivivsibleBy5or7or11(47)))
    print("{:<8}{:<}{:^5}{}".format("Number:", "15", "-->", isDivivsibleBy5or7or11(15)))
    print("{:<8}{:<}{:^5}{}".format("Number:", "49", "-->", isDivivsibleBy5or7or11(49)))
    print("{:<8}{:<}{:^5}{}".format("Number:", "24", "-->", isDivivsibleBy5or7or11(24)))
    print()
    



#Writes commission tests to console   
def displayCommission():
    printHeader("Commission Results")
    printCommissionDisplay("Trainee", 4000, 5000, 53)
    printCommissionDisplay("Associate", 10234.43, 600, 8)
    printCommissionDisplay("Lead", 1000000.46, 345968.33, 121)
    printCommissionDisplay("Manager", 5000, 329.74, 36)
    printCommissionDisplay("Trainee", -4769.03, 300, 2)




    







##================================MAIN EXECUTION=================================##
main()





#SUMMARY
'''
Much like the previous projects, I took what was known(function names, commission calculation),
and then worked towards accounting for what wasn't known, which in this case, was nothing.
When I say nothing, I mean all of the specifications were given, and I just had to go from there.
I stubbed out all the functions I would need first, then implemented the operations functions.
After that, I implemented the display functions.  In the main function, I added one diplay function,
and tested the output from that function until the code worked and displayed properly, then added
an additional display function call to main, and repeated the process for the remaining display function calls.
The commission calculations were verified using excel.

For what I don't like, I would've preferred to have made a decision in commission around taking in negative sales number,
as there's no way of handling them at this time.  Also, while functions have helped quite a bit in reducing
repitition, I'm still repeating lines of code.  Particlulary these print statements
that are near carbon copies of eachother.  A loop structure would help with this problem, but those
won't be discussed until later on.

Primary take-away from this assignment are the additional exercises in string formatting.  That
took a little more time compared to the previous projects.  This could be very useful
for scripts and console utilities.

'''
