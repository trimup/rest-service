package com.lihe.service;

import com.lihe.AppConfig;
import com.lihe.Event.EvalResultEvent;
import com.lihe.Event.EvalResultReturn;
import com.lihe.hundsunreply.EvalResultReply;
import com.lihe.Event.ReonlineJudgeReturn;
import com.lihe.common.Constant;
import com.lihe.common.Msg;
import com.lihe.entity.UserInfo;
import com.lihe.hundsunreply.RequestHSQuestionReturn;
import com.lihe.persistence.EvalRiskLogMapper;
import com.lihe.persistence.KeyValueMapper;
import com.lihe.persistence.UserInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;

/**
 * Created by trimup on 2016/7/27.
 */
@Service
public class OnLineEvaluationService {

    private static final Logger L = LoggerFactory.getLogger(OnLineEvaluationService.class);

    private static final SimpleDateFormat Format = new SimpleDateFormat("yyyyMMddHHmmss");


    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private KeyValueMapper keyValueMapper;
    @Autowired
    private EvalRiskLogMapper evalRiskLogMapper;
    @Autowired
    private AppConfig appConfig;

    /**
     * 获取恒生在线评测 题目
     * @return
     */
    public Msg getHundSunPaperinfo(Integer user_tid){

        if(evalRiskLogMapper.countUserRiskEvalByYear(user_tid)>5)
            return  new Msg(Constant.RISK_EVAL_FLAG,"该用户风险测评本年度已经超过五次");
        Msg msg =new Msg();
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<String, String>();
        HttpHeaders requestHeaders = new HttpHeaders();
        paramMap.add("paper_client_type","0");
        RestTemplate restTemplate = new RestTemplate();
        String url = appConfig.getURL_HS_PAPERINFO_QUERY();
        url = MessageFormat.format(url, appConfig.getIP_PORT());
        RequestHSQuestionReturn reply =
                restTemplate.postForObject(url,paramMap,RequestHSQuestionReturn.class);
        L.info("reqeust hundSun paper result is  "+reply.toString());
        if(!reply.getCode().equals(Constant.HS_SUCCESS_CODE)){
            msg.setCode(Constant.FAIL);
            msg.setMsg(reply.getMessage());
            return  msg;
        }
        ReonlineJudgeReturn judgeReturn =new ReonlineJudgeReturn();
        judgeReturn.setRowcount(reply.getRowcount());
        judgeReturn.setTotal_count(reply.getTotal_count());
        judgeReturn.setQuestions(reply.getQuestions());
        msg.setCode(Constant.SUCCESS);
        msg.setMsg(Constant.SUCCESS_MSG);
        msg.setData(judgeReturn);
        return  msg;

    }


    /**
     * 获取恒生在线评测结果
     * @return
     */
    public Msg getOnlineEvalResult(EvalResultEvent event){


        if(evalRiskLogMapper.countUserRiskEvalByYear(event.getUser_tid())>5)
            return  new Msg(Constant.RISK_EVAL_FLAG,"该用户风险测评本年度已经超过五次");
        Msg msg =new Msg();

        UserInfo userInfo= userInfoMapper.findUserById(event.getUser_tid());

        if(userInfo.getIdentity_card()==null)
            return  new Msg(1,"该用户还未绑定银行,请先绑定银行卡再进来测评");
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("full_name",userInfo.getReal_name()); //
        paramMap.add("id_kind_gb","0"); //证件类型
        paramMap.add("id_no",userInfo.getIdentity_card()); // 证件编号
        paramMap.add("elig_content",event.getElig_content()); // 答题类容

        RestTemplate restTemplate = new RestTemplate();
        String url = appConfig.getURL_HS_SUBMIT_PAPER();
        url = MessageFormat.format(url, appConfig.getIP_PORT());
        EvalResultReply reply  =
                restTemplate.postForObject(url, paramMap, EvalResultReply.class);
        L.info("user "+event.getUser_tid()+"evaluation   is "+reply.toString());
        if(!reply.getCode().equals(Constant.HS_SUCCESS_CODE)){
            msg.setCode(Constant.FAIL);
            msg.setMsg(reply.getMessage());
            return  msg;
        }
        EvalResultReturn resultReturn =new EvalResultReturn();
        resultReturn.setInvest_risk_tolerance(keyValueMapper.getEvalResultCode(reply.getInvest_risk_tolerance()));
        //更新用户表中的 用户风险等级
        userInfoMapper.updateUserRiskLevel(event.getUser_tid(),reply.getInvest_risk_tolerance());
        //如果风险评测到 用户风险等级日志标中
        evalRiskLogMapper.insertUserRiskEval(event.getUser_tid(),reply.getInvest_risk_tolerance());
        msg.setCode(Constant.SUCCESS);
        msg.setMsg(Constant.SUCCESS_MSG);
        msg.setData(resultReturn);
        return  msg;

    }
}
