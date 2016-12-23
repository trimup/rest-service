/**
 * @Version 1.0.0
 * Copyright (c) 2016上海相诚金融-版权所有
 */
package com.lihe.util;

import com.lihe.common.Constant;
import com.lihe.common.Msg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @Class InterfaceCallingUtil
 * @Description 外部接口调用工具类
 * @Author 张超超
 * @Date 2016/3/28 16:04
 */
@Component
public class InterfaceCallingUtil {
    private static Logger L = LoggerFactory.getLogger(InterfaceCallingUtil.class);

    public static int GET = 0;
    public static int POST = 1;


    private static RestTemplate template;

//    @Autowired(required = true)
//    public void setTemplate(RestTemplate template) {
//        InterfaceCallingUtil.template = template;
//    }

    /**
     * 调用外部接口，json（get方法暂时有问题，勿调用）
     * @param url
     *          url地址
     * @param paramsMap
     *          参数
     * @param method
     *          get/post
     * @param inner
     *          是否调用内部方法
     * @param  clazz
     *          返回值类型
     * @return T
     */
    public static <T> T callWithJson(String url, Map<String, String> paramsMap, int method, int inner, Class<T> clazz) {
        L.info("method callWithJson params : " + paramsMap);
        T t = null;
        try {
            t = clazz.newInstance();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, String>> entity = new HttpEntity<Map<String,String>>(paramsMap, headers);
            if (1 != inner) template = new RestTemplate();
//            if (GET == method) { //get方法暂时有问题，勿调用
//                ResponseEntity<Msg> responseEntity = template.exchange(url, HttpMethod.GET, entity, Msg.class);
//                msg = responseEntity.getBody();
//            } else
            if (POST == method) { //post
                ResponseEntity<T> responseEntity = template.exchange(url, HttpMethod.POST, entity, clazz);
                t = responseEntity.getBody();
            } else {
//                msg.setCode(Constant.FAIL);
//                msg.setMsg("method 参数错误");
            }
        } catch (Exception e) {
//            msg.setCode(Constant.EXCEPTION);
//            msg.setData(e);
//            msg.setMsg("发生异常");
            L.info("method callWithJson exception ", e);
        }
        L.info("method callInterface return : " + t);
        return t;
    }

    /**
     * 调用外部接口
     * @param url
     *          url地址
     * @param paramsMap
     *          参数
     * @param method
     *          get/post
     * @param inner
     *          是否调用内部方法
     * @param  clazz
     *          返回值类型
     * @return T
     */
    public static <T> T call(String url, Map<String, String> paramsMap, int method, int inner, Class<T> clazz) {
        L.info("method callInterface params : " + paramsMap);
        T t = null;
        try {
            t = clazz.newInstance();
            if (paramsMap.keySet().size() > 0) {
                StringBuilder urlWithParam = new StringBuilder(url + "?");
                paramsMap.forEach((key,value) -> {urlWithParam.append(key).append("=").append(value).append("&");});
                url = urlWithParam.substring(0, urlWithParam.length() - 1);
            }
            if (1 != inner) template = new RestTemplate();
            if (GET == method) { //get
                ResponseEntity<T> entity = template.getForEntity(url, clazz, new Object[]{});
                t = entity.getBody();
            } else if (POST == method) { //post
                ResponseEntity<T> entity = template.postForEntity(url, null, clazz, new Object[]{});
                t = entity.getBody();
                L.info("method call result : " + t);
            } else {
//                msg.setCode(Constant.FAIL);
//                msg.setMsg("method 参数错误");
            }
        } catch (Exception e) {
//            msg.setCode(Constant.EXCEPTION);
//            msg.setData(e);
//            msg.setMsg("发生异常");
            L.info("method callInterface exception ", e);
        }
        L.info("method callInterface return : " + t);
        return t;
    }
}
