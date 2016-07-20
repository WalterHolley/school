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
        paymentAmount = payment(loanAmount, getRatePerPeriod(interestRate), totalPeriods)

        #display report
        showReport(loanAmount, interestRate, totalPeriods, paymentAmount)

        #Pause for menu input
        input("Press <Enter> to continue")
        totalPeriods = showMenu()
        
        
##================UTILTITY FUNCTIONS===================##

def getInterestPayment(totalPayment, interestRate):
    return totalPayment * interestRate


def getRatePerPeriod(annualInterest):
    return annualInterest / 12

#converts received interest value to percentage
def convertInterest(annualInterest):
    return annualInterest * 0.01

##================OPERATIONS FUNCTIONS=================##


#Asks the user for details of the loan
def inputLoanData():
    loanAmount = float(input("Enter loan amount, example 10000:"))
    interestRate = float(input("Enter annual interest rate, example 2.9:"))

    return loanAmount, convertInterest(interestRate)

def showMenu():
    choice = -1

    
    #menu Header
    print("-" * 50)
    print("Amy's Auto - Loan Report Menu")
    print("-" * 50)


    #menu contents
    for i in range(1,6):
        print("{}.{:<2}{}-month loan".format(i, " ", i * 12))

    print("{}.{:<2}EXIT".format(0, " "))


    #menu selection    
    while choice < 0 or choice > 5:
        choice = int(input("Choice:"))

    return choice * 12

#Calculates the base principal payment for the loan
def payment(presentValue, ratePerPeriod, numberOfPeriods):
    return round((ratePerPeriod * presentValue) / (1 - (1 + ratePerPeriod)**-numberOfPeriods), 2)


def showReport(presentValue, annualInterestRate, numberOfPeriods, paymentAmount):
    interestRate = getRatePerPeriod(annualInterestRate)
    totalPaymentAmount = 0

    print(paymentAmount)
    #print report header
    print("{:<8}{:>9}{:>10}{:>11}{:>12}".format("Pmt#", "PmtAmt", "Int", "Princ", "Balance"))
    print("{:<8}{}{:>10}{:>11}{:>12}".format("-" * 4, "-" * 9, "-" * 5, "-" * 6, "-" * 8))

    
    for i in range(1, numberOfPeriods + 1):

        #determine payments
        interestPayment = presentValue * interestRate
        principalPayment = paymentAmount - interestPayment
        #totalPaymentAmount += principalPayment + interestPayment
        totalPaymentAmount += paymentAmount
        
        #reduce balances and periods
        presentValue -= principalPayment
            
        #display payments
        print("{:>4}{:>13,.2f}{:>10.2f}{:>11.2f}{:>11.2f}".format(i, paymentAmount, interestPayment, principalPayment, presentValue))

    print(totalPaymentAmount)

        
    return 0
    




##=============MAIN EXECUTION==========================##
main()
