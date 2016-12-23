/*
 * Copyright (c) 2016. lihe-fund All Rights Reserved.
 */

package com.lihe.service;

import com.lihe.crawler.Util;
import com.lihe.entity.*;
import com.lihe.persistence.FundMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.*;

/**
 * @author leo
 * @version V1.0.0
 * @package com.lihe.service
 * @date 8/26/16
 */
@Slf4j
@Service
public class CrawlerService {

    private final FundMapper fundMapper;

    @Autowired
    public CrawlerService(FundMapper fundMapper) {
        this.fundMapper = fundMapper;
    }

    /**
     * 抓去公募基金数据
     *
     * @param beanInfos 公募基金数据
     */
    @Async
    @Transactional
    public void grabPublicFund(HashMap<String, String> beanInfos) {
        FundInfo fundInfo = FundInfo.toBean(beanInfos);
        if (System.currentTimeMillis() - fundInfo.getUpdate_time().getTime()
            < 60 * 60 * 1000) {//一小时内不在更新
            return;
        }
        FundInfo fund = fundMapper.find(fundInfo.getFund_code());
        if (fund != null) {
            CopyFromNotNullBeanUtilsBean copyFromNotNullBeanUtils =
                new CopyFromNotNullBeanUtilsBean("id", "fund_code");
            try {
                copyFromNotNullBeanUtils.copyProperties(fund, fundInfo);
            } catch (Exception e) {
                log.warn(e.getMessage(), e);
            }
            fund.setUpdate_time(fundInfo.getUpdate_time());
            fundMapper.save(fund);
        } else {
            fundInfo.setCreate_time(new Date());
            fundMapper.create(fundInfo);
        }
    }

    /**
     * 抓去公募基金费率数据
     *
     * @param beanInfos 公募基金数据
     */
    @Transactional
    public void grabPublicFundRate(Map<String, List<String>> beanInfos) {
        Set<FundRateinfo> fundRateinfos = FundRateinfo.toBean(beanInfos);
        int size = fundRateinfos.size();
        long count = fundRateinfos.stream().distinct().count();
        if (size != count) {
            log.warn(">2 NOT");
        }
        fundRateinfos.forEach(fundRateinfo -> {
            List<FundRateinfo> fundRates =
                fundMapper.findRate(fundRateinfo.getFund_code(), fundRateinfo.getRate_type(),
                    fundRateinfo.getAmount_range());
            if (fundRates != null && fundRates.size() > 0) {
                if (fundRates.size() > 1) {
                    log.warn(">2 " + fundRateinfo);
                }
                FundRateinfo target = fundRates.get(0);
                CopyFromNotNullBeanUtilsBean copyFromNotNullBeanUtils =
                    new CopyFromNotNullBeanUtilsBean("id,fund_code,rate_type,amount_range");
                try {
                    copyFromNotNullBeanUtils.copyProperties(target, fundRateinfo);
                } catch (Exception e) {
                    log.warn(e.getMessage(), e);
                }
                target.setUpdate_time(fundRateinfo.getUpdate_time());
                fundMapper.saveRate(target);
            } else {
                fundMapper.createRate(fundRateinfo);
            }
        });
    }



    /**
     * 抓去公募基金资产配置数据
     *
     * @param fundValueEntity 公募基金数据
     */
    @Async
    @Transactional
    public void grabAssets(FundValueEntity fundValueEntity) {
        List<FundAssetAllocationInfo> fundNetDayInfos = FundAssetAllocationInfo.toBean(fundValueEntity);
        fundNetDayInfos.forEach(assetAllocationInfo -> {
            String hqrq = assetAllocationInfo.getHqrq();
            if (null == hqrq||limitTime(hqrq))
                return;
            List<FundAssetAllocationInfo> assetAllocationInfos =
                fundMapper.findAssets(assetAllocationInfo.getFund_code(), hqrq);
            if (assetAllocationInfos != null && assetAllocationInfos.size() > 0) {
                if (assetAllocationInfos.size() > 1) {
                    log.warn(">2" + assetAllocationInfo);
                }
                log.info("D=>" + assetAllocationInfo);
                FundAssetAllocationInfo target = assetAllocationInfos.get(0);
                log.info("S=>" + target);
                CopyFromNotNullBeanUtilsBean copyFromNotNullBeanUtils =
                    new CopyFromNotNullBeanUtilsBean("id,fund_code,hqrq");
                try {
                    copyFromNotNullBeanUtils.copyProperties(target, assetAllocationInfo);
                } catch (Exception e) {
                    log.warn(e.getMessage(), e);
                }
                log.info("U=>" + target);
                target.setUpdate_time(assetAllocationInfo.getUpdate_time());
                fundMapper.saveAssets(target);
            } else {
                assetAllocationInfo.setCreate_time(new Date());
                fundMapper.createAssets(assetAllocationInfo);
            }
        });

    }


    /**
     * 抓去公募基金净值数据
     *
     * @param fundValueEntity 公募基金数据
     */
    @Async
    @Transactional
    public void grabNetFund(FundValueEntity fundValueEntity) {
        List<FundNetDayInfo> fundNetDayInfos = FundNetDayInfo.toBean(fundValueEntity);
        fundNetDayInfos.forEach(fundNetDayInfo -> {
            String hqrq = fundNetDayInfo.getHqrq();
            if (null == hqrq||limitTime(hqrq))
                return;
            List<FundNetDayInfo> fundNetDayInfoList =
                fundMapper.findNetDay(fundNetDayInfo.getFund_code(), hqrq);
            if (fundNetDayInfoList != null && fundNetDayInfoList.size() > 0) {
                if (fundNetDayInfoList.size() > 1) {
                    log.warn(">2" + fundNetDayInfo);
                }
                log.info("D=>" + fundNetDayInfo);
                FundNetDayInfo target = fundNetDayInfoList.get(0);
                log.info("S=>" + target);
                CopyFromNotNullBeanUtilsBean copyFromNotNullBeanUtils =
                    new CopyFromNotNullBeanUtilsBean("id,fund_code,hqrq");
                try {
                    copyFromNotNullBeanUtils.copyProperties(target, fundNetDayInfo);
                } catch (Exception e) {
                    log.warn(e.getMessage(), e);
                }
                log.info("U=>" + target);
                target.setUpdate_time(fundNetDayInfo.getUpdate_time());
                fundMapper.saveNetDay(target);
            } else {
                fundNetDayInfo.setCreate_time(new Date());
                fundMapper.createNetDay(fundNetDayInfo);
            }
        });

    }

    private boolean limitTime(String hqrq) {
        try {
            LocalDate dateTime = LocalDate.parse(hqrq, Util.FORMATTER);
            if (dateTime.plusYears(1).isBefore(LocalDate.now())) {
                return true;
            }
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            return true;
        }
        return false;
    }

    /**
     * 抓去公募基金持仓数据
     *
     * @param beanInfos 公募基金数据
     */
    @Async
    @Transactional
    public void grabMainFund(Map<String, String> beanInfos) {
        List<FundMainPositionsInfo> mainPositionsInfos =
            fundMapper.findMain(beanInfos.getOrDefault(Key.CODE, ""));
        if (!CollectionUtils.isEmpty(mainPositionsInfos))//已经存在不在抓去
            return;
        FundMainPositionsInfo.toBean(beanInfos).forEach(info -> fundMapper.createMain(info));
    }
}
