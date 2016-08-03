#lab9 - Walter Holley III 8/2/2016
#exception handling lab
'''
Take in a user's age.  If the age can't be converted to an
integer, catch the error and assign -99 to the age
'''

try:
    userAge = int(input('Enter Age:'))
except:
    userAge = -99

print("User's Age: {}".format(userAge))
