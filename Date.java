package package1;

import java.io.Serializable;
import java.util.Calendar;

public class Date implements Serializable {
	String year, month, day;

	public Date(String year, String month, String day) {
		this.year = year;
		this.month = month;
		this.day = day;

	}

	public Date() {
		Calendar calendar = Calendar.getInstance();
		this.year = String.valueOf(calendar.get(Calendar.YEAR));
		this.month = String.format("%02d", (calendar.get(Calendar.MONTH) + 1));
		this.day = String.format("%02d", calendar.get(Calendar.DATE));

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
