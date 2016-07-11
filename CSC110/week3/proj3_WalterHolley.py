#Project 3 - Fun with Selection
'''
7/8/2016
Implements functions that use slection control structures.
Display their results.
'''
#returns a description of an employee's numeric rating
#rating: the employee numeric rating
def empRating(rating):
    description = "No Description"

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

    return nscGrade

def isDivivsibleBy5or7or11(number):

    if number % 5 == 0 or number % 7 == 0 or number % 11 == 0:
        return true

    return false

def commission(position, inStateAmount, outStateAmount, monthsAtCompany):
    

def main():
