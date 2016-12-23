/**
 * @Version 1.0.0
 * Copyright (c) 2016上海相诚金融-版权所有
 */
package com.lihe.persistence;

import com.lihe.entity.FundInfo;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @Class FundInfoMapper
 * @Description
 * @Author 张超超
 * @Date 2016/9/14 18:14
 */
public interface FundInfoMapper {

    @Select({"<script>",
            "SELECT * FROM `lh_fund_info` WHERE 1=1 ",
            "<if test='fund_code != null'>",
            "AND `fund_code` = #{fund_code}",
            "</if>",
    "</script>"})
    List<FundInfo> findFundInfoListByParams(Map<String, String> paramMap);
}
