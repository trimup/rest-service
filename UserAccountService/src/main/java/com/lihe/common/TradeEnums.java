/**
 * @Version 1.0.0
 * Copyright (c) 2016上海相诚金融-版权所有
 */
package com.lihe.common;

/**
 * @Class TradeEnums
 * @Description
 * @Author 张超超
 * @Date 2016/9/7 10:47
 */
public class TradeEnums {

    /**
     * 基金状态枚举类
     */
    public enum HSFundStatusEnum {

        ZC("0"), //正常
        FX("1"), //发行
        FXCG("2"), //发行成功
        FXSB("3"), //发行失败
        TZJY("4"), //停止交易
        TZSG("5"), //停止申购
        TZSH("6"), //停止赎回
        QYDJ("7"), //权益登记
        HLFF("8"), //红利发放
        JJFB("9"), //基金封闭
        JJZZ("a"); //基金终止

        private final String value;

        HSFundStatusEnum(String value) {
            this.value = value;
        };

        public String getValue() {
            return value;
        }
    }

    /**
     * 公募基金交易类型
     */
    public enum HSFundTradeEnum {

        SG(0), //申购
        RG(1), //认购
        SH(2); //赎回

        private final Integer value;

        HSFundTradeEnum(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    /**
     * 公募基金交易状态
     */
    public enum HSTradeStatusEnum {
        //待确认、已持仓、赎回中、已赎回
        QRSB(0), //确认失败
        QRCG(1), //确认成功
        BFQR(2), //部分确认
        SSQRCG(3), //实时确认成功
        YCXJY(4), //已撤销交易
        XWQR(5), //行为确认
        DQR(9); //待确认

        private final Integer value;

        HSTradeStatusEnum(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    /**
     * 公募基金交易状态(Character)
     */
    public enum HSTradeStatusCharacterEnum {
        //待确认、已持仓、赎回中、已赎回
        QRSB('0'), //确认失败
        QRCG('1'), //确认成功
        BFQR('2'), //部分确认
        SSQRCG('3'), //实时确认成功
        YCXJY('4'), //已撤销交易
        XWQR('5'), //行为确认
        DQR('9'); //待处理

        private final Character value;

        HSTradeStatusCharacterEnum(Character value) {
            this.value = value;
        }

        public Character getValue() {
            return value;
        }
    }

    /**
     * 基金交易返回结果枚举类
     */
    public enum HSTradeResultStatusEnum {

        CG("ETS-5BP0000"); //成功

        private final String value;

        HSTradeResultStatusEnum(String value) {
            this.value = value;
        };

        public String getValue() {
            return value;
        }
    }

    /**
     * 恒生申请类业务代码枚举类
     */
    public enum HSBusinessApplyCodeEnum {


        RG("020"), //认购
        SG("022"), //申购
        SH("024"); //赎回

        private final String value;

        HSBusinessApplyCodeEnum(String value) {
            this.value = value;
        };

        public String getValue() {
            return value;
        }
    }

    /**
     * 恒生扣款状态枚举类
     */
    public enum HSDeductStatusEnum {


        WJY('0'), //0:未校验（用户线下付款时）
        WX('1'), //1:无效
        YX('2'), //2:有效
        YFS('3'); //3:已发送扣款指令

        private final Character value;

        HSDeductStatusEnum(Character value) {
            this.value = value;
        };

        public Character getValue() {
            return value;
        }
    }
}
