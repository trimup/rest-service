package com.lihe.service;

import com.lihe.AppConfig;
import com.lihe.HundSunReply.FixTradePwdReply;
import com.lihe.HundSunReply.ResetTradePwdReply;
import com.lihe.common.Constant;
import com.lihe.common.Msg;
import com.lihe.entity.BankAccountInfo;
import com.lihe.entity.FaRelationInfo;
import com.lihe.entity.UserInfo;
import com.lihe.event.login.*;
import com.lihe.persistence.*;
import com.lihe.pojo.EmerContactPojo;
import com.lihe.pojo.UserSafeCenterPojo;
import com.lihe.until.Base64;
import com.lihe.until.DESUntil;
import com.lihe.until.MD5;
import com.lihe.until.RandomStringUntil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.security.InvalidParameterException;
import java.util.List;

/**
 * Created by trimup on 2016/7/22.
 */
@Service
public class LoginService {

    private static final Logger L = LoggerFactory.getLogger(LoginService.class);


    @Autowired private AppConfig config;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private VerifyCodeService verifyCodeService;

    @Autowired
    private KeyValueMapper keyValueMapper;


    @Autowired
    private UserBankAccountMapper userBankAccountMapper;


    @Autowired
    private FaRelationMapper faRelationMapper;

    @Autowired
    private LHUserInfoMapper lhUserInfoMapper;

    @Transactional
    public Msg checkUserLogin(String telephone, String pwd) throws Exception{

        Msg msg  =new Msg();
        UserInfo info = userInfoMapper.findUserBytelephone(telephone);
        //检查该用户是否有注册
        if (info == null) {
            msg.setCode(Msg.FAIL);
            msg.setMsg("该手机号还未注册");//输入的手机号或密码错误
            return msg;
        }
        //检查该用户的错误登录次数
        if (info.getErrortips() > 4 || "1".equals(info.getStatus())) {
            msg.setCode(Msg.FAIL);
            msg.setMsg("该账户已被冻结，请半小时后再操作或者致电客服热线400-882-6823");
            //该账户异常,请过半小时后再操作或者致电客服热线
            //msg.setMsg("该账户登陆异常,请过半小时后再操作或者致电客服热线400-882-6823");
            //更新错误时间,说明用户尝试登陆过
            userInfoMapper.updateUserInfoErrorTime(telephone);
//            if ("5".equals(info.getSource_type())) {
//                userInfoMapper.unfrozenUser(telephone);
//            }
            return msg;
        }
        //数据库的密码验证
        boolean isFit = MD5.sign(telephone+MD5.sign(
                DESUntil.decode(config.getDES_KEY(),
                        Base64.decode(info.getLogin_password())),
                "utf-8"),
                "utf-8").equals(pwd);
        //检查 用户名密码 如果用户名密码 不匹配 数据库中记录的错误登录次数+1
        if (!isFit) {
            Integer errortips = info.getErrortips() + 1;
            userInfoMapper.updateUserInfoErrortips(telephone, errortips);
            //如果errortips 次数大于等于5  冻结该帐号
            if (errortips > 4) {
                userInfoMapper.frozenUser(telephone);
                msg.setCode(Msg.FAIL);
                msg.setMsg("手机号或密码错误超过5次，账户已被冻结,请致电客服热线400-882-6823");
                return msg;
            }
            msg.setCode(Msg.FAIL);
            msg.setMsg("输入的手机号或密码错误");
            return msg;
        }
        //检查通过 之后 生成token
        String token = createAppTaken();
        //更新token进数据库
        userInfoMapper.updateUserToken(telephone, token);
        //充置错误次数为0
        userInfoMapper.updateUserInfoErrortips(telephone, 0);
        msg.setCode(Msg.SUCCESS);
        msg.setMsg("登录成功");
        //用户理财师
        UserLoginReturn event = new UserLoginReturn();
        FaRelationInfo  faRelationInfo =  faRelationMapper.queryFaRelationByUserId(info.getId());
        if(faRelationInfo!=null){
            event.setFp_id(faRelationInfo.getFinancial_advisor_id());
            event.setFp_url(lhUserInfoMapper.findLHUserLogoById(faRelationInfo.getFinancial_advisor_id()));
            event.setHave_fp(true);
        }
        /**
         * 筛选出app需要的数据
         */
        event.setToken(token);
        event.setId(info.getId());
        if (info.getReal_name().equals(info.getTelephone())) {
            event.setReal_name("");
        } else {
            event.setReal_name(info.getReal_name());
        }
        event.setUser_name(info.getUser_name());
        event.setIdentity_card(info.getIdentity_card());
        event.setTelephone(info.getTelephone());
        if(info.getTrade_password()!=null)
            event.setBind_bank_card(true);
        if(info.getRisk_level()!=null)
            event.setHave_risk(true);
        msg.setData(event);
        return msg;

    }

    public Msg getUserInfo(Integer user_tid) {

        Msg msg  =new Msg();
        UserInfo info = userInfoMapper.findUserById(user_tid);
        UserLoginReturn event = new UserLoginReturn();
        FaRelationInfo  faRelationInfo =  faRelationMapper.queryFaRelationByUserId(info.getId());
        if(faRelationInfo!=null){
            event.setFp_id(faRelationInfo.getFinancial_advisor_id());
            event.setFp_url(lhUserInfoMapper.findLHUserLogoById(faRelationInfo.getFinancial_advisor_id()));
            event.setHave_fp(true);
        }
        /**
         * 筛选出app需要的数据
         */
        event.setId(info.getId());
        if (info.getReal_name().equals(info.getTelephone())) {
            event.setReal_name("");
        } else {
            event.setReal_name(info.getReal_name());
        }
        event.setUser_name(info.getUser_name());
        event.setIdentity_card(info.getIdentity_card());
        event.setTelephone(info.getTelephone());
        event.setToken(info.getToken());
        if(info.getTrade_password()!=null)
            event.setBind_bank_card(true);
        if(info.getRisk_level()!=null)
            event.setHave_risk(true);
        msg.setData(event);
        msg.setCode(Constant.SUCCESS);
        msg.setMsg(Constant.SUCCESS_MSG);
        return msg;
    }


    /**
     * 生成时候检查是否有重复的token 如有重复的token再重新生成token
     * 直到生成不重复token
     *
     * @return
     */
    public String createAppTaken() {
        String token = "";
        while (true) {
            token = RandomStringUntil.generateString(18);
            if (userInfoMapper.getTokenCount(token) == 0) {
                break;
            }
        }
        return token;
    }


    public boolean checkUserPwd(CheckUserPwdEvent event) throws Exception{
        UserInfo info = userInfoMapper.findUserById(event.getUser_tid());
        //检查该用户是否有注册
        if (info == null) {
            return false;
        }

        //验证旧密码
        boolean isRight=MD5.sign(info.getTelephone()+MD5.sign(
                DESUntil.decode(config.getDES_KEY(),
                        Base64.decode(info.getLogin_password())),
                "utf-8"),
                "utf-8").equals(event.getOldPwd());

        return  isRight;

    }




    /**
     * 修改用户密码 根据老密码
     * @param event
     * @return
     */
    @Transactional
    public Msg fixUserPwd(FixUserPwdEvent event) throws Exception{
        Msg  msg =new Msg();
        UserInfo info = userInfoMapper.findUserBytelephone(event.getTelephone());
        //检查该用户是否有注册
        if (info == null) {
            msg.setCode(Msg.FAIL);
            msg.setMsg("该用户不存在");//输入的手机号或密码错误
            return msg;
        }

        //验证旧密码
        boolean isRight=MD5.sign(info.getTelephone()+MD5.sign(
                DESUntil.decode(config.getDES_KEY(),
                        Base64.decode(info.getLogin_password())),
                "utf-8"),
                "utf-8").equals(event.getOldPwd());
        //如果验证错误 返回失败
        if(!isRight)
            return  new Msg(Constant.FAIL,"原密码错误");

        //如果验证成功 修改用户密码
        userInfoMapper.updateUserLoginPwd(Base64.encode(
                DESUntil.encode(config.getDES_KEY(),event.getNewPwd())),event.getTelephone());

         msg.setCode(Constant.SUCCESS);
        msg.setMsg(Constant.SUCCESS_MSG);
        return  msg;
    }

    /**
     * 重置用户密码
     */
    @Transactional
    public Msg resetUserPwd(ResetUserPwdEvent event) throws Exception{
        Msg  msg =new Msg();
        UserInfo info = userInfoMapper.findUserBytelephone(event.getTelephone());
        //检查该用户是否有注册
        if (info == null) {
            msg.setCode(Msg.FAIL);
            msg.setMsg("该用户不存在");//输入的手机号或密码错误
            return msg;
        }

        msg=verifyCodeService.vaildVerifyCode(event.getTelephone(),2,event.getVerifyCode());
        if(msg.getCode()==Constant.SUCCESS)
        {
            //如果验证成功 修改用户密码
            userInfoMapper.updateUserLoginPwd(Base64.encode(
                    DESUntil.encode(config.getDES_KEY(),event.getNewPwd())),event.getTelephone());
            msg.setCode(Constant.SUCCESS);
            msg.setMsg("重置密码成功");
        }else{
            msg.setMsg("验证码输入有误");
        }
        return  msg;
    }


    /**
     * 修改用户交易密码
     * @param event
     * @return
     */
    @Transactional
    public Msg fixTradePwd(FixTradePwdEvent event) throws Exception{
        Msg  msg =new Msg();
        //查询出用户信息
        UserInfo info = userInfoMapper.findUserById(event.getUser_tid());
        //检查该用户是否有注册
        if (info == null) {
            msg.setCode(Msg.FAIL);
            msg.setMsg("该用户不存在");
            return msg;
        }

        //验证旧密码
        boolean isRight=MD5.sign(
                DESUntil.decode(config.getDES_KEY(),
                        Base64.decode(info.getTrade_password())),
                "utf-8").equals(event.getOldPwd());
        //如果验证错误 返回失败
        if(!isRight)
            return  new Msg(Constant.FAIL,"原密码错误");

        //如果验证成功 调用接口修改用户的交易密码
        //1 查询出所有改用户银行账户
        List<BankAccountInfo> accountInfos = userBankAccountMapper.queryAccountByUserId(event.getUser_tid());
        if(accountInfos==null||accountInfos.isEmpty())
            return  new Msg(Constant.FAIL,"该用户还未绑定银行卡");

        //将该用户的每张银行卡交易密码修改掉
        for(BankAccountInfo b :accountInfos){
            fixUserAccountPwdByHS(event, b);
        }

        //成功之后更新用户标中该用户的交易密码
        userInfoMapper.updateUserTradePwd(Base64.encode(
                DESUntil.encode(config.getDES_KEY(),event.getNewPwd())),info.getId());
        msg.setCode(Constant.SUCCESS);
        msg.setMsg(Constant.SUCCESS_MSG);
        return  msg;
    }


    /**
     * 重置用户交易密码
     * @param event
     * @return
     */
    @Transactional
    public Msg resetTradePwd(ResetTradePwdEvent event) throws Exception{
        Msg  msg =new Msg();
        //查询出用户信息
        UserInfo info = userInfoMapper.findUserById(event.getUser_tid());
        //检查该用户是否有注册
        if (info == null) {
            msg.setCode(Msg.FAIL);
            msg.setMsg("该用户不存在");
            return msg;
        }
        //验证重置交易密码验证码
        msg=verifyCodeService.vaildVerifyCode(info.getTelephone(),3,event.getVerifyCode());
        //如果验证码有误 马上返回错误
        if (msg.getCode() == 1) {
            return msg;
        }
        //验证身份证
        boolean isRight=info.getIdentity_card().equals(event.getIdentity_card());
        //如果验证错误 返回失败
        if(!isRight)
            return  new Msg(Constant.FAIL,"身份证错误");

        //如果验证成功 调用重置交易密码接口
        //1 查询出所有改用户银行账户
        List<BankAccountInfo> accountInfos = userBankAccountMapper.queryAccountByUserId(event.getUser_tid());
        if(accountInfos==null||accountInfos.isEmpty())
            return  new Msg(Constant.FAIL,"该用户还未绑定银行卡");

        //重置交易密码
        msg =resetTradePwdByHS(accountInfos.get(0),event.getNewPwd());

        if(msg.getCode()==0){ //成功之后更新用户表中该用户的交易密码，
            userBankAccountMapper.updateUserAccountPwd(Base64.encode(
                    DESUntil.encode(config.getDES_KEY(),event.getNewPwd())),event.getUser_tid());
            //还有所有银行卡的交易密码为该密码
            userInfoMapper.updateUserTradePwd(Base64.encode(
                    DESUntil.encode(config.getDES_KEY(),event.getNewPwd())),info.getId());
        }
        return  msg;
    }


    private void fixUserAccountPwdByHS(FixTradePwdEvent event, BankAccountInfo b) throws Exception {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("id_kind_gb",b.getId_kind_gb()); //证件类型
        paramMap.add("id_no",b.getId_no());
        paramMap.add("new_password",event.getNewPwd());
        paramMap.add("password", DESUntil.decode(config.getDES_KEY(),
                Base64.decode(b.getPassword())));
        paramMap.add("trade_acco",b.getTrade_acco());

        RestTemplate restTemplate = new RestTemplate();
        FixTradePwdReply reply   =
                restTemplate.postForObject("http://"+config.getIP_PORT()+"/openapi/fundbusrestful/account/modifytradepassword",
                        paramMap, FixTradePwdReply.class);
        L.info("userId "+b.getUser_tid()+" fix "+b.getBank_account()+" result is "+reply.toString());
        if(reply.getCode().equals(Constant.HS_SUCCESS_CODE))
        {
            //更新该用户的该银行账户的密码 加密
            userBankAccountMapper.updateUserAccountPwd(Base64.encode(
                    DESUntil.encode(config.getDES_KEY(),event.getNewPwd())),b.getId());
        }else {
            throw new InvalidParameterException("修改密码失败");
        }
    }


    /**
     * 重置交易密码
     * @param b
     * @throws Exception
     */
    private Msg resetTradePwdByHS(BankAccountInfo b,String newPwd) throws Exception {
        Msg msg =new Msg();
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("id_kind_gb",b.getId_kind_gb()); //证件类型
        paramMap.add("id_no",b.getId_no());
        paramMap.add("new_password",newPwd);
        RestTemplate restTemplate = new RestTemplate();
        ResetTradePwdReply reply   =
                restTemplate.postForObject("http://"+config.getIP_PORT()+"/openapi/fundbusrestful/account/resettradepassword",
                        paramMap, ResetTradePwdReply.class);
        L.info("userId "+b.getUser_tid()+" reset trade pwd result is "+reply.toString());
        if(reply.getCode().equals(Constant.HS_SUCCESS_CODE))
        {
           msg.setCode(Constant.SUCCESS);
           msg.setMsg("重置成功");
        }else {
            msg.setCode(Constant.FAIL);
            msg.setMsg(reply.getMessage());
        }
        return  msg;
    }


    /**
     * 获取安全中心信息
     * @param event
     * @return
     */
    public Msg  getSafeInfo(QuerySafeInfoEvent event){
        UserInfo userInfo =userInfoMapper.findUserById(event.getUser_tid());
        if(userInfo==null)
            return  new Msg(Constant.FAIL,"该用户不存在");

        Msg msg =new Msg();
        int risk_level =1;
        UserSafeCenterPojo safeCenterPojo =new UserSafeCenterPojo();
        if(userInfo.getEmer_contact_name()!=null){
            EmerContactPojo emerContactPojo=new EmerContactPojo();
            emerContactPojo.setEmer_contact_name(userInfo.getEmer_contact_name());
            emerContactPojo.setEmer_contact_relation(userInfo.getEmer_contact_relation());
            emerContactPojo.setEmer_contact_telephone(userInfo.getEmer_contact_telephone());
            safeCenterPojo.setEmerContactPojo(emerContactPojo);
            safeCenterPojo.setEmerContact(true);
            risk_level=risk_level+1;
        }
        List<BankAccountInfo> bankAccountInfos =userBankAccountMapper.queryAccountByUserId(event.getUser_tid());
        if(!bankAccountInfos.isEmpty()){
            safeCenterPojo.setBank(true);
            safeCenterPojo.setReal_name(userInfo.getReal_name());
            risk_level=risk_level+2;
        }
        safeCenterPojo.setRisk_level(risk_level);
        safeCenterPojo.setTelephone(userInfo.getTelephone());
        msg.setCode(Constant.SUCCESS);
        msg.setData(safeCenterPojo);
        msg.setMsg(Constant.SUCCESS_MSG);
        return  msg;
    }

}
