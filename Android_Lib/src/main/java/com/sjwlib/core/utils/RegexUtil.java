package com.sjwlib.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {
	public static String match(String input, String regex){
		Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);   
		Matcher matcher = pattern.matcher(input);   
		if(matcher.find()){
			return matcher.group();
		}else{
			return input;
		}
	}
	
	public static String match(String input, String regex, int groupid){
		Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);   
		Matcher matcher = pattern.matcher(input);   
		if(matcher.find()){
			return matcher.group(groupid);
		}else{
			return input;
		}
	}	
	
	public static String replace(String input, String regex, String replace){
		Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);   
		Matcher matcher = pattern.matcher(input);   
		if(matcher.find()){
			return matcher.replaceAll(replace);
		}else{
			return input;
		}
	}
	
	public static String[] split(String input, String regex){
		Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);   
		String[] result = pattern.split(input);
		return result;
	}

}
