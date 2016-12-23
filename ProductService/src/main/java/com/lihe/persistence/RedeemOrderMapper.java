package com.lihe.persistence;

import com.lihe.entity.HSOrderPurchase;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

/**
 * Created by trimup on 2016/10/10.
 */
public interface RedeemOrderMapper {

    @Select({"SELECT create_time FROM `lh_hs_order_redeem` where user_tid =#{user_tid} and " +
            "fund_code =#{fund_code}  and trade_status in (1,2,9,5,3) order by  create_time desc limit 1"})
    Date findHsOrderRedeemDate(@Param("user_tid") int user_tid, @Param("fund_code") String fund_code);


}
