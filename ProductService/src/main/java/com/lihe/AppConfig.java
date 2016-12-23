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

    //可修改分红方式列表
    @Value("${URL_HS_BONUSLIST_QUERY}") private String URL_HS_BONUSLIST_QUERY;
    public String getURL_HS_BONUSLIST_QUERY(){
        return URL_HS_BONUSLIST_QUERY;
    }

    //交易结果查询url
    @Value("${URL_HS_TRADERESULTQUERY}")
    private String URL_HS_TRADERESULTQUERY;
    public String getURL_HS_TRADERESULTQUERY(){
        return URL_HS_TRADERESULTQUERY;
    }


    //申购url
    @Value("${URL_HS_PURCHASEAPPLY}") private String URL_HS_PURCHASEAPPLY;
    public String getURL_HS_PURCHASEAPPLY(){
        return URL_HS_PURCHASEAPPLY;
    }

    //认购url
    @Value("${URL_HS_SUBSCRIBEAPPLY}") private String URL_HS_SUBSCRIBEAPPLY;
    public String getURL_HS_SUBSCRIBEAPPLY(){
        return URL_HS_PURCHASEAPPLY;
    }

    //交易申请查询 url
    @Value("${URL_HS_TRADEAPPLYQUERY}") private String URL_HS_TRADEAPPLYQUERY;
    public String getURL_HS_TRADEAPPLYQUERY(){
        return URL_HS_TRADEAPPLYQUERY;
    }

    //浮动收益查询 url
    @Value("${URL_HS_FLOATING_PROFIT_QUERY}") private String URL_HS_FLOATING_PROFIT_QUERY;
    public String getURL_HS_FLOATING_PROFIT_QUERY(){
        return URL_HS_FLOATING_PROFIT_QUERY;
    }

    //修改用户分红方式 url
    @Value("${URL_HS_MODIFY_BONUS}") private String URL_HS_MODIFY_BONUS;
    public String getURL_HS_MODIFY_BONUS(){
        return URL_HS_MODIFY_BONUS;
    }

    //份额查询 url
    @Value("${URL_HS_SHARE_QUERY}") private String URL_HS_SHARE_QUERY;
    public String getURL_HS_SHARE_QUERY(){
        return URL_HS_SHARE_QUERY;
    }

    //交易确认查询 url
    @Value("${URL_HS_TRADECONFIRMQUERY}") private String URL_HS_TRADECONFIRMQUERY;
    public String getURL_HS_TRADECONFIRMQUERY(){
        return URL_HS_TRADECONFIRMQUERY;
    }

    //计算手续费 url
    @Value("${URL_HS_TRANSACTION_FEECAL_QUERY}") private String URL_HS_TRANSACTION_FEECAL_QUERY;
    public String getURL_HS_TRANSACTION_FEECAL_QUERY(){
        return URL_HS_TRANSACTION_FEECAL_QUERY;
    }

    //赎回 url
    @Value("${URL_HS_REDEEM}") private String URL_HS_REDEEM;
    public String getURL_HS_REDEEM(){
        return URL_HS_REDEEM;
    }


}
