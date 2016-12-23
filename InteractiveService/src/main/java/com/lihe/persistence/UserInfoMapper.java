package com.lihe.persistence;


import com.lihe.entity.UserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * Created by trimup on 2016/7/21.
 */

public interface UserInfoMapper {

    /**
     * 根据手机号查询用户信息
     */
    @Select("select * from lh_app_user_info where id=#{id} ")
    UserInfo findUserById(@Param("id") Integer id);



    @Select("update lh_app_user_info set risk_level=#{risk_level} where id =#{id} ")
    void updateUserRiskLevel(@Param("id") Integer id,@Param("risk_level")String risk_level);





}
