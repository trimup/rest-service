package com.lihe.service;

import com.lihe.AppConfig;
import com.lihe.common.Constant;
import com.lihe.common.Msg;
import com.lihe.entity.SmsLogInfo;
import com.lihe.entity.VerifySmsInfo;
import com.lihe.event.register.RequestVeriCodeEvent;
import com.lihe.persistence.UserInfoMapper;
import com.lihe.persistence.VerifySmsCodeMapper;
import com.lihe.proxy.SMSProxy;
import com.lihe.until.RandomStringUntil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * Created by trimup on 2016/7/25.
 */
@Service
public class VerifyCodeService {

    private static final Logger L = LoggerFactory.getLogger(VerifyCodeService.class);

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private VerifySmsCodeMapper verifySmsCodeMapper;

    @Autowired
    private SMSProxy smsProxy;

    @Autowired
    private AppConfig appConfig;



    /**
     * 获取验证码
     * @param event
     * @return
     */
    public Msg getVerifyCode(RequestVeriCodeEvent event){
        Msg msg =new Msg();

        Integer userNum = userInfoMapper.existUserBytelephone(event.getTelephone());
        if(event.getVeri_type()==1 && userNum != 0) {
            msg.setCode(Msg.FAIL);
            msg.setMsg("您已是注册用户,请直接登录");
            return msg;
        }
        if(event.getVeri_type()!=1 && userNum ==0){
            msg.setCode(Msg.FAIL);
            msg.setMsg("您还未注册,请先注册");
            return msg;
        }
        msg=createVerifyCode(
                event.getTelephone(),appConfig.getVERIFY_SMS_MODLE(),event.getVeri_type());
        return  msg;

    }


    /**
     * 生成验证码，并根据短信模版给客户发送验证码短信
     *
     * @param telephone    手机号码
     * @param contentModel 短信模版
     *                     for example 亲爱的用户，您好!您的注册验证码是:%s
     *                     为了您的账户安全，请勿将验证码转发他人！(有效期30分钟)
     * @return
     */
    @Transactional
    public Msg createVerifyCode(String telephone, String contentModel[], Integer code_type) {
        Msg msg = new Msg();
        //查询该用户在当天申请验证码短信的条数
        Integer smsCount = verifySmsCodeMapper.countSmsLog(telephone);
        L.info("create_code=>" + telephone + ":" + smsCount);
        //如果该当天发送短信的条数超过或者等于5条 就不再发送短信
        if (smsCount >= appConfig.getSMS_MAX()) {//TODO 修改为配置文件20160604
            msg.setCode(Msg.FAIL);
            msg.setMsg("该用户发送短信已超过当天限制");
            return msg;
        }
        // 查询 该用户是否申请过验证码
        Integer verifyCount = verifySmsCodeMapper.existCodeByTelAndType(telephone,code_type);
        VerifySmsInfo verifyInfo = new VerifySmsInfo();
        //如果没有 申请
        if (verifyCount == 0) {
            //生成新的验证码
            String sysCode = RandomStringUntil.generateNumber(4);
            verifyInfo.setTelephone(telephone);
            //            String encode = Base64.encode(sysCode.getBytes());
            verifyInfo.setSms_verify_code(encode(telephone, sysCode));//TODO 校验短信验证码接口是否有漏洞,区分APP/PC接口漏洞
            //            L.info("Code=>" + sysCode + ":" + encode);
            verifyInfo.setCode_type(code_type);
            //插入到数据库中记录
            verifySmsCodeMapper.insertVerifySms(verifyInfo);
        } else {//如果曾经有申请过   生成新的验证码去更新 该记录
            String sysCode = RandomStringUntil.generateNumber(4);
            verifyInfo.setTelephone(telephone);
            verifyInfo.setSms_verify_code(encode(telephone, sysCode));
            verifySmsCodeMapper.updateVerfysmsCode(verifyInfo);
        }

        /**
         * 按顺序取出发送模版 1为 注册验证码 2忘记密码验证码 3修改安全密码验证码
         */
        String content =
                String.format(contentModel[code_type-1], decode(verifyInfo.getSms_verify_code()));
        L.info("send regverify msg content is " + telephone + "=>" + content);
        //给用户发送验证码短信
        Msg reply = smsProxy.sendSMSWithChuanglan(telephone,content);
        reply.setData(telephone);
        if (reply.getCode() == 0) {
            //如果发送成功
            SmsLogInfo smsLogInfo =new SmsLogInfo();
            smsLogInfo.setContent(content);
            smsLogInfo.setMobile(telephone);
            smsLogInfo.setReturn_code("");
            //插入发送记录到 sms_log
            verifySmsCodeMapper.insertSmsLog(smsLogInfo);
            msg.setCode(Msg.SUCCESS);
            msg.setMsg("发送成功");
        } else {
            msg.setCode(Msg.FAIL);
            msg.setMsg("发送失败");
        }
        return msg;
    }

    /**
     * 验证用户验证码
     * @param telephone  手机号
     * @param userVerCode 验证码
     * @param code_type   验证码类型
     */
    public Msg vaildVerifyCode(String telephone, Integer code_type, String userVerCode){

        Msg msg =new Msg();

        msg.setData(telephone);
        if (userVerCode == null || "".equals(userVerCode)) {
            msg.setCode(Msg.FAIL);
            msg.setMsg("验证码不能为空");
            return msg;
        }
        VerifySmsInfo verifyInfo = verifySmsCodeMapper.queryCodeByTelephone(telephone,code_type);
        if (verifyInfo == null) {
            msg.setCode(Msg.FAIL);
            msg.setMsg("该用户还未请求验证码");
            return msg;
        }

        if ((new Date().getTime() - verifyInfo.getCreate_time().getTime()) > 1800000) {
            msg.setCode(Msg.FAIL);
            msg.setMsg("验证码超时，请重新获取");
            return msg;
        }

        String sysVerCode = verifyInfo.getSms_verify_code();
        if (StringUtils.isEmpty(sysVerCode)) {
            msg.setCode(Msg.FAIL);
            msg.setMsg("验证码有误,请重新输入");
            return msg;
        }
        //        String decode = new String(Base64.decode(verifyCode.trim()));
        //        L.info("Code=>" + verifyCode + ":" + decode);
        L.info("code=--" + userVerCode + "--");
        if (!decode(sysVerCode).equals(userVerCode)) {//TODO  解码验证码
            msg.setCode(Msg.FAIL);
            msg.setMsg("验证码有误");
            return msg;
        }
        msg.setCode(Msg.SUCCESS);
        msg.setMsg("验证成功");
        return msg;
    }

    /**
     * 解密
     *
     * @param sysCode 加密验证码
     * @return 解密验证码
     */
    private String decode(String sysCode) {
        return sysCode.trim().replace(appConfig.getSMS_SALT(), "").substring(0, 4);
    }

    /**
     * 加密
     *
     * @param telephone 手机
     * @param sysCode   验证码
     * @return 加密验证码
     */
    private String encode(String telephone, String sysCode) {
        return " " + sysCode.substring(0, 2)
                + appConfig.getSMS_SALT() + sysCode.substring(2, sysCode.length())
                + telephone.substring(10) + " ";
    }
}
