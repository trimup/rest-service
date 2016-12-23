package com.lihe.persistence;

import com.lihe.entity.ConcernedProdcutInfo;
import com.lihe.event.ConcProductEvent;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by trimup on 2016/9/2.
 */
public interface ConcernedMapper {


    @Insert("insert into lh_concerned_product(pro_code,type,user_tid,status) values(#{fund_code},#{type},#{user_tid},1)")
    public void addConcernedProduct(ConcProductEvent event);


    /**
     * 判断该人是否有关注过该项目
     * @param pro_code
     * @param user_tid
     * @return
     */
    @Select("select count(*) from lh_concerned_product where user_tid=#{user_tid} and pro_code=#{pro_code} and status =1 ")
    public Integer haveConcernedProduct(@Param("pro_code") String pro_code,@Param("user_tid") Integer user_tid);


    @Select("select * from lh_concerned_product where user_tid=#{user_tid} and pro_code=#{pro_code} ")
    public ConcernedProdcutInfo queryUserConcernedProduct(@Param("pro_code") String pro_code,@Param("user_tid") Integer user_tid);

    @Select("select * from lh_concerned_product where type =#{type} and status =1 and user_tid=#{user_tid} ")
    public List<ConcernedProdcutInfo> queryConCerenedProduct(@Param("type") Integer type,@Param("user_tid") Integer user_tid);

    /**
     * 移除关注
     */
    @Update("<script> update lh_concerned_product set status =0 where id in " +
            "<foreach item=\"item\" index=\"index\" " +
            "collection=\"array\" open=\"(\" separator=\",\" close=\")\"> ${item} </foreach> </script>")
    public void removeConcerned(String[] removeid);

    /**
     * 添加关注
     */
    @Update("<script> update lh_concerned_product set status =1 where user_tid =#{user_tid} and  pro_code=#{pro_code} </script>")
    public void concernedProduct(@Param("pro_code") String pro_code,@Param("user_tid") Integer user_tid);

}
