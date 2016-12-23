package com.lihe.until;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringQedUtil {
	/**
	 * Double类型数据格式化(.##)
	 * @param d
	 * @return
	 */
	public static String getFormatString(Double d) {
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(d);
	}


	/**
	 * 判断是否是数字（仅校验int）
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str){ 
	   Pattern pattern = Pattern.compile("[0-9]+(\\.[0-9]+)?"); 
	   Matcher isNum = pattern.matcher(str);
	   if(!isNum.matches()){
	       return false; 
	   } 
	   return true; 
	}

	/**
	 * 判断是否是空
	 * @param str
	 * @return
     */
	public static boolean isBlank(String str) {
		if (null == str || "".equals(str.trim())) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否不为空
	 * @param str
	 * @return
	 */
	public static boolean isNotBlank(String str) {
		if (null != str && !"".equals(str.trim())) {
			return true;
		}
		return false;
	}
}
