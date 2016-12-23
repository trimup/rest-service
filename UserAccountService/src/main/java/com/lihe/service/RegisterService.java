package com.lihe.service;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.lihe.AppConfig;
import com.lihe.HundSunReply.OpenTradeReply;
import com.lihe.HundSunReply.PostmessagesignReply;
import com.lihe.HundSunReply.PremessAgesignReply;
import com.lihe.common.Constant;
import com.lihe.HundSunReply.RgisterFundReply;
import com.lihe.common.Msg;
import com.lihe.entity.*;
import com.lihe.event.register.*;
import com.lihe.persistence.*;
import com.lihe.pojo.RecoUserPojo;
import com.lihe.until.Base64;
import com.lihe.until.DESUntil;
import com.lihe.until.IDCardUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by trimup on 2016/7/21.
 * 注册服务
 */
@Service
public class RegisterService {
    private static final Logger L = LoggerFactory.getLogger(RegisterService.class);
    private static final SimpleDateFormat Format = new SimpleDateFormat("yyyyMMddHHmmss");

    @Autowired
    private AppConfig appConfig;
    //
    @Autowired
    private UserInfoMapper userInfoMapper;

    //验证码
    @Autowired
    private VerifyCodeService vaildVerifyCode;


    //用户银行账户标操作
    @Autowired
    private UserBankAccountMapper userBankAccountMapper;

    @Autowired
    private KeyValueMapper keyValueMapper;

    //用户分享注册表操作
    @Autowired
    private UserShareActivityMapper userShareActivityMapper;


    //利和后台用户表（私募理财经理）操作
    @Autowired
    private LHUserInfoMapper lhUserInfoMapper;

    @Autowired
    private FaRelationMapper faRelationMapper;

    @Autowired
    private HSBankMapper hsBankMapper;




    /**
     * 注册新用户
     * @param userEvent
     * @return
     */
    public Msg  insertNewUser(RegisterUserEvent userEvent) throws Exception{
        //        -------

        Msg msg =new Msg();
        //验证该手机号是否已经注册
        if(userInfoMapper.existUserBytelephone(userEvent.getTelephone())!=0){
            msg.setCode(Constant.FAIL);
            msg.setMsg("该手机号已经注册");
            return  msg;
        }
        //验证验证码是否正确（注册）
         msg = vaildVerifyCode.vaildVerifyCode(userEvent.getTelephone(), 1, userEvent.getVerifyCode());
        //如果验证码有误 马上返回错误
        if (msg.getCode() == 1) {
            return msg;
        }

        UserInfo userInfo = getRegisterUserInfo(userEvent);
        userInfoMapper.insertUser(userInfo);
        if (!Strings.isNullOrEmpty(userEvent.getFaTelephone())) {
            Integer faId = lhUserInfoMapper.findLHUserIdByTel(userEvent.getFaTelephone());
            if (faId != null) {
                //如果该手机号没有对应的理财师相匹配 设为默认值0
                FaRelationInfo rInfo =new FaRelationInfo();
                rInfo.setFinancial_advisor_id(faId);
                rInfo.setLh_app_user_id(userInfo.getId());
                rInfo.setWidth(1); //一级关系
                faRelationMapper.insertFaRealtion(rInfo);
            }
        }
        msg.setCode(Constant.SUCCESS);
        msg.setMsg(Constant.SUCCESS_MSG);
        return  msg;
    }


    //获取app注册用户
    private UserInfo getRegisterUserInfo(RegisterUserEvent userEvent) throws Exception {
        UserInfo userInfo = new UserInfo();
        //组装新用户的参数
        userInfo.setId(userInfoMapper.queryUserInfoMaxId());
        userInfo.setTelephone(userEvent.getTelephone());
        userInfo.setReal_name(userEvent.getTelephone());
        userInfo.setUser_name(userEvent.getTelephone());
        userInfo.setReferrer_id(userEvent.getReferrer_id());
        userInfo.setRegister_channel(userEvent.getRegister_channel());
        userInfo.setRegister_type(userEvent.getRegister_type());
        userInfo.setLogin_password(Base64.encode(
                DESUntil.encode(appConfig.getDES_KEY(), userEvent.getLogin_password())));
        return userInfo;
    }



    /**
     * 基金开户
     * @param event
     * @return
     */
    @Transactional
    public Msg  fundAccountRegister(AddBankCardEvent event) throws Exception{

        if(userInfoMapper.countUserById(event.getId_no())>0)
            return new Msg(Constant.FAIL,"您的身份证已被开户");
        if(event.getPassword()==null){
            List<BankAccountInfo> bankAccountInfos=
                    userBankAccountMapper.queryAccountByUserId(event.getUser_tid());
            if(bankAccountInfos==null||bankAccountInfos.isEmpty()){
                return new Msg(Constant.FAIL,"参数有误");
            }
            event.setPassword(DESUntil.decode(appConfig.getDES_KEY(), Base64.decode(
                                                             bankAccountInfos.get(0).getPassword())));
        }

        HSBankCodeInfo hsBankCodeInfo =hsBankMapper.queryHsBankCode(event.getBank_no());
        if(hsBankCodeInfo==null){
            return  new Msg(Constant.SUCCESS,"该银行通道已关闭");
        }

        //基金开户  封装参数
        Msg msg =new Msg();
        //验证验证码
        msg =hundSunPostmessagesignBySms(event,hsBankCodeInfo);
        if(msg.getCode()==Constant.FAIL)
            return  msg;
        //调用恒生接口基金
        ReFundAccountEvent reFundAccountEvent = transReFundAccountEvent(event,hsBankCodeInfo);
        Msg refundAccountMsg = hundSunFundAccount(reFundAccountEvent);
        RgisterFundReply reply =(RgisterFundReply) refundAccountMsg.getData();
        if(refundAccountMsg.getCode()==Constant.FAIL)
            return new Msg(Constant.FAIL,reply.getMessage());
        //插入本地 用户银行账户表
        BankAccountInfo bankAccountInfo =new BankAccountInfo();
        bankAccountInfo.setBank_account(event.getBank_account());
        bankAccountInfo.setBank_account_name(event.getBank_account_name());
        bankAccountInfo.setBank_name(event.getBank_name());
        bankAccountInfo.setBank_no(event.getBank_no());
        bankAccountInfo.setId_kind_gb("0");
        bankAccountInfo.setId_no(event.getId_no());
        bankAccountInfo.setPassword(Base64.encode(
                DESUntil.encode(appConfig.getDES_KEY(),event.getPassword())));
        bankAccountInfo.setTelephone(event.getTelephone());
        bankAccountInfo.setTrade_acco(reply.getTrade_acco());
        bankAccountInfo.setTa_acco(reply.getTa_acco());
        bankAccountInfo.setUser_tid(event.getUser_tid());
        bankAccountInfo.setBranch_bank_no(reFundAccountEvent.getBranch_bank_no());
        bankAccountInfo.setCapital_mode(reFundAccountEvent.getCapital_mode());
        bankAccountInfo.setDetail_fund_way(reFundAccountEvent.getDetail_fund_way());
        bankAccountInfo.setCapital_mode_name(hsBankCodeInfo.getMoney_type());
        //把该用户的账户信息插入到 银行账户表里面
        userBankAccountMapper.insertUserBankAccount(bankAccountInfo);
        //将用户身份证信息 交易密码等更新到user_info
        userInfoMapper.updateUserByReFund(event.getId_no(),event.getBank_account_name(),
                Base64.encode(
                        DESUntil.encode(appConfig.getDES_KEY(),event.getPassword())),event.getUser_tid(),reply.getClient_id());
        msg.setCode(Constant.SUCCESS);
        msg.setMsg("注册成功");
        return  msg;
    }

    /**
     * 增开交易账户 新增银行卡
     * @param event
     * @return
     */
    @Transactional
    public Msg  addTradeAccount(AddBankCardEvent event) throws Exception{


        List<BankAccountInfo> bankAccountInfos=
                    userBankAccountMapper.queryAccountByUserId(event.getUser_tid());
        if(bankAccountInfos==null||bankAccountInfos.isEmpty()){
                return new Msg(Constant.FAIL,"参数有误");
        }
        List<BankAccountInfo> sameBankList =bankAccountInfos.stream().
                filter(o->o.getBank_no().equals(event.getBank_no())).collect(Collectors.toList());
        if(sameBankList!=null&&!sameBankList.isEmpty()){
            return  new Msg(Constant.FAIL,"您已绑定过此银行的银行卡");
        }

        event.setPassword(DESUntil.decode(appConfig.getDES_KEY(), Base64.decode(
                    bankAccountInfos.get(0).getPassword())));
        event.setId_no(bankAccountInfos.get(0).getId_no());
        event.setBank_account_name(bankAccountInfos.get(0).getBank_account_name());
        //基金开户  封装参数
        //验证验证码
        HSBankCodeInfo hsBankCodeInfo =hsBankMapper.queryHsBankCode(event.getBank_no());
        if(hsBankCodeInfo==null){
            return  new Msg(Constant.SUCCESS,"该银行通道已关闭");
        }

        Msg msg =hundSunPostmessagesignBySms(event,hsBankCodeInfo);

        if(msg.getCode()==Constant.FAIL)
            return  msg;

        ReFundAccountEvent reFundAccountEvent = transReFundAccountEvent(event,hsBankCodeInfo);
        //调用恒生接口 增开基金账户
        Msg refundAccountMsg = openTradeaccount(reFundAccountEvent,bankAccountInfos.get(0).getTrade_acco());
        //返回结果
        OpenTradeReply reply =(OpenTradeReply) refundAccountMsg.getData();
        if(refundAccountMsg.getCode()==Constant.FAIL)
            return new Msg(Constant.FAIL,reply.getMessage());
        //插入本地 用户银行账户表
        BankAccountInfo bankAccountInfo =new BankAccountInfo();
        bankAccountInfo.setBank_account(event.getBank_account());
        bankAccountInfo.setBank_account_name(event.getBank_account_name());
        bankAccountInfo.setBank_name(event.getBank_name());
        bankAccountInfo.setBank_no(event.getBank_no());
        bankAccountInfo.setId_kind_gb("0");
        bankAccountInfo.setId_no(event.getId_no());
        bankAccountInfo.setPassword(Base64.encode(
                DESUntil.encode(appConfig.getDES_KEY(),event.getPassword())));
        bankAccountInfo.setTelephone(event.getTelephone());
        bankAccountInfo.setTrade_acco(reply.getTrade_acco());
        bankAccountInfo.setUser_tid(event.getUser_tid());
        bankAccountInfo.setBranch_bank_no(reFundAccountEvent.getBranch_bank_no());
        bankAccountInfo.setCapital_mode(reFundAccountEvent.getCapital_mode());
        bankAccountInfo.setDetail_fund_way(reFundAccountEvent.getDetail_fund_way());
        bankAccountInfo.setCapital_mode_name(hsBankCodeInfo.getMoney_type());
        //把该用户的账户信息插入到 银行账户表里面
        userBankAccountMapper.insertUserBankAccount(bankAccountInfo);

        msg.setCode(Constant.SUCCESS);
        msg.setMsg(Constant.SUCCESS_MSG);
        return  msg;
    }


    /**
     * 基金开户发送验证码
     * @param event
     * @return
     */
    public Msg registerFundAccountSendSms(AddBankCardEvent event){
        return hundSunPremessagesignBySms(event);
    }




    /**
     * 调用恒生接口短信签约确认
     * @param event
     * @return
     */
    public Msg  hundSunPostmessagesignBySms(AddBankCardEvent event,HSBankCodeInfo hsBankCodeInfo){

        Msg msg =new Msg();

        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<String, String>();
        paramMap.add("allot_no",Format.format(new Date())); //申请编号 使用时间戳
        paramMap.add("bank_account",event.getBank_account()); //银行帐号
        paramMap.add("bank_no", event.getBank_no());  //银行编号
        paramMap.add("capital_mode", hsBankCodeInfo.getMoney_type_code());  //资金方式目前 确定是快钱 ‘l’
        paramMap.add("client_name", event.getBank_account_name());//客户名称
        paramMap.add("id_kind_gb","0");  //证件类型
        paramMap.add("id_no", event.getId_no());  //证件号
        paramMap.add("mobile_authcode", event.getVerifyCode()); //手机验证码
        paramMap.add("mobile_tel", event.getTelephone()); //手机号
        paramMap.add("original_appno",event.getAllot_no());//原申请单号
        paramMap.add("sign_biztype", "00"); //签约业务类型
        paramMap.add("other_serial",event.getOther_serial());//对方流水号


        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add( new FormHttpMessageConverter());
        PostmessagesignReply reply =
                restTemplate.postForObject("http://"+appConfig.getIP_PORT()+"/openapi/fundbusrestful/capital/postmessagesign",
                        paramMap, PostmessagesignReply.class);

        if(reply.getCode().equals(Constant.HS_SUCCESS_CODE)){
            msg.setCode(Constant.SUCCESS);
            msg.setMsg(Constant.SUCCESS_MSG);
        }else {
            msg.setCode(Constant.FAIL);
            msg.setMsg(reply.getMessage());
        }
        return  msg;

    }


    /**
     * 调用恒生发起短信签约
     * @param event
     * @return
     */
    public Msg  hundSunPremessagesignBySms(AddBankCardEvent event){

        Msg msg =new Msg();

        HSBankCodeInfo hsBankCodeInfo =hsBankMapper.queryHsBankCode(event.getBank_no());
        if(hsBankCodeInfo==null){
            return  new Msg(Constant.SUCCESS,"该银行通道已关闭");
        }
        String  allot_no = Format.format(new Date());

        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<String, String>();
        paramMap.add("allot_no",allot_no); //申请编号 使用时间戳
        paramMap.add("bank_account",event.getBank_account()); //银行帐号
        paramMap.add("bank_no", event.getBank_no());  //银行编号
        paramMap.add("capital_mode", hsBankCodeInfo.getMoney_type_code());  //资金方式
        paramMap.add("client_name", event.getBank_account_name());//客户名称
        paramMap.add("id_kind_gb","0");  //证件类型
        paramMap.add("id_no", event.getId_no());  //证件号
        paramMap.add("mobile_tel", event.getTelephone()); //手机号
        paramMap.add("sign_biztype", "00"); //签约业务类型

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add( new FormHttpMessageConverter() );
        PremessAgesignReply agesignReply =
                restTemplate.postForObject("http://"+appConfig.getIP_PORT()+"/openapi/fundbusrestful/capital/premessagesign",
                        paramMap,PremessAgesignReply.class);
        if(agesignReply.getCode().equals(Constant.HS_SUCCESS_CODE)){
            FundCodeReturn fundCodeReturn =new FundCodeReturn();
            fundCodeReturn.setAllot_no(allot_no);
            fundCodeReturn.setOther_serial(agesignReply.getOther_serial());
            msg.setCode(Constant.SUCCESS);
            msg.setMsg(Constant.SUCCESS_MSG);
            msg.setData(fundCodeReturn);
        }else {
            msg.setCode(Constant.FAIL);
            msg.setMsg(agesignReply.getMessage());
        }
        return  msg;

    }


    /**
     * 调用恒生接口基金开户
     * @param event
     * @return
     */
    public Msg  hundSunFundAccount(ReFundAccountEvent event){

        Msg msg =new Msg();
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<String, String>();
        paramMap.add("bank_account", event.getBank_account());
        paramMap.add("bank_account_name", event.getBank_account_name());
        paramMap.add("bank_name", event.getBank_name());
        paramMap.add("bank_no", event.getBank_no());
        paramMap.add("capital_mode", event.getCapital_mode());
        paramMap.add("branch_bank_no",event.getBranch_bank_no());//联行号
        paramMap.add("client_full_name",event.getClient_full_name());
        paramMap.add("client_name", event.getClient_name());
        paramMap.add("id_kind_gb", event.getId_kind_gb());
        paramMap.add("client_gender",event.getClient_gender());//客户性别
        paramMap.add("id_no", event.getId_no());
        paramMap.add("mobile_tel", event.getMobile_tel());
        paramMap.add("password", event.getPassword());
        paramMap.add("detail_fund_way",event.getDetail_fund_way()); // 明细资金方式 支持托收代扣，空：不支持托收代扣

        RestTemplate restTemplate = new RestTemplate();
        RgisterFundReply hundSunReply =
                restTemplate.postForObject("http://"+appConfig.getIP_PORT()+"/openapi/fundbusrestful/account/openfundaccount",
                        paramMap,  RgisterFundReply.class);

        L.info("HS fund Register return is "+hundSunReply.toString());
        if(hundSunReply.getCode().equals(Constant.HS_SUCCESS_CODE)){
            msg.setCode(Constant.SUCCESS);
            msg.setMsg(Constant.SUCCESS_MSG);
        }else {
            msg.setCode(Constant.FAIL);
            msg.setMsg(hundSunReply.getMessage());
        }
        msg.setData(hundSunReply);
        return  msg;

    }



    /**
     * 增开交易账号 恒生接口
     * @param event
     * @return
     */
    public Msg  openTradeaccount(ReFundAccountEvent event,String trade_acco){

        Msg msg =new Msg();
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<String, String>();
        paramMap.add("bank_account", event.getBank_account());
        paramMap.add("bank_account_name", event.getBank_account_name());
        paramMap.add("bank_name", event.getBank_name());
        paramMap.add("bank_no", event.getBank_no());
        paramMap.add("branch_bank_no",event.getBranch_bank_no());
        paramMap.add("capital_mode", "l");
        paramMap.add("password", event.getPassword());
        paramMap.add("detail_fund_way","01"); // 明细资金方式 支持托收代扣，空：不支持托收代扣
        paramMap.add("trade_acco",trade_acco); //交易帐号

        RestTemplate restTemplate = new RestTemplate();
        OpenTradeReply reply =
                restTemplate.postForObject("http://"+appConfig.getIP_PORT()+"/openapi/fundbusrestful/account/opentradeaccount",
                        paramMap,  OpenTradeReply.class);

        L.info("HS add fund account  return is "+reply.toString());
        if(reply.getCode().equals(Constant.HS_SUCCESS_CODE)){
            msg.setCode(Constant.SUCCESS);
            msg.setMsg(Constant.SUCCESS_MSG);
        }else {
            msg.setCode(Constant.FAIL);
            msg.setMsg(reply.getMessage());
        }
        msg.setData(reply);
        return  msg;

    }


    /**
     * 将银行开户参数转换成基金开户参数
     * @param event
     * @return
     */
    private ReFundAccountEvent transReFundAccountEvent(AddBankCardEvent event,HSBankCodeInfo hsBankCodeInfo) {
        ReFundAccountEvent reFundAccountEvent  =new ReFundAccountEvent();
        reFundAccountEvent.setBank_account(event.getBank_account());
        reFundAccountEvent.setBank_account_name(event.getBank_account_name());
        reFundAccountEvent.setBank_name(event.getBank_name());
        reFundAccountEvent.setBank_no(event.getBank_no());
        reFundAccountEvent.setCapital_mode(hsBankCodeInfo.getMoney_type_code()); //资金支付方式目前 因后台支持未上目前写固定  后续版本更改为读库
        reFundAccountEvent.setClient_full_name(event.getBank_account_name());
        reFundAccountEvent.setClient_name(event.getBank_account_name());
        reFundAccountEvent.setId_kind_gb("0");
        reFundAccountEvent.setMobile_tel(event.getTelephone());
        reFundAccountEvent.setId_no(event.getId_no());
        reFundAccountEvent.setPassword(event.getPassword());
        reFundAccountEvent.setBranch_bank_no(hsBankCodeInfo.getBranch_bank_no());//联行号
        reFundAccountEvent.setClient_gender(IDCardUtil.genderCode(event.getId_no()));//身份证
        reFundAccountEvent.setDetail_fund_way("01");
        return reFundAccountEvent;
    }


    //生成 分享链接
    //生成 分享链接
    @Transactional
    public ShareLinkReturnEvent createShareLink(CreateShareLinkEvent event) throws Exception {

        /**
         * 加密密钥
         * @var int
         */
        ShareLinkReturnEvent returnEvent = new ShareLinkReturnEvent();
        //生成100 以内随机数
        Random ra = new Random();
        Integer rand = ra.nextInt(1000);
        event.setRandom(rand);

        //将数据加密

        //先DES 加密 再 Base64 加密
        String shareUrl = Base64.encode(DESUntil.encode(appConfig.getDES_KEY(), JSON.toJSONString(event)));

        //分享链接信息插入数据库
        UserShareInfo shareInfo = new UserShareInfo();
        shareInfo.setExtend_type(event.getExtend_type());
        shareInfo.setShare_user_tid(event.getUser_tid());
        shareInfo.setRandom(event.getRandom());
        shareInfo.setEnd_time(event.getTime());
        userShareActivityMapper.insertShareLink(shareInfo);
        L.info("share link =>" + "uid:" + event.getUser_tid() + "=" + shareUrl);
        returnEvent.setShare(shareUrl);
        return returnEvent;

    }


    /**
     * 分享 注册新用户
     *
     * @param sUEvent 分享新注册用户参数
     */
    public Msg reNewUserByShare(ShareReUserEvent sUEvent) throws Exception {
        Msg msg =new Msg();
        //验证该手机号是否已经注册
        if(userInfoMapper.existUserBytelephone(sUEvent.getTelephone())!=0){
            msg.setCode(Constant.FAIL);
            msg.setMsg("该手机号已经注册");
            return  msg;
        }

        //------------验证加密信息
        CreateShareLinkEvent shareLinInfo = null;
        //解密
        String link = DESUntil.decode(appConfig.getDES_KEY(), Base64.decode(sUEvent.getShare_link()));
        if (StringUtils.isEmpty(link)) {
            L.warn("Decrypt Link is Empty =>" + sUEvent.getShare_link());
        } else {
            shareLinInfo = JSON.parseObject(link, CreateShareLinkEvent.class);
            if (shareLinInfo != null) {
                if (shareLinInfo.getTime() < System.currentTimeMillis() / 1000)
                    return new Msg(1, "活动时间过期");
                if (userShareActivityMapper.checkSharelink(shareLinInfo) == 0)
                    return new Msg(1, "参数校验不通过");
            } else {
                L.warn("Cant Parse Link =>" + link);
            }
        }
        if (shareLinInfo == null) {
            shareLinInfo = new CreateShareLinkEvent();
            shareLinInfo.setUser_tid(0);
        }
        //--------------------------------
        //验证验证码是否正确（注册）
        msg = vaildVerifyCode.vaildVerifyCode(sUEvent.getTelephone(), 1, sUEvent.getVerifyCode());
        //如果验证码有误 马上返回错误
        if (msg.getCode() == 1) {
            return msg;
        }
        //      ----------插入新用户进数据库
       //找出推荐人的信息

        UserInfo refferUser=userInfoMapper.findUserById(shareLinInfo.getUser_tid());
        if(refferUser ==null){
            return  new Msg(Constant.FAIL,"推荐人不存在");
        }
        //去查询该推荐人是否为私募的理财经理

        UserInfo newUser =new UserInfo();
        newUser.setId(userInfoMapper.queryUserInfoMaxId());
        newUser.setTelephone(sUEvent.getTelephone());
        newUser.setReal_name(sUEvent.getTelephone());
        newUser.setUser_name(sUEvent.getTelephone());
        newUser.setReferrer_id(shareLinInfo.getUser_tid());
        newUser.setRegister_channel(sUEvent.getRegister_channel());
        newUser.setRegister_type(sUEvent.getRegister_type());
        newUser.setLogin_password(Base64.encode(
                DESUntil.encode(appConfig.getDES_KEY(), sUEvent.getLogin_password())));
        userInfoMapper.insertUser(newUser);
        //绑定理财经理关系
        Integer lhid=lhUserInfoMapper.findLHUserIdByTel(refferUser.getTelephone());
        if(lhid ==null){ //如果 推荐人不为私募的理财经理
            //查找推荐人 理财经理
            FaRelationInfo refferdFaRelation =faRelationMapper.queryFaRelationByUserId(lhid);
            if(refferdFaRelation!=null){ //如果推荐人存在理财经理
                if(refferdFaRelation.getWidth()==1){ //而且推荐人与理财经理之间是一级绑定关系
                    //那么就将新用户与理财经理绑定二级绑定关系
                    FaRelationInfo newRelation =new FaRelationInfo();
                    newRelation.setFinancial_advisor_id(refferdFaRelation.getFinancial_advisor_id());
                    newRelation.setLh_app_user_id(newUser.getId());
                    newRelation.setWidth(2);
                    faRelationMapper.insertFaRealtion(newRelation);
                }
                //如果并非一级绑定关系 新用户不绑定任何理财经理
            }//如果推荐人没有理财经理 那么也不绑定任何理财经理

        }else{ //如果推荐人 为私募的理财经理
            //直接同该理财经理绑定一级绑定关系
            FaRelationInfo newRelation =new FaRelationInfo();
            newRelation.setFinancial_advisor_id(lhid);
            newRelation.setLh_app_user_id(newUser.getId());
            newRelation.setWidth(1);
            faRelationMapper.insertFaRealtion(newRelation);
        }

        msg.setCode(Constant.SUCCESS);
        msg.setMsg(Constant.SUCCESS_MSG);
        return  msg;

    }


    /**
     * 添加紧急联系人
     * @param event
     */
  public  void addEmerContact(AddEmeryContactEvent event){
      userInfoMapper.addEmerContact(event);
  }

    /**
     * 获取用户推荐人
     */
    public RecommendUserData queryReUser(Integer user_tid){
        RecommendUserData data =new RecommendUserData();
        List<RecoUserPojo> recoUserPojoList =userInfoMapper.queryReUser(user_tid);
        data.setList(recoUserPojoList);
        return data;
    }


}
