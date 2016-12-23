package com.lihe.until;


import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public class NumberUtil {

    private final static String JX = "JX";

    private final static String TY = "TY";

    public static List<String> get16BitJXRandomStringList(int num) {

        List<String> result = new ArrayList<String>();

        for (int i = 0; i < num; i++) {

            String uuid = UUID.randomUUID().toString();

            StringBuffer strbuf = new StringBuffer(NumberUtil.JX);

            strbuf.append(uuid.substring(0, 16).replaceAll("-", "").toUpperCase());

            result.add(strbuf.toString());

        }

        return result;

    }

    public static List<String> get16BitTYRandomStringList(int num) {

        List<String> result = new ArrayList<String>();

        for (int i = 0; i < num; i++) {

            String uuid = UUID.randomUUID().toString();

            StringBuffer strbuf = new StringBuffer(NumberUtil.TY);

            strbuf.append(uuid.substring(0, 16).replaceAll("-", "").toUpperCase());

            result.add(strbuf.toString());

        }

        return result;

    }

    public static String transRate(String rate) {

        String[] rateArr = rate.split("\\|");

        String relust = "";

        if (2 == rateArr.length) {

            if ("B".equals(rateArr[1])) {

                BigDecimal rateBigDecimal = new BigDecimal(rateArr[0]);

                relust = rateBigDecimal.multiply(new BigDecimal("0.1")).toString();

            } else {

                relust = rateArr[0];

            }

        }

        return relust;
    }

    public static String format(Number number) {
        BigDecimal b = new BigDecimal(Double.toString(number.doubleValue()));
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
    }
/*	
    public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
		
		System.out.println(NumberUtil.get16BitRandomStringSet(10000).size());
		
		System.out.println(System.currentTimeMillis());
	}
	*/


    public static Integer Division(int Divisor, long Dividend)
    {
        if(Dividend%Divisor==0)
        {
            return (int) Dividend/Divisor;
        }else{
            return (int) Dividend/Divisor+1;
        }
    }



    public   static  String percent( double  p1,  double  p2)  {
        String str;
        double  p3  = p2==0?0:p1  /  p2;
        NumberFormat nf  =  NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits( 2 );
        str  =  nf.format(p3);
        return  str;
    }



    /**
     * 将数字格式化输出
     *
     * @param value
     *            需要格式化的值
     * @param precision
     *            精度(小数点后的位数)
     * @return
     */
    public static String format(Object value, Integer precision) {
        Double number = 0.0;
        if (NumberUtil.isDigit(value)) {
            number = new Double(value.toString());
        }
        precision = (precision == null || precision < 0) ? 2 : precision;
        BigDecimal bigDecimal = new BigDecimal(number);
        return bigDecimal.setScale(precision, BigDecimal.ROUND_HALF_UP)
                .toString();
    }


    /**
     * 判断当前值是否为数字(包括小数)
     *
     * @param value
     * @return
     */
    public static boolean isDigit(Object value) {
        if (StringUtils.isEmpty(value)) {
            return false;
        }
        String mstr = value.toString();
        Pattern pattern = Pattern.compile("^(-?[1-9]\\d*\\.?\\d*)|(-?0\\.\\d*[1-9])|(-?[0])|(-?[0]\\.\\d*)$");
        return pattern.matcher(mstr).matches();
    }

    /**
     * 判断当前值是否为百分比
     *
     * @param value
     * @return
     */
    public static boolean isPercent(Object value) {
        if (StringUtils.isEmpty(value)) {
            return false;
        }
        String mstr = value.toString();
        Pattern pattern = Pattern.compile("^\\d+\\.?\\d*\\d%?$");
        return pattern.matcher(mstr).matches();
    }
}
