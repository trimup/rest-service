/*
 * Copyright (c) 2016. lihe-fund All Rights Reserved.
 */

package com.lihe.persistence;

import com.lihe.entity.*;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * lh_fund_info   利和基金详情表
 * lh_fund_rate_info  基金费率表
 * lh_fund_net_day  每日净值表
 * lh_fund_asset_allocation 基金资产配置
 *
 * @author leo
 * @version V1.0.0
 * @package com.persistence
 * @date 8/25/16
 */
public interface FundMapper {
    @Update("UPDATE  lh_fund_info SET pu_confir_time=#{pu_confir_time}, redeem_confir_time=#{redeem_confir_time}, fund_manager_rate=#{fund_manager_rate},fund_trust_rate=#{fund_trust_rate},sale_service_rate=#{sale_service_rate},setup_time=#{setup_time},net_asset_value=#{net_asset_value},fund_management=#{fund_management},custodian_bank=#{custodian_bank},total_share=#{total_share},fund_manager_name=#{fund_manager_name},invest_scope=#{invest_scope},invest_target=#{invest_target},fund_manager_logo=#{fund_manager_logo},fund_manager_introduce=#{fund_manager_introduce},update_time=#{update_time} WHERE fund_code=#{fund_code}")
    void save(FundInfo info);

    @Select("SELECT * FROM lh_fund_info WHERE fund_code=#{fund_code}") FundInfo find(
        @Param("fund_code") String fund_code);

    @Insert("INSERT INTO lh_fund_info (fund_code,pu_confir_time, redeem_confir_time,fund_manager_rate,fund_trust_rate,sale_service_rate,setup_time,net_asset_value,fund_management,custodian_bank,total_share,fund_manager_name,create_time,invest_scope,invest_target,fund_manager_logo,fund_manager_introduce) VALUES (#{fund_code},#{pu_confir_time}, #{redeem_confir_time}, #{fund_manager_rate},#{fund_trust_rate},#{sale_service_rate},#{setup_time},#{net_asset_value},#{fund_management},#{custodian_bank},#{total_share},#{fund_manager_name},#{create_time},#{invest_scope},#{invest_target},#{fund_manager_logo},#{fund_manager_introduce})")
    void create(FundInfo info);

    @Select("SELECT * FROM lh_fund_rate_info WHERE fund_code=#{fund_code} AND rate_type=#{rate_type} AND amount_range LIKE '${amount_range}'")
    List<FundRateinfo> findRate(@Param("fund_code") String fund_code,
        @Param("rate_type") int rate_type,
        @Param("amount_range") String amount_range);

    @Update("UPDATE lh_fund_rate_info SET orgiginal_rate=#{orgiginal_rate},preferred_rate=#{preferred_rate},rate_type=#{rate_type},update_time=#{update_time}  WHERE fund_code=#{fund_code} AND rate_type=#{rate_type} AND amount_range=#{amount_range}")
    void saveRate(FundRateinfo fundRate);

    @Insert("INSERT INTO lh_fund_rate_info (fund_code,amount_range,orgiginal_rate,preferred_rate,rate_type,update_time) VALUES (#{fund_code},#{amount_range},#{orgiginal_rate},#{preferred_rate},#{rate_type},#{update_time}) ")
    void createRate(FundRateinfo fundRateinfo);

    @Select("SELECT * FROM lh_fund_net_day WHERE fund_code=#{fund_code} AND hqrq=#{hqrq}")
    List<FundNetDayInfo> findNetDay(@Param("fund_code") String fund_code,
        @Param("hqrq") String hqrq);

    @Update("UPDATE  lh_fund_net_day SET dwjz=#{dwjz},rzdf=#{rzdf},syl_z=#{syl_z},syl_y=#{syl_y},syl_3y=#{syl_3y},syl_6y=#{syl_6y},syl_1n=#{syl_1n},syl_2n=#{syl_2n},syl_3n=#{syl_3n},syl_jn=#{syl_jn},syl_ln=#{syl_ln},update_time=#{update_time} WHERE  fund_code=#{fund_code} AND hqrq=#{hqrq}")
    void saveNetDay(FundNetDayInfo fundNetDayInfo);

    @Insert("INSERT INTO lh_fund_net_day (fund_code,create_time,hqrq,dwjz,rzdf,syl_z,syl_y,syl_3y,syl_6y,syl_1n,syl_2n,syl_3n,syl_jn,syl_ln,update_time) VALUES (#{fund_code},#{create_time},#{hqrq},#{dwjz},#{rzdf},#{syl_z},#{syl_y},#{syl_3y},#{syl_6y},#{syl_1n},#{syl_2n},#{syl_3n},#{syl_jn},#{syl_ln},#{update_time}) ")
    void createNetDay(FundNetDayInfo fundNetDayInfo);

    @Insert("INSERT INTO lh_fund_main_positions (fund_code,create_time,name,code,capital,accounting_ratio) VALUES (#{fund_code},#{create_time},#{name},#{code},#{capital},#{accounting_ratio}) ")
    void createMain(FundMainPositionsInfo fundMainPositionsInfo);

    @Select("SELECT * FROM lh_fund_main_positions WHERE fund_code=#{fund_code}")
    List<FundMainPositionsInfo> findMain(@Param("fund_code") String fund_code);

    @Select("SELECT * FROM lh_fund_asset_allocation WHERE fund_code=#{fund_code} AND hqrq=#{hqrq}")
    List<FundAssetAllocationInfo> findAssets(@Param("fund_code") String fund_code,
        @Param("hqrq") String hqrq);

    @Update("UPDATE  lh_fund_asset_allocation SET stock_per=#{stock_per},bank_per=#{bank_per},bond_per=#{bond_per},total_assets=#{total_assets},update_time=#{update_time} WHERE  fund_code=#{fund_code} AND hqrq=#{hqrq}")
    void saveAssets(FundAssetAllocationInfo assetAllocationInfo);

    @Insert("INSERT INTO lh_fund_asset_allocation (fund_code,create_time,hqrq,stock_per,bank_per,bond_per,total_assets,update_time) VALUES (#{fund_code},#{create_time},#{hqrq},#{stock_per},#{bank_per},#{bond_per},#{total_assets},#{update_time}) ")
    void createAssets(FundAssetAllocationInfo assetAllocationInfo);
}
