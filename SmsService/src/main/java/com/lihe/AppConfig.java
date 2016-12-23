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
     * 云容账户名
     */
    @Value("${YR_USER_NAME}") private String YR_USER_NAME;
    public String getYR_USER_NAME() {
        return YR_USER_NAME;
    }

    /**
     * 云容密码
     */
    @Value("${YR_PASSWORD}") private String YR_PASSWORD;
    public String getYR_PASSWORD() {
        return YR_PASSWORD;
    }

    /**
     * 创蓝账户名
     */
    @Value("${CL_USER_NAME}") private String CL_USER_NAME;
    public String getCL_USER_NAME() {
        return CL_USER_NAME;
    }

    //创蓝密码
    @Value("${CL_PASSWORD}") private String CL_PASSWORD;
    public String getCL_PASSWORD() {
        return CL_PASSWORD;
    }

    //获取云容url
    @Value("${YR_PATH}") private String YR_PATH;
    public String getYR_PATH() {
        return YR_PATH;
    }

    //创蓝url
    @Value("${CL_PATH}") private String CL_PATH;
    public String getCL_PATH() {
        return CL_PATH;
    }


}
