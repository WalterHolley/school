#Project 4 - Walter Holley III
#7/17/2016
'''
Amy's Auto Loan Payments
produces a report breaking down the details of
a potential buyer's loan payments
'''

#main entry point for program
def main():

    loanAmount, interestRatePerPeriod = inputLoanData()
    totalPeriods = showMenu();
  
    while totalPeriods:
        #determine payment
        paymentAmount = payment(loanAmount, interestRatePerPeriod, totalPeriods)

        #display report
        showReport(loanAmount, interestRatePerPeriod, totalPeriods, paymentAmount)

        #Pause for menu input
        input("Press <Enter> to continue")
        print()
        totalPeriods = showMenu()
        
        
##================UTILTITY FUNCTIONS===================##



#determines the rate at which interest is compounded
#annualInterest: the decimal form of the annual interest e.g: 0.029 = 2.9%
def getRatePerPeriod(annualInterest):
    return annualInterest / 12



#converts received interest value to percentage
#annualInterest: the float form of the annual interest e.g: 2.9
def convertInterest(annualInterest):
    return annualInterest * 0.01




##================OPERATIONS FUNCTIONS=================##


#Asks the user for details of the loan
#returns loan amount, and Annual interest rate
def inputLoanData():
    loanAmount = float(input("Enter loan amount, example 10000:"))
    interestRate = float(input("Enter annual interest rate, example 2.9:"))

    return loanAmount, getRatePerPeriod(convertInterest(interestRate))



#displays a menu for selecting loan terms(12 months, 24 months, etc.)
#returns number of periods
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
    print()

    #menu selection    
    while choice < 0 or choice > 5:
        choice = int(input("Choice:"))

    return choice * 12



#Calculates the base principal payment for the loan.  Rounded to nearest penny
#presentValue: full balance of the loan
#ratePerPeriod: the interest rate paid per payment period
#numberOfPeriod: the total number of periods from which interest will be paid
def payment(presentValue, ratePerPeriod, numberOfPeriods):
    return round((ratePerPeriod * presentValue) / (1 - (1 + ratePerPeriod)**-numberOfPeriods), 2)




#Displays report detailing the payments for each period through the term of the loan
#presentValue:  The ful principal value of the loan
#ratePerPeriod:  The interest rate charged per payment period
#numberOfPeriods:  The number of loan periods
#paymentAmount:  The determined payment amount for the loan
def showReport(presentValue, ratePerPeriod, numberOfPeriods, paymentAmount):
    totalPaymentAmount = 0

    
    #print report header
    print("{:<8}{:>20}{:>24}{:>24}{:>25}".format("Pmt#", "PmtAmt", "Int", "Princ", "Balance"))
    print("{:<8}{:>20}{:>24}{:>24}{:>25}".format("-" * 4, "-" * 9, "-" * 5, "-" * 6, "-" * 8))

    
    for i in range(1, numberOfPeriods + 1):

        #determine payments
        interestPayment = round(presentValue * ratePerPeriod, 2)
        principalPayment = paymentAmount - interestPayment
        
        

        #adjust balances
        presentValue -= principalPayment
        totalPaymentAmount += paymentAmount
        
        if i == numberOfPeriods:
            principalPayment += presentValue
            paymentAmount += presentValue
            totalPaymentAmount += presentValue
            presentValue -= presentValue
            

            
        #display payments and balances
        print("{:>4}{:>24,.2f}{:>24,.2f}{:>24,.2f}{:>25,.2f}".format(i, paymentAmount, interestPayment, principalPayment, presentValue))

        
        
    #display final balance
    print("{:>28}".format("-" * 9))
    print("{:>28,.2f}".format(totalPaymentAmount))
    print()
        
    




##=============MAIN EXECUTION==========================##
main()


#TESTING
'''
$20,000 at 2.9% for 12 months
Expected payoff amount: $20,315.54
Actual Payoff amount: $20,315.54

$20,000 at 2.9% for 48 months
Expected payoff amount: $21,206.55
Actual Payoff amount: $21,206.55


$3 at 20% for 12 months
Expected payoff amount: $3.33
Actual Payoff amount: $3.33


$3 at 20% for 60 months
Expected payoff amount: $4.80
Actual Payoff amount: $4.80

$1 trillion at 20% for 12 months
Expected payoff amount: $1,111,614,070,764.97
Actual Payoff amount: $1,111,614,070,764.97


-$20,000 at 2.9% for 12 months
Expected payoff amount: -$20,315.54
Actual Payoff amount: -$20,315.54


$967,523.65 at 10% for 48 months
Expected payoff amount: $1,177,867.11
Actual Payoff amount: $1,177,867.11
'''



#SUMMARY
'''
Like the previous project, I stubbed out the functions I needed, and fileld them in.
The calculations were verified through excel.  I tested the program by using small
(e.g: 20,000)and large(e.g: 1,000,000,000,000) numbers.  I got stuck around the issue
of formatting.  Particularly for the larger numbers.  I considered thinking of
a way to space things out dynamically, but decided that may be a little too much
for this project.  I decided to space thigns out far enough to properly display
numbers in the single trillions of dollars.  The big take-away for me in this project
is, once again, formatting.  Not so much the process of formatting, but considering
the space(or lack thereof) you have.  The (non)constraints here weren't so bad, but
they may be a thing to consider in the future when working on more complex programs
(websites, games, etc.)
'''
