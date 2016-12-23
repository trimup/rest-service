/**
 * @Version 1.0.0
 * Copyright (c) 2016上海相诚金融-版权所有
 */
package com.lihe.persistence;

import com.lihe.entity.HSOrderPurchase;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Class HSOrderPurchaseMapper
 * @Description
 * @Author 张超超
 * @Date 2016/9/5 18:36
 */
public interface HSOrderPurchaseMapper {



    @Select({"SELECT * FROM `lh_hs_order_purchase` where user_tid =#{user_tid} and " +
            "fund_code =#{fund_code}  and trade_status in (1,2,9,5,3) order by  create_time desc limit 1"})
    HSOrderPurchase findHsOrderPurchaseByUserAndFund(@Param("user_tid") int user_tid,@Param("fund_code") String fund_code);



}
