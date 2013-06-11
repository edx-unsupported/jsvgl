package edu.umb.jsVGL.client.VGL;

import java.io.UnsupportedEncodingException;
/*
 * compresses strings from two-byte to one-byte
 * and uncompresses them
 * 
 * only works for chars that can be converted to ASCII
 * 
 * cannot have a * as last character, since it's used as padding
 */

public class StringCompressionUtils {
	
	public static String compress(String input) {
		// must be even number of characters
		if ((input.length() % 2) == 1) input = input + "*";
		
		StringBuffer out = new StringBuffer();
		byte[] bytes = null;
		try {
			bytes = input.getBytes("US-ASCII");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
		int i = 0;
		while (i < input.length()) {
			int code = bytes[i] + (256 * bytes[i + 1]);
			out.append(Character.toChars(code));
			i = i + 2;
		}
		return out.toString();
	}
	
	public static String uncompress(String input) {
		StringBuffer out = new StringBuffer();
		char[] chars = input.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			int x = chars[i];
			out.append(Character.toChars(x & 0x00FF));
			out.append(Character.toChars((x & 0xFF00) >> 8));
		}
		
		// remove trailing * if present
		if (out.substring(out.length() - 1).equals("*")) out.deleteCharAt(out.length() - 1);
		
		return out.toString();
	}
	
	public static void test() {
		String start = "Hi there!";
		String comp = compress(start);
		String out = uncompress(comp);
		System.out.println(start + " " + comp + " " + out);
	}

}
