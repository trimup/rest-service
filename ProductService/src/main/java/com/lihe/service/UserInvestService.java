package com.lihe.service;

import com.github.pagehelper.PageHelper;
import com.lihe.entity.HSProductInfo;
import com.lihe.entity.HsOrderInfo;
import com.lihe.event.QueryInvestEvent;
import com.lihe.persistence.HSOrderMapper;
import com.lihe.persistence.HSProductMapper;
import com.lihe.pojo.userInvest.UserInvestPojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by trimup on 2016/9/5.
 * 用户投资
 */
@Service
public class UserInvestService {
    private static final Logger L = LoggerFactory.getLogger(ProductDetailService.class);
    private static final SimpleDateFormat Format = new SimpleDateFormat("yyyy-MM-dd");


    @Autowired
    private HSOrderMapper hsOrderMapper;

    @Autowired
    private HSProductMapper hsProductMapper;


    /**
     * 查询用户公募订单接口
     * @param event
     */
    public void getUserPubFundList(QueryInvestEvent event){

        PageHelper.startPage(event.getPage(),event.getPageSize());
        //查询出该用户投资的订单
        List<HsOrderInfo>  userInvestList= hsOrderMapper.QueryHsOrder(event.getUser_tid());
        List<HSProductInfo> hsProductInfoList =hsProductMapper.queryAllHSProductInfo();
        Map<String,String> hsProductNameMap =hsProductInfoList.stream().
                collect(Collectors.toMap(HSProductInfo::getFund_code,HSProductInfo::getFund_name));
        List<UserInvestPojo>   investPojoList=new ArrayList<>();
        for(HsOrderInfo h : userInvestList){
            UserInvestPojo u =new UserInvestPojo();
            u.setBlance(h.getBalance());
            u.setFund_code(h.getFund_code());
            u.setFund_name(hsProductNameMap.get(h.getFund_code()));

        }

    }


}
