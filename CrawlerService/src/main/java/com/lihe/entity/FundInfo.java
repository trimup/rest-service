/*
 * Copyright (c) 2016. lihe-fund All Rights Reserved.
 */

package com.lihe.entity;

import lombok.Data;

import java.util.Date;
import java.util.HashMap;


/**
 * lh_fund_info   利和基金详情表
 * lh_fund_rate_info  基金费率表
 * lh_fund_net_day  每日净值表
 * Created by trimup on 2016/8/25.
 * 基金信息
 */
@Data
public class FundInfo {
    private Integer id;   //  '主键ID',
    private String fund_code;   //  '基金代码',
    private Date create_time;   //  '创建时间',
    private String pu_confir_time = "";          //申购确认时间
    private String redeem_confir_time = "";     //赎回确认时间
    private String fund_manager_rate = "";      //基金管理费
    private String fund_trust_rate = "";        //基金托管费率
    private String sale_service_rate = "";      //销售服务费
    private String setup_time = "";//成立时间
    private String net_asset_value = "";   //   资产规模
    private String fund_management = "";// 基金管理
    private String custodian_bank = "";//托管银行
    private String total_share = "";//    份额规模
    private String invest_scope;//投资范围
    private String invest_target = "";//投资目标
    private Date update_time;  // 更新时间
    private String fund_manager_logo = "";   //基金经理头像
    private String fund_manager_name = "";    //基金经理名字
    private String fund_manager_introduce = "";  //基金经理介绍

    /**
     * 基金全称 广发天天红发起式货币市场基金 基金简称 广发天天红货币B
     * 基金代码 002183（前端） 基金类型 货币型
     * 发行日期 成立日期/规模 2015年12月15日 / --
     * 资产规模 164.68亿元（截止至：2016年06月30日） 份额规模 164.6847亿份（截止至：2016年06月30日）
     * 基金管理人 广发基金 基金托管人 广发银行
     * 基金经理人 谭昌杰、任爽 成立来分红 每份累计0.00元（0次）
     * 管理费率 0.27%（每年） 托管费率 0.05%（每年）
     * 销售服务费率 0.01%（每年） 最高认购费率 ---（前端）
     * 最高申购费率 0.00%（前端） 最高赎回费率 0.00%（前端）
     * 业绩比较基准 活期存款利率(税后) 跟踪标的 该基金无跟踪标的
     *
     * @param beanInfos
     * @return 公募基金数据
     */
    public static FundInfo toBean(HashMap<String, String> beanInfos) {
        FundInfo info = new FundInfo();
        info.setFund_code(beanInfos.get(Key.CODE));
        info.setFund_manager_rate(beanInfos.getOrDefault("管理费率", ""));
        info.setFund_trust_rate(beanInfos.getOrDefault("托管费率", ""));
        info.setTotal_share(beanInfos.getOrDefault("份额规模", ""));
        info.setNet_asset_value(beanInfos.getOrDefault("资产规模", ""));
        info.setFund_management(beanInfos.getOrDefault("基金管理人", ""));
        info.setFund_manager_name(beanInfos.getOrDefault("基金经理人", ""));
        info.setCustodian_bank(beanInfos.getOrDefault("基金托管人", ""));
        info.setPu_confir_time(beanInfos.getOrDefault("申购确认日", ""));
        info.setRedeem_confir_time(beanInfos.getOrDefault("赎回确认日", ""));
        info.setSetup_time(beanInfos.getOrDefault("成立日期/规模", "").split("/")[0]);
        info.setInvest_target(beanInfos.getOrDefault("投资目标", ""));
        info.setInvest_scope(beanInfos.getOrDefault("投资范围", ""));
        info.setFund_manager_logo(beanInfos.getOrDefault("img", ""));
        info.setFund_manager_name(beanInfos.getOrDefault("name", ""));
        info.setFund_manager_introduce(beanInfos.getOrDefault("intro", ""));
        info.setSale_service_rate(beanInfos.getOrDefault("销售服务费率", ""));
        info.setUpdate_time(new Date());
        return info;
    }

}
