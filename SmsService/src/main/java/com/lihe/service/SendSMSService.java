package com.lihe.service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lihe.AppConfig;
import com.lihe.common.Constant;
import com.lihe.common.Msg;
import com.lihe.controller.SendSMSController;
import com.lihe.event.SendSmsEvent;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by trimup on 2016/7/25.
 */
@Service
public class SendSMSService {


    private static final Logger L   =  LoggerFactory.getLogger(SendSMSController.class);

    private static final SimpleDateFormat Format = new SimpleDateFormat("yyyyMMddHHmmss");

    @Autowired
    private AppConfig appConfig;


    /**
     * 调用创蓝接口发送短信
     * @param event
     * @return
     */
    public Msg sendSmsWithChuangLan(SendSmsEvent event) throws Exception{
        boolean needstatus = true;// 是否需要状态报告，需要true，不需要false
        String extno = null;// 扩展码
            return batchSend(appConfig.getCL_PATH(), appConfig.getCL_USER_NAME(), appConfig.getCL_PASSWORD(),
                    event.getTelephone(), event.getContent(), needstatus, extno);
    }



    /**
     * 通过云融通道发送短信 (发送单条短信)
     */
    public Msg sendSmsWithYunRong(SendSmsEvent event){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("cmd", "sendMessage"));
        params.add(new BasicNameValuePair("userName", appConfig.getYR_USER_NAME()));
        params.add(new BasicNameValuePair("passWord", appConfig.getYR_PASSWORD()));
        params.add(new BasicNameValuePair("messageId", Format.format(new Date())));
        params.add(new BasicNameValuePair("contentType", "sms/mt"));
        params.add(new BasicNameValuePair("phoneNumber", event.getTelephone()));
        params.add(new BasicNameValuePair("subject", ""));
        params.add(new BasicNameValuePair("body", event.getContent()));
        params.add(new BasicNameValuePair("serviceCode", ""));
        params.add(new BasicNameValuePair("serviceCodeExt", ""));
        params.add(new BasicNameValuePair("scheduleDateStr", ""));
        return  sendPostMessage(params);
    }


    /*
	 * params 填写的URL的参数 encode 字节编码
	 */
    public  Msg sendPostMessage(List<NameValuePair> params) {
        Msg msg =new Msg();
        HttpPost httpRequest = new HttpPost(appConfig.getYR_PATH());
        String strResult = "";
        try {
			/* 添加请求参数到请求对象 */
            httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			/* 发送请求并等待响应 */
            HttpResponse httpResponse = new DefaultHttpClient()
                    .execute(httpRequest);
			/* 若状态码为200 ok */
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
				/* 读返回数据 */
                strResult = EntityUtils.toString(httpResponse
                        .getEntity());
                if("0".equals(strResult)){
                    msg.setCode(Constant.SUCCESS);
                    msg.setMsg(Constant.SUCCESS_MSG);
                    msg.setData(strResult);
                }else{
                    msg.setCode(Constant.FAIL);
                    msg.setMsg("发送失败");
                    msg.setData(strResult);
                }
            }else {
                msg.setCode(Constant.FAIL);
                msg.setMsg("请求失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msg;
    }


    /**
     *
     * @param url
     *            应用地址，类似于http://ip:port/msg/
     * @param account
     *            账号
     * @param pswd
     *            密码
     * @param mobile
     *            手机号码，多个号码使用","分割
     * @param msg
     *            短信内容
     * @param needstatus
     *            是否需要状态报告，需要true，不需要false
     * @return 返回值定义参见HTTP协议文档
     * @throws Exception
     */
    public Msg  batchSend(String url,String account,String pswd,String mobile,String msg,boolean needstatus,
                                   String extno) throws Exception{
        Msg reply =new Msg();
        HttpClient client=new HttpClient();
        GetMethod method=new GetMethod();
        try{
            L.info("the url is"+url);
            URI base=new URI(url,false);
            method.setURI(new URI(base,"HttpBatchSendSM",false));
            method.setQueryString(new org.apache.commons.httpclient.NameValuePair[]{new org.apache.commons.httpclient.NameValuePair("account",account),
                    new org.apache.commons.httpclient.NameValuePair("pswd",pswd),new org.apache.commons.httpclient.NameValuePair("mobile",mobile),
                    new org.apache.commons.httpclient.NameValuePair("needstatus",String.valueOf(needstatus)),new org.apache.commons.httpclient.NameValuePair("msg",msg),
                    new org.apache.commons.httpclient.NameValuePair("extno",extno),});
            int result=client.executeMethod(method);
            if(result== HttpStatus.SC_OK) {
                InputStream in=method.getResponseBodyAsStream();
                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                byte[] buffer=new byte[1024];
                int len=0;
                while((len=in.read(buffer))!=-1){
                    baos.write(buffer,0,len);
                }
                String sendResult=URLDecoder.decode(baos.toString(),"UTF-8");
                if(sendResult.split(",")[1].startsWith("0")) {
                    reply.setCode(Constant.SUCCESS);
                    reply.setMsg(Constant.SUCCESS_MSG);
                    reply.setData(sendResult);
                }else {
                    reply.setCode(Constant.FAIL);
                    reply.setMsg("发送失败");
                    reply.setData(sendResult);
                }

            }else{
                reply.setCode(Constant.FAIL);
                reply.setMsg("请求失败");
            }
        }finally{
            method.releaseConnection();
        }
        L.info(mobile+"send sms result is"+msg.toString());
        return  reply;
    }


}
