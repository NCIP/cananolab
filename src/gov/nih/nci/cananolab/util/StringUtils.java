package gov.nih.nci.cananolab.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

/**
 * This class contains a set of utilities for converting Strings to other
 * formats or converting other formats to String.
 * 
 * @author pansu
 * 
 */
/* CVS $Id: StringUtils.java,v 1.9 2008-08-26 22:27:30 tanq Exp $ */

public class StringUtils {
	private static Logger logger = Logger.getLogger(StringUtils.class);

	public static boolean isImgFileExt(String fileName) {
		if (isEmpty(fileName)) {
			return false;
		}

		boolean isImgFileExt = false;
		for (int i = 0; i < Constants.IMAGE_FILE_EXTENSIONS.length; i++) {
			if (fileName.toUpperCase().endsWith(
					"." + Constants.IMAGE_FILE_EXTENSIONS[i])) {
				isImgFileExt = true;
				break;
			}
		}

		return isImgFileExt;
	}

	public static String join(String[] stringArray, String delimiter) {
		String joinedStr = "";
		if (stringArray == null || stringArray.length == 0) {
			return joinedStr;
		}
		List<String> stringList = Arrays.asList(stringArray);
		return join(stringList, delimiter);
	}

	public static String join(Collection<String> stringList, String delimiter) {
		String joinedStr = "";
		if (stringList == null || stringList.isEmpty()) {
			return joinedStr;
		}
		// remove empty items
		Collection<String> modList = new ArrayList<String>(stringList);
		for (String str : modList) {
			if (isEmpty(str)) {
				stringList.remove(str);
			}
		}

		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (String str : stringList) {
			if (i < stringList.size() - 1) {
				sb.append(str);
				sb.append(delimiter);
			} else {
				sb.append(str);
			}
			i++;
		}
		joinedStr = sb.toString();
		return joinedStr;
	}

	public static String sortJoin(Collection<String> strings, String delimiter) {
		SortedSet<SortableName> sortableNames = new TreeSet<SortableName>();
		for (String str : strings) {
			sortableNames.add(new SortableName(str));
		}
		String joinedStr = "";
		if (sortableNames == null || sortableNames.isEmpty()) {
			return joinedStr;
		}
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (SortableName sortableName : sortableNames) {
			if (i < sortableNames.size() - 1) {
				if (!StringUtils.isEmpty(sortableName.getName()))
					// joinedStr += sortableName.getName() + delimiter;
					sb.append(sortableName.getName());
				sb.append(delimiter);
			} else {
				if (!StringUtils.isEmpty(sortableName.getName()))
					// joinedStr += sortableName.getName();
					sb.append(sortableName.getName());
			}
			i++;
		}
		joinedStr = sb.toString();
		return joinedStr;
	}

	/*
	 * empty string of the collection will be included in the joined string and
	 * null item in the collection will be converted to an empty string
	 */
	public static String joinEmptyItemIncluded(
			Collection<String> stringCollection, String delimiter) {
		StringBuffer buffer = new StringBuffer();
		if (stringCollection == null || stringCollection.isEmpty()) {
			return buffer.toString();
		}

		Iterator iter = stringCollection.iterator();
		while (iter.hasNext()) {
			String item = (String) iter.next();
			if (item == null)
				item = "";
			buffer.append(item);
			if (iter.hasNext()) {
				buffer.append(delimiter);
			}
		}
		return buffer.toString();
	}

	public static Float convertToFloat(String floatStr) {
		if (isEmpty(floatStr)) {
			return null;
		}
		try {
			Float floatNum = Float.parseFloat(floatStr);
			return floatNum;
		} catch (NumberFormatException e) {
			logger.error("Error converting the given string to a float number",
					e);
			throw new RuntimeException(
					"Can't convert the given string to a float number: "
							+ floatStr);
		}
	}

	public static Long convertToLong(String longStr) {
		if (isEmpty(longStr)) {
			return null;
		}
		try {
			Long longNum = Long.parseLong(longStr);
			return longNum;
		} catch (NumberFormatException e) {
			logger.error("Error converting the given string to a long number",
					e);
			throw new RuntimeException(
					"Can't convert the given string to a long number: "
							+ longStr);
		}
	}

	public static String convertToString(Object obj) {
		if (obj == null) {
			return "";
		} else {
			return obj.toString();
		}
	}

	public static boolean isInteger(String theStr) {
		if (isEmpty(theStr)) {
			return false;
		} else {
			for (int i = 0; i < theStr.length(); i++) {
				if (!Character.isDigit(theStr.charAt(i))) {
					return false;
				}
			}
			return true;
		}
	}

	public static boolean isDouble(String theStr) {
		int decimalCount = 0;

		if (isEmpty(theStr)) {
			return false;
		} else {
			for (int i = 0; i < theStr.length(); i++) {
				if (!Character.isDigit(theStr.charAt(i))) {
					if (theStr.charAt(i) == ('.')) {
						decimalCount++;
						continue;
					} else {
						return false;
					}
				}
			}

			if (decimalCount == 1)
				return true;
			else
				return false;
		}
	}

	public static boolean contains(String[] array, String aString,
			boolean ignoreCase) {
		boolean containsString = false;

		for (int i = 0; i < array.length; i++) {
			if (ignoreCase) {
				if (array[i].equalsIgnoreCase(aString))
					containsString = true;
			} else {
				if (array[i].equals(aString))
					containsString = true;
			}
		}

		return containsString;
	}

	public static String[] add(String[] x, String aString) {
		String[] result = new String[x.length + 1];
		for (int i = 0; i < x.length; i++) {
			result[i] = x[i];
		}
		result[x.length] = aString;

		return result;
	}

	/**
	 * Convert a string with multiple words separated by space to one word, with
	 * first letter as lower case.
	 * 
	 * @param words
	 * @return
	 */
	public static String getOneWordLowerCaseFirstLetter(String words) {
		// remove space in words and make the first letter lower case.
		String oneWord = words;
		if (!isEmpty(words)) {
			String firstLetter = words.substring(0, 1);
			oneWord = words
					.replaceFirst(firstLetter, firstLetter.toLowerCase())
					.replace(" ", "");
		}
		return oneWord;
	}

	/**
	 * Convert a string with multiple words separated by space to one word, with
	 * first letter as upper case.
	 * 
	 * @param words
	 * @return
	 */
	public static String getOneWordUpperCaseFirstLetter(String words) {
		// remove space in words and make the first letter lower case.
		String firstLetter = words.substring(0, 1);
		String oneWord = words.replaceFirst(firstLetter,
				firstLetter.toUpperCase()).replace(" ", "");
		return oneWord;
	}

	/**
	 * Parse the text into an array of words using white space as delimiter.
	 * Keeping words in quotes together.
	 * 
	 * @param texts
	 * @return
	 */
	public static List<String> parseToWords(String text) {
		if (isEmpty(text)) {
			return null;
		}
		SortedSet<String> wordList = new TreeSet<String>();

		// extract words in quotes first
		String patternStr = "\\B[\"']([^\"']*)[\"']\\B";
		String[] nonQuotedTexts = text.split(patternStr);
		for (String txt : nonQuotedTexts) {
			String[] nonQuotedWords = txt.split("\\s");
			wordList.addAll(Arrays.asList(nonQuotedWords));
		}
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(text);

		List<String> quotedWords = new ArrayList<String>();
		int start = 0;
		while (matcher.find(start)) {
			String quotedWord = matcher.group(1).trim();
			quotedWords.add(quotedWord);
			start = matcher.end(1);
		}
		wordList.addAll(quotedWords);

		return new ArrayList<String>(wordList);
	}

	public static Boolean containsIgnoreCase(Collection<String> collection,
			String match) {
		for (String str : collection) {
			if (str.trim().equalsIgnoreCase(match.trim())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Return true for Null or empty string, false otherwise.
	 */
	public static boolean isEmpty(String str) {
		return (str == null || str.trim().length() == 0);
	}

	public static void main(String[] args) {
		try {
			String texts = "this is 'a test' of \"parsing words\"";
			System.out.println(texts);
			List<String> words = StringUtils.parseToWords(texts);
			for (String word : words) {
				System.out.println(word);
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}
}
