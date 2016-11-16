package com.assignment.four;

public class Date {
	int _month;
	int _year;
	int _day;
	public Date(int year, int month, int day){
		_month = month;
		_year = year;
		_day = day;
	}
	
	public int getYear(){
		return _year;
	}
	
	public int getMonth(){
		return _month;
	}
	
	public int getDay(){
		return _day;
	}
	
	//returns a string representation of 
	//the date (year/month/day)
	public String toString(){
		return _year + "/" + _month + "/" + _day;
	}
	
	public boolean equals(Date d){
		boolean isEqual = true;
		
		if(_year != d.getYear()){
			isEqual = false;
		}
		else if(_month != d.getMonth()){
			isEqual = false;
		}
		else if(_day != d.getDay()){
			isEqual = false;
		}
		
		return isEqual;
	}
	
	//determines if this date falls on a leap year
	public boolean isLeapYear(){
		boolean isALeapYear = false;
		
		if(this._year % 100 == 0){
			if(this._year % 400 == 0){
				isALeapYear = true;
			}
		}
		else if(this._year % 4 == 0){
				isALeapYear = true;
		}
		
		return isALeapYear;
	}
	
	//advances the date one day
	public void nextDay(){
		int daysInMonth = getDaysInMonth();
		
		if(_day + 1 > daysInMonth){
			_day = 1;
			if(_month + 1 > 12){
				_month = 1;
				_year++;
			}
			else{
				_month++;
			}
		}
		else{
			_day++;
		}
		
	}
	
	//advances the date object to the
	//parameters date.  returns number
	//of days between the two date objects.
	public int advanceTo(Date endDay){
		int days = 0;
		
		while(!this.equals(endDay)){
			this.nextDay();
			days++;
		}
		
		return days;
	}
	
	//Returns a string representing the day of the week
	public String getDayOfTheWeek(){
		String dayOfWeek = null;
		
		//The minimum date begins on a monday
		Date minDate = new Date(1753, 1, 1);
		int days = minDate.advanceTo(this);
		days = days % 7;
		
		switch(days){
		case 0:
			dayOfWeek = "Monday";
			break;
		case 1:
			dayOfWeek = "Tuesday";
			break;
		case 2:
			dayOfWeek = "Wednesday";
			break;
		case 3:
			dayOfWeek = "Thursday";
			break;
		case 4:
			dayOfWeek = "Friday";
			break;
		case 5:
			dayOfWeek = "Saturday";
			break;
		case 6:
			dayOfWeek = "Sunday";
			break;
		}
		
		return dayOfWeek;
	}
	
	//determines the number of days in a month
	private int getDaysInMonth(){
		int[] thirtyDayMonths = {9,4,6,11};
		int days = 31;
		
		if(_month == 2){
			days =	(isLeapYear())? 29 : 28;
		}
		else{
			for(int i = 0; i < thirtyDayMonths.length; i++){
				if(_month == thirtyDayMonths[i]){
					days = 30;
					break;
				}
			}
		}
		
		return days;
	}
	
	
	

}
