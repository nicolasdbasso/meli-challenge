package com.meli.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationURL {

	// Function to validate URL
	// using regular expression
	public static boolean isValidURL(String url) {
		// Regex to check valid URL
		String regex = "((http|https)://)(www.)?" + "[a-zA-Z0-9@:%._\\+~#?&//=]" + "{2,256}\\.[a-z]"
				+ "{2,6}\\b([-a-zA-Z0-9@:%" + "._\\+~#?&//=]*)";

		// Compile the ReGex
		Pattern p = Pattern.compile(regex);

		// If the string is empty
		// return false
		if (url == null) {
			return false;
		}

		// Find match between given string
		// and regular expression
		// using Pattern.matcher()
		Matcher m = p.matcher(url);

		// Return if the string
		// matched the ReGex
		return m.matches();
	}

}
