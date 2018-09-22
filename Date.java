package package1;

import java.util.Calendar;

class Date {
	String year,month,day;
	public Date(String year, String month,String day) {
		this.year=year;
		this.month=month;
		this.day=day;
		
	}
	
	public Date() {
		Calendar calendar = Calendar.getInstance(); 
		this.year=String.valueOf(calendar.get(Calendar.YEAR));
		this.month=String.valueOf(calendar.get(Calendar.MONTH));
		this.day=String.valueOf(calendar.get(Calendar.DATE));
	}
	
	public String getYear() {
		return this.year;
	}
	
	public String getMonth() {
		return this.month;
	}
	
	public String getDay() {
		return this.day;
	}
}
