package com.lihe.persistence;

import com.lihe.entity.productDetail.SunNetValInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by trimup on 2016/9/13.
 * 基金净值
 */
public interface SunNetValMapper {


    /**
     * 查询 某一个项目的  每日单位净值
     * @param product_id
     * @return
     */
    @Select("select * from lh_jz where product_id =#{product_id} order by riqi desc")
    public List<SunNetValInfo>  queryNetVal(@Param("product_id") Integer product_id);
}
