/*
 * Copyright (c) 2016. lihe-fund All Rights Reserved.
 */

package com.lihe.entity;

import com.lihe.crawler.Util;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * lh_fund_info   利和基金详情表
 * lh_fund_rate_info  基金费率表
 * lh_fund_net_day  每日净值表
 * Created by trimup on 2016/8/25
 * 基金每日净值.
 */
@Slf4j
@Data
public class FundNetDayInfo {
    private Integer id;//  '主键ID',
    private String fund_code;//  '基金代码',
    private Date create_time;//  '创建时间',
    private String update_time;//更新时间
    private String hqrq;   //  '行情日期',

    //当天的
    private String dwjz;   //  '单位净值',
    private String rzdf;   //  '日跌涨幅',

    // 今天取昨天的
    private String syl_z;   //  '近一周跌涨幅',
    private String syl_y;   //  '近一个月跌涨幅',
    private String syl_3y;   //  '近三个月跌涨幅',
    private String syl_6y;   //  '近六个月跌涨幅',
    private String syl_1n;   //  '近一年跌涨幅',
    private String syl_2n;   //  '近两年跌涨幅',
    private String syl_3n;   //  '近三年跌涨幅',
    private String syl_jn;   //  '今年以来跌涨幅',
    private String syl_ln;   //  '成立以来跌涨幅',

    //    //每个季度给一次
    //    private String stock_per;//股票占配比
    //    private String bank_per;//银行占配比
    //    private String bond_per;// 债权占配比
    //    private String total_assets;//总资产

    /**
     * 获取行情日期
     *
     * @return 行情日期
     */

    public String getHqrq() {
        if (!StringUtils.isEmpty(hqrq) && hqrq.contains("*")) {
            hqrq = hqrq.replace("*", "").trim();
        }
        return hqrq;

    }

    public static List<FundNetDayInfo> toBean(FundValueEntity fundValueEntity) {
        if (null == fundValueEntity || CollectionUtils.isEmpty(fundValueEntity.getDatas()))
            return Collections.emptyList();
        List<FundNetDayInfo> fundNetDayInfos = new ArrayList<>();
        String code = fundValueEntity.getCode();
        switch (fundValueEntity.getTitle()) {
//            case Key.KEY_ZP2JZ:
//                fundValueEntity.getDatas().forEach(map -> {
//                    String k = Key.BODY;
//                    String v = map.get(Key.BODY);
//                    String[] vs = v.split(" ");
//                    if (vs == null || vs.length < 3)
//                        return;
//                    FundNetDayInfo fundNetDayInfo = new FundNetDayInfo();
//                    fundNetDayInfo.setFund_code(code);
//                    fundNetDayInfo.setUpdate_time(LocalDateTime.now().format(Util.FORMATTER));
//                    fundNetDayInfos.add(fundNetDayInfo);
//                    if (k.equals(Key.BODY) && vs.length > 4 && vs[0].length() > 9) {
//                        fundNetDayInfo.setHqrq(vs[0]);
//                        fundNetDayInfo.setStock_per(vs[1]);
//                        fundNetDayInfo.setBank_per(vs[3]);
//                        fundNetDayInfo.setBond_per(vs[2]);
//                        fundNetDayInfo.setTotal_assets(vs[4]);
//                    }
//                });
//                break;
            case Key.KEY_LLJZ:
                fundValueEntity.getDatas().forEach(data -> {
                    data.forEach((k, v) -> {
                        if (Key.TITLE.equals(k) || k.contains(Key.HEAD))
                            return;
                        String[] vs = v.split(" ");
                        if (vs == null || vs.length < 3)
                            return;
                        FundNetDayInfo fundNetDayInfo = new FundNetDayInfo();
                        fundNetDayInfo.setFund_code(code);
                        fundNetDayInfo.setUpdate_time(LocalDateTime.now().format(Util.FORMATTER));
                        fundNetDayInfos.add(fundNetDayInfo);
                        if (vs.length > 3 && vs[0].length() > 9) {
                            fundNetDayInfos.add(fundNetDayInfo);
                            if (vs.length == 5) {
                                fundNetDayInfo.setHqrq(vs[0]);//净值日期
                                fundNetDayInfo.setDwjz(vs[1]);//万份收益
                                //                        fundNetDayInfo.setSyl_jn(vs[2]);//累计净值
                                fundNetDayInfo.setRzdf(vs[2]);//七日年化收益
                            } else {
                                fundNetDayInfo.setHqrq(vs[0]);//净值日期
                                fundNetDayInfo.setDwjz(vs[1]);//单位净值
                                //                        fundNetDayInfo.setSyl_jn(vs[2]);//累计净值
                                fundNetDayInfo.setRzdf(vs[3]);//日增长率
                            }

                        }
                    });
                });
                break;
            case Key.KEY_JZZF:
                fundValueEntity.getDatas().forEach(data -> {
                    data.forEach((k, v) -> {
                        if (Key.TITLE.equals(k) || k.contains(Key.HEAD))
                            return;
                        String[] vs = v.split(" ");
                        if (vs == null || vs.length < 3)
                            return;
                        FundNetDayInfo fundNetDayInfo = new FundNetDayInfo();
                        fundNetDayInfo.setFund_code(code);
                        fundNetDayInfo.setUpdate_time(LocalDateTime.now().format(Util.FORMATTER));
                        fundNetDayInfos.add(fundNetDayInfo);
                        if (vs.length > 9) {
                            //今年来 近1周 近1月 近3月 近6月 近1年 近2年 近3年 近5年 成立来
                            fundNetDayInfo.setHqrq(k);//截止日期
                            fundNetDayInfo.setSyl_jn(vs[0]);//"今年来"
                            fundNetDayInfo.setSyl_z(vs[1]);//"近一周"
                            fundNetDayInfo.setSyl_y(vs[2]);//"近一月"
                            fundNetDayInfo.setSyl_3y(vs[3]);//"近三月"
                            fundNetDayInfo.setSyl_6y(vs[4]);//"近六月"
                            fundNetDayInfo.setSyl_1n(vs[5]);//"近一年"
                            fundNetDayInfo.setSyl_2n(vs[6]);//"近二年"
                            fundNetDayInfo.setSyl_3n(vs[7]);//"近三年"
                            fundNetDayInfo.setSyl_ln(vs[9]);//"成立来"
                        }
                    });
                });
                break;
            default:
                log.warn(fundValueEntity.toString());
        }

        return fundNetDayInfos;
    }
}
