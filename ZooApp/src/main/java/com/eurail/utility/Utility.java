package com.eurail.utility;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @author Amit
 *
 */
public class Utility {
	private static final String PATTERN = "yyyy-MM-dd";

	/**
	 * @param instant
	 * @return String date
	 */
	public static String formatDate(Instant instant) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN).withZone(ZoneId.from(ZoneOffset.UTC));
		String formattedDate = formatter.format(instant);

		return formattedDate;
	}
	
	/**
	 * @param str
	 * @return String
	 */
	public static String capitalize(String str){  
		if (str == null || str.length() == 0) 
			return str;  
		return str.substring(0, 1).toUpperCase() + str.substring(1);  
	}  

}
