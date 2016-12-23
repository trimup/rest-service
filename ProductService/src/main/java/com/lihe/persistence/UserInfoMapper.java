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
     * 根据手机号查询是否存在该用户 1为存在 0为不存在
     * @param telephone
     * @return
     */
    @Select("select count(1) from lh_app_user_info where telephone =#{telephone}")
    public int existUserBytelephone(@Param("telephone") String telephone);


    @Insert("insert into lh_app_user_info(user_name,real_name,telephone,referrer_id," +
            "login_password,status,register_type,register_channel) values(#{user_name},#{real_name}," +
            "#{telephone},#{referrer_id}," +
            "#{login_password},#{status},#{register_type},#{register_channel})")
    public void insertUser(UserInfo userInfo);


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


    /**
     * 更新用户登录错误时间
     *
     * @param telephone 登录帐号 电话号码
     */
    @Update({"update lh_app_user_info set error_time=now() where telephone=#{telephone}"})
    public void updateUserInfoErrorTime(@Param("telephone") String telephone);

    /**
     * 更新用户登录错误次数
     *
     * @param telephone 登录帐号 电话号码
     * @param errortips 错误次数
     */
    @Update({
            "update lh_app_user_info set errortips=#{errortips},error_time=now() where telephone=#{telephone}"})
    public void updateUserInfoErrortips(@Param("telephone") String telephone,
                                        @Param("errortips") Integer errortips);


    /**
     * 冻结该帐号
     */
    @Update({"update lh_app_user_info set status=1,error_time=now() where telephone=#{telephone}"})
    public void frozenUser(@Param("telephone") String telephone);


    /**
     * 查询 系统中是否有重复的token
     *
     * @param token
     * @return
     */

    @Select("SELECT count(1) from lh_app_user_info where token =#{token} ")
    public Integer getTokenCount(String token);

    /**
     * 更新用户 token值
     *
     * @param telephone
     * @param token
     */
    @Update({"update lh_app_user_info set token=#{token} where telephone=#{telephone}"})
    public void updateUserToken(@Param("telephone") String telephone, @Param("token") String token);

    /**
     * 更新用户 密码
     *
     * @param login_password
     * @param telephone
     */
    @Update({"update lh_app_user_info set login_password=#{login_password} where telephone=#{telephone}"})
    public void updateUserLoginPwd(@Param("login_password") String login_password, @Param("telephone") String telephone);


    /**
     * 基金开户更新用户 real_name,identity_id
     *
     * @param
     * @param
     */
    @Update({"update lh_app_user_info set identity_card=#{identity_card} " +
            " ,real_name =#{real_name},trade_password =#{trade_password},client_id=#{client_id} where  id =#{id}"})
    public void updateUserByReFund(@Param("identity_card") String identity_card,
                                   @Param("real_name") String real_name,
                                   @Param("trade_password") String trade_password,
                                   @Param("id") Integer id, @Param("client_id") String client_id);


    /**
     * 更新用户 real_name,identity_id
     *
     * @param
     * @param
     */
    @Update({"update lh_app_user_info set trade_password =#{trade_password} where  id =#{id}"})
    public void updateUserTradePwd(@Param("trade_password") String trade_password,
                                   @Param("id") Integer id);

}
