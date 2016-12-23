package com.lihe.persistence;

import com.lihe.entity.SmsLogInfo;
import com.lihe.entity.VerifySmsInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * Created by trimup on 2016/7/21.
 * 验证码
 */
public interface VerifySmsCodeMapper {

    /**
     * 插入验证码
     * @param verifySmsInfo
     */
    @Insert("insert into lh_verify_sms_code(sms_verify_code,telephone,code_type) " +
            "values(#{sms_verify_code},#{telephone},#{code_type})")
    public void insertVerifySms(VerifySmsInfo verifySmsInfo);

    /**
     * 根据手机号查询 验证码
     * @param telephone
     * @return
     */
    @Select("select * from lh_verify_sms_code where telephone =#{telephone} and code_type=#{code_type}")
    public VerifySmsInfo queryCodeByTelephone(@Param("telephone") String telephone,@Param("code_type") Integer code_type);

    /**
     * 根据手机号查询 验证码
     * @param telephone
     * @return
     */
    @Select("select count(1) from lh_verify_sms_code where telephone =#{telephone} and code_type=#{code_type}")
    public Integer existCodeByTelAndType(@Param("telephone") String telephone,@Param("code_type") Integer code_type);


    /**
     * 插入短信消息
     */
    @Insert(" insert into lh_smslog(mobile,content,return_code) values(#{mobile},#{content},#{return_code}) ")
    public void insertSmsLog(SmsLogInfo smsLogInfo);


    /**
     * 查询该手机号当天发送短信的数量
     * @param mobile
     * @return
     */
    @Select("select count(*) from lh_smslog where mobile =#{mobile} and date_format(create_time,'%Y-%m-%d')= CURDATE() ")
    public Integer countSmsLog(@Param("mobile") String mobile);

    /**
     * 更新 最新的验证码
     * @param info
     */
    @Update(" update lh_verify_sms_code set sms_verify_code=#{sms_verify_code},create_time=now() where telephone=#{telephone} ")
    public void updateVerfysmsCode(VerifySmsInfo info);

}
