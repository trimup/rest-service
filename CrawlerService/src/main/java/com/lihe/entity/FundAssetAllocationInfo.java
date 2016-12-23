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
 * 基金资产配置
 * lh_fund_asset_allocation
 *
 * @author leo
 * @version V1.0.0
 * @package com.lihe.entity
 * @date 23/12/2016
 */
@Slf4j
@Data
public class FundAssetAllocationInfo {

    //每个季度给一次
    private String stock_per;//股票占配比
    private String bank_per;//银行占配比
    private String bond_per;// 债权占配比
    private String total_assets;//总资产

    private Long id; //'主键ID',
    private String fund_code;// '基金代码',
    private String hqrq;// '行情日期',
    private Date create_time;// '创建时间',
    private String update_time;//'更新时间',


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

    public static List<FundAssetAllocationInfo> toBean(FundValueEntity fundValueEntity) {
        if (null == fundValueEntity || CollectionUtils.isEmpty(fundValueEntity.getDatas()))
            return Collections.emptyList();
        List<FundAssetAllocationInfo> assetAllocationInfos = new ArrayList<>();
        String code = fundValueEntity.getCode();
        switch (fundValueEntity.getTitle()) {
            case Key.KEY_ZP2JZ:
                fundValueEntity.getDatas().forEach(map -> {
                    String k = Key.BODY;
                    String v = map.get(Key.BODY);
                    String[] vs = v.split(" ");
                    if (vs == null || vs.length < 3)
                        return;
                    FundAssetAllocationInfo assetAllocationInfo = new FundAssetAllocationInfo();
                    assetAllocationInfo.setFund_code(code);
                    assetAllocationInfo.setUpdate_time(LocalDateTime.now().format(Util.FORMATTER));
                    assetAllocationInfos.add(assetAllocationInfo);
                    if (k.equals(Key.BODY) && vs.length > 4 && vs[0].length() > 9) {
                        assetAllocationInfo.setHqrq(vs[0]);
                        assetAllocationInfo.setStock_per(vs[1]);
                        assetAllocationInfo.setBank_per(vs[3]);
                        assetAllocationInfo.setBond_per(vs[2]);
                        assetAllocationInfo.setTotal_assets(vs[4]);
                    }
                });
                break;
            default:
                log.warn(fundValueEntity.toString());
        }

        return assetAllocationInfos;
    }
}
