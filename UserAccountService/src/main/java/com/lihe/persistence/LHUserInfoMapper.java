package com.lihe.persistence;

import com.lihe.entity.UserInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by trimup on 2016/8/1.
 * 理财师用户
 */
public interface LHUserInfoMapper {


    @Select("select id from lh_user_info where telephone =#{telephone} ")
    public Integer findLHUserIdByTel(@Param("telephone")String telephone);

    @Select("select user_logo from lh_user_info where id =#{id} ")
    public String findLHUserLogoById(@Param("id")Integer telephone);
}
