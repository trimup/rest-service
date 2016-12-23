package com.lihe.persistence;

import com.lihe.entity.OrderProductInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by trimup on 2016/9/7.
 */
public interface OrderProductMapper {


    @Insert("insert into lh_order_product(user_tid,product_code)  values (#{user_tid},#{product_code})")
    public void insertLhOrderProdcut(@Param("user_tid")Integer  user_tid,@Param("product_code") String product_code);

    @Select("select count(*) from lh_order_product where user_tid=#{user_tid} and product_code =#{product_code}")
    public Integer  queryLhOrderProduct(@Param("user_tid")Integer  user_tid,@Param("product_code")String product_code);

    @Select("select * from  lh_order_product where user_tid=#{user_tid} order by create_time desc")
    public List<OrderProductInfo>  queryOrderProductByUser(@Param("user_tid") Integer user_tid);

}
