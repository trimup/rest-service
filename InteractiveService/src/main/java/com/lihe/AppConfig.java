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
@Configuration
@RefreshScope
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

    //在线测评题目
    @Value("${URL_HS_PAPERINFO_QUERY}") private String URL_HS_PAPERINFO_QUERY;
    public String getURL_HS_PAPERINFO_QUERY() {
        return URL_HS_PAPERINFO_QUERY;
    }

    //在线测评题目
    @Value("${URL_HS_SUBMIT_PAPER}") private String URL_HS_SUBMIT_PAPER;
    public String getURL_HS_SUBMIT_PAPER() {
        return URL_HS_SUBMIT_PAPER;
    }
}
