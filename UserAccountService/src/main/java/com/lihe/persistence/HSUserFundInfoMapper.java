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


    @Select({"SELECT * FROM `lh_hs_user_fund_info`",
            "WHERE 1 = 1",
            "AND `user_tid` = #{user_tid}",
            "AND `fund_code` = #{fund_code}",
            "AND `enable_shares` > 0"})
    List<HSUserFundInfo> findUserFundListForRedeem(@Param("user_tid") int userId, @Param("fund_code") String fund_code);

    @Select("select * from lh_hs_user_fund_info where user_tid =#{user_tid} ")
    List<HSUserFundInfo>  queryLhUserFundById(@Param("user_tid") Integer user_tid);

}
