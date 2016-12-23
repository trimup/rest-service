package com.lihe.proxy;

import com.lihe.common.Msg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by trimup on 2016/7/25.
 */
@Service
public class SMSProxy {
    @Autowired
    private RestTemplate restTemplate;
    private static final String SEND_SMS_URL ="http://sms-service/lihe/" +
            "smsService/sendSMS/sendSmsByChuangLan";
    private static final Logger L = LoggerFactory.getLogger(SMSProxy.class);


    /**
     * 发送短信
     * @param telephone 手机号
     * @param content 发送短信内容
     * @return 短信发送是否成功 code 0成功 1失败
     */
    public Msg sendSMSWithChuanglan(String telephone, String content)
    {
        Map sendMap =new HashMap<String ,String>();
        sendMap.put("telephone",telephone);
        sendMap.put("content", content);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(new MediaType("application", "json"));
        HttpEntity<Map> requestEntity = new HttpEntity<Map>(sendMap, requestHeaders);
        ResponseEntity<Msg>
                responseEntity = restTemplate.exchange(SEND_SMS_URL, HttpMethod.POST, requestEntity, Msg.class);
        Msg smsMsg = responseEntity.getBody();
        return  smsMsg;
    }
}
