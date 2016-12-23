/**
 * @Version 1.0.0
 * Copyright (c) 2016上海相诚金融-版权所有
 */
package com.lihe.persistence;

import com.lihe.entity.HSUserFundInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Class HSUserFundInfoMapper
 * @Description
 * @Author 张超超
 * @Date 2016/9/9 13:50
 */
public interface HSUserFundInfoMapper {


    @Select("select * from lh_hs_user_fund_info  ")
    List<HSUserFundInfo>  queryAllLhUserFund();

    @Select("select * from  lh_hs_user_fund_info where user_tid=#{user_tid} and  fund_code=#{fund_code}")
    List<HSUserFundInfo> queryFundByUserAndFundCode
            (@Param("user_tid")Integer user_tid,@Param("fund_code")String fund_code);

}
