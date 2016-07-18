#Project 4 - Walter Holley III
#7/17/2016
'''
Amy's Auto Loan Payments
produces a report breaking down the details of
a potential buyer's loan payments
'''

#main entry point for program
def main():
    loanAmount, interestRate = inputLoanData()
    totalPeriods = showMenu();

    while totalPeriods:
        #determine payment
        paymentAmount = payment(loanAmount, interestRate, totalPeriods)

        #display report
        

        #Pause
        
        
##================UTILTITY FUNCTIONS===================##

#returns the present value of the loan
#loanBalance: current loan balance
#periodsRemaining: number of interest/payment periods left in the loan
#interestRate: rate of interest charged per period
def getPresentValue(loanBalance, periodsRemaining, interestRate):
    return loanBalance / (1 + interestRate)**periodsRemaining




##================OPERATIONS FUNCTIONS=================##


#Asks the user for details of the loan
def inputLoanData():
    loanAmount = float(input("Enter loan amount, example 10000:"))
    interestRate = float(input("Enter annual interest rate, example 2.9:"))

    return loanAmount, interestRate

def showMenu():
    choice = -1

    
    #menu Header
    print("-" * 50)
    print("Amy's Auto - Loan Report Menu")
    print("-" * 50)


    #menu contents
    for i in range(0,6):
        print("{}.{:<2}{}-month loan".format(i, " ", i * 12))

    print("{}.{:<2}EXIT".format(0, " "))


    #menu selection    
    while choice < 0 and choice > 5:
        choice = int(input("Choice:"))

    return choice * 12


def payment(presentValue, ratePerPeriod, numberOfPeriods):
    return (ratePerPeriod * presentValue) / (1 - (1 + ratePerPeriod)**-numberOfPeriods)


def showReport(PV, interestRate, numberOfPeriods, paymentAmount):
    return 0
    




##=============MAIN EXECUTION==========================##
main()
