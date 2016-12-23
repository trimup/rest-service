/*
 * Copyright (c) 2016. lihe-fund All Rights Reserved.
 */

package com.lihe.crawler;

import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author leo
 * @version V1.0.0
 * @package com.lihe.crawler
 * @date 8/29/16
 */
public final class Util {
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(YYYY_MM_DD);

    // 判断一个字符串是否都为数字
    public static boolean isDigit(String strNum) {
        Pattern pattern = Pattern.compile("[0-9]{1,}");
        Matcher matcher = pattern.matcher(strNum);
        return matcher.matches();
    }

    //截取数字
    public static String splitNumbers(String content) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }
    //截取时间
    public static String splitDate(String content) {
        Pattern pattern = Pattern.compile("[0-9-]{1,}");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    // 截取非数字
    public static String splitNotNumber(String content) {
        Pattern pattern = Pattern.compile("\\D+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }
}
