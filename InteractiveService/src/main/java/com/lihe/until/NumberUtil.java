package com.lihe.until;

import java.math.BigDecimal;

/**
 * Created by leo on 8/28/15.
 */
public class NumberUtil {

    public static String format(Number number) {
        BigDecimal b = new BigDecimal(number.doubleValue());
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
    }



    /**
     * //判断两个数字相除 如果有余数 自动+1  如无 就返回原数
     * @param Divisor 除数
     * @param Dividend 被除数
     * @return
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
}
