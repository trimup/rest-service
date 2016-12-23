package com.lihe.persistence;

import com.lihe.entity.HsOrderInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by trimup on 2016/9/5.
 */
public interface HSOrderMapper {

    @Select("select * from  lh_hs_order_info where user_id =#{user_tid} order  by  apply_time desc ")
    public List<HsOrderInfo> QueryHsOrder(@Param("user_id") Integer user_id);
}
