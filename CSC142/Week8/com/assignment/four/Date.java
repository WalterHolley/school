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
	
	public boolean isLeapYear(){
		return (_year % 4 == 0);
	}

}
