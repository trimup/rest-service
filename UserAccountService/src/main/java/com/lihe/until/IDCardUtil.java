package com.lihe.until;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 身份证工具类
 * Created by leo on 8/6/15.
 */
public final class IDCardUtil {

    // 获取性别
    public static String gender(String ID) {
        if (null == ID || ID.length() < 18)
            return "";
        String gender = "男";
        String id17 = ID.substring(16, 17);
        if (Integer.parseInt(id17) % 2 != 0) {
            gender = "男";
        } else {
            gender = "女";
        }
        return gender;
    }


    // 获取性别code
    public static String genderCode(String ID) {
        if (null == ID || ID.length() < 18)
            return "";
        String gender = "1";
        String id17 = ID.substring(16, 17);
        if (Integer.parseInt(id17) % 2 != 0) {
            gender = "1";
        } else {
            gender = "0";
        }
        return gender;
    }

    public static String birthday(String ID) {
        if (null == ID || ID.length() < 18)
            return "";
        String birthday = ID.substring(6, 14);
        try {
            Date birthdate = new SimpleDateFormat("yyyyMMdd").parse(birthday);
            return new SimpleDateFormat("yyyy-MM-dd").format(birthdate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return birthday;
    }

    public static String age(String ID) {
        String brithday = birthday(ID);
        try {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
            String currentTime = formatDate.format(calendar.getTime());
            Date today = formatDate.parse(currentTime);
            Date brithDay = formatDate.parse(brithday);

            return today.getYear() - brithDay.getYear() + "";
        } catch (Exception e) {
            return "";
        }
    }

    public static String mask(String ID) {
        if (null == ID || ID.length() < 18)
            return "";
        return ID.substring(0, 2) + "************" + ID.substring(15);
    }
}
