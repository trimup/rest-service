package com.lihe.persistence;

import com.lihe.entity.FaRelationInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by trimup on 2016/8/2.
 * 理财师推荐关系查询
 */
public interface FaRelationMapper {


    /**
     * 添加理财师推荐关系
     */
    @Insert("insert into lh_app_fa_relation(financial_advisor_id,lh_app_user_id,width) " +
            "values(#{financial_advisor_id},#{lh_app_user_id},#{width}) ")
    public void insertFaRealtion(FaRelationInfo faRelationInfo);

    /**
     *
     */
    @Select("select * from lh_app_fa_relation where lh_app_user_id =#{user_id} ")
    public FaRelationInfo queryFaRelationByUserId(@Param("user_id")Integer user_id);

    /**
     *
     */
    @Select("select count(*) from lh_app_fa_relation where lh_app_user_id =#{user_id} ")
    public Integer countFaRelationByUserId(@Param("user_id")Integer user_id);

}
