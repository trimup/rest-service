package com.lihe;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by leo on 9/6/15.
 */
@RefreshScope
@Configuration
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class AppConfig extends WebMvcConfigurerAdapter {


    /**
     * 加密key
     */
    @Value("${DES_KEY}") private String DES_KEY;
    public String getDES_KEY() {
        return DES_KEY;
    }

    //恒生接口 地址
    @Value("${IP_PORT}")
    private String IP_PORT;
    public String getIP_PORT(){
        return IP_PORT;
    }

    //短信模版查询
    @Value("${VERIFY_SMS_MODLE}")
    private String[] VERIFY_SMS_MODLE;
    public String[] getVERIFY_SMS_MODLE(){
        return VERIFY_SMS_MODLE;
    }
    //短信最大条数
    @Value("${SMS_MAX}")
    private String SMS_MAX ="5";
    public int getSMS_MAX(){
        return Integer.parseInt(SMS_MAX);
    }

    /**验证码加密盐 */
    @Value("${SMS_SALT}")
    private String SMS_SALT = "0O";
    public String getSMS_SALT(){
        return SMS_SALT;
    }

    //交易结果查询url
    @Value("${URL_HS_TRADERESULTQUERY}") private String URL_HS_TRADERESULTQUERY;
    public String getURL_HS_TRADERESULTQUERY(){
        return URL_HS_TRADERESULTQUERY;
    }

    @Value("${TEST_BUS}")
    private String TEST_BUS;
    public String getTEST_BUS(){
        return  TEST_BUS;
    }


}
