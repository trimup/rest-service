/*
 * Copyright (c) 2016. lihe-fund All Rights Reserved.
 */

package com.lihe.entity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * lh_fund_info   利和基金详情表
 * lh_fund_rate_info  基金费率表
 * lh_fund_net_day  每日净值表
 * Created by trimup on 2016/8/25.
 * //基金费率
 */
@Deprecated
@Slf4j
@Data
public class FundRateinfo {
    private static final String KEY1 = "申购费率（前端）";
    private static final String KEY2 = "申购费率（后端）";
    private static final String KEY3 = "认购费率（前端）";
    private static final String KEY4 = "认购费率（后端）";
    private static final String KEY5 = "赎回费率";
    private static final String REGEX = " ";
    private Integer id;    //    主键ID
    private String fund_code = "";    //    基金代码
    private String amount_range = "";    //    金额区间
    private String orgiginal_rate = "";    //    原始费率
    private String preferred_rate = "";    //    优惠费率
    private Integer rate_type;    //    费率类型 1申购(前端)2申购(后端)3认购(前端)4认购(后端)5赎回
    private Date update_time;// 更新时间

    public static Set<FundRateinfo> toBean(Map<String, List<String>> beanInfos) {
        String code = beanInfos.get(Key.CODE).get(0);
        Set<FundRateinfo> list = new HashSet<>();
        beanInfos.forEach((k, v) -> {
            if (k.equals(KEY1)) {
                key12(code, list, v, 1);
            }
            if (k.equals(KEY2)) {
                key12(code, list, v, 2);
            }
            if (k.equals(KEY3)) {
                key12(code, list, v, 3);
            }
            if (k.equals(KEY4)) {
                key12(code, list, v, 4);
            }
            if (k.equals(KEY5)) {
                key12(code, list, v, 5);
            }
        });

        return list;
    }

    private static void key12(String code, Set<FundRateinfo> list,
        List<String> v, int type) {
        v.forEach(l -> {
            if (StringUtils.isEmpty(l))
                return;
            String[] ds = l.split(REGEX);
            FundRateinfo info = new FundRateinfo();
            info.setRate_type(type);
            info.setAmount_range(ds[0]);
            String[] rates = ds[2].split("\\|");

            if (rates != null) {
                if (rates.length > 0) {
                    info.setOrgiginal_rate(rates[0].trim());
                } else if (rates.length > 1) {
                    info.setPreferred_rate(rates[1].trim());
                }
            }
            info.setFund_code(code);
            info.setUpdate_time(new Date());
            log.info(info.toString());
            list.add(info);
        });
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof FundRateinfo))
            return false;

        FundRateinfo that = (FundRateinfo) o;

        if (!fund_code.equals(that.fund_code))
            return false;
        if (amount_range != null ?
            !amount_range.equals(that.amount_range) :
            that.amount_range != null)
            return false;
        return rate_type.equals(that.rate_type);

    }

    @Override public int hashCode() {
        int result = fund_code.hashCode();
        result = 31 * result + (amount_range != null ? amount_range.hashCode() : 0);
        result = 31 * result + rate_type.hashCode();
        return result;
    }
}
