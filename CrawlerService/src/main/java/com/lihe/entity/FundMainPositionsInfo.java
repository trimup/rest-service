/*
 * Copyright (c) 2016. lihe-fund All Rights Reserved.
 */

package com.lihe.entity;

import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by trimup on 2016/8/26.
 */
@Data
public class FundMainPositionsInfo {

    private Integer id;  // 主键ID',
    private String fund_code;  // 基金代码',
    private String name;  // '名称',
    private String code;  // '代码',
    private String capital;  // '市值',
    private String accounting_ratio;  // '占配比',
    private String create_time;  //  '创建时间',

    public static List<FundMainPositionsInfo> toBean(Map<String, String> beanInfos) {
        if (CollectionUtils.isEmpty(beanInfos) || !beanInfos.containsKey(Key.BODY) || !beanInfos
            .containsKey(Key.CODE))
            return Collections.emptyList();
        String[] titles = beanInfos.get(Key.HEAD).split(" ");
        String[] datas = beanInfos.get(Key.BODY).split(" ");
        int step = titles.length;
        if (null == titles || step == 0)
            return Collections.emptyList();
        String code = beanInfos.get(Key.CODE);
        String time = beanInfos.get(Key.TIME);
        //序号	股票代码	股票名称	最新价	涨跌幅	相关资讯	占净值比例	持股数（万股）持仓市值（万元）
        List<FundMainPositionsInfo> infos = new ArrayList<>();
        int l = datas.length;
        for (int index = 0; index < l; index += step) {
            FundMainPositionsInfo info = new FundMainPositionsInfo();
            info.setFund_code(code);
            infos.add(info);
            info.setCreate_time(time);
            info.setCode(datas[index + 1]);
            info.setName(datas[index + 2]);
            info.setCapital(datas[index+ step - 1]);
            info.setAccounting_ratio(datas[index + step - 3]);
        }
        return infos;
    }
}
