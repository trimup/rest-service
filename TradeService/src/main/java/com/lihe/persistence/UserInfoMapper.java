package com.lihe.persistence;

import com.lihe.entity.UserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by trimup on 2016/7/21.
 */
public interface UserInfoMapper {


    /**
     * 查询 user_info 下一个自增ID
     *
     * @return
     */
    @Select("SELECT AUTO_INCREMENT FROM information_schema.`TABLES` WHERE TABLE_SCHEMA='51qed' AND TABLE_NAME='lh_app_user_info'")
    Integer queryUserInfoMaxId();


    /**
     * 根据手机号查询用户信息
     */
    @Select("select * from lh_app_user_info where telephone=#{telephone} ")
    UserInfo findUserBytelephone(@Param("telephone") String telephone);

    /**
     * 根据用户iD查询用户信息
     */
    @Select("select * from lh_app_user_info where id=#{id} ")
    UserInfo findUserById(@Param("id") Integer id);

    @Select("select * from  lh_app_user_info ")
    List<UserInfo>  findAllUser();
}
