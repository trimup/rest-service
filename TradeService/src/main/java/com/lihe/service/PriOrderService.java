package com.lihe.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lihe.common.Constant;
import com.lihe.common.Msg;
import com.lihe.controller.PriOrderController;
import com.lihe.entity.OrderProductInfo;
import com.lihe.entity.PrivateProductInfo;
import com.lihe.event.priOrder.MyOrderData;
import com.lihe.event.priOrder.OrderPriProductEvent;
import com.lihe.event.priOrder.QueryMyOrderEvent;
import com.lihe.persistence.OrderProductMapper;
import com.lihe.persistence.PrivateProductMapper;
import com.lihe.pojo.MyOrderListPojo;
import com.lihe.util.NumberUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by trimup on 2016/9/7.
 */
@Service
public class PriOrderService {

    private static final Logger L = LoggerFactory.getLogger(PriOrderService.class);


    @Autowired
    private OrderProductMapper orderProductMapper;
    @Autowired
    private PrivateProductMapper privateProductMapper;

    /**
     * 预约私募基金
     */
    public Msg orderPrivateFund(OrderPriProductEvent event){
       if(orderProductMapper.queryLhOrderProduct(event.getUser_tid(),event.getProduct_code())==1)
           return new Msg(Constant.FAIL,"该基金已经预约成功！");
        if(privateProductMapper.countPriProductByCode(event.getProduct_code())<1)
            return new Msg(Constant.FAIL,"该基金不存在");
        Msg msg =new Msg();
        orderProductMapper.insertLhOrderProdcut(event.getUser_tid(),event.getProduct_code());
        msg.setCode(Constant.SUCCESS);
        msg.setMsg(Constant.SUCCESS_MSG);
        return  msg;

    }


    /**
     * 我的预约
     */
    public MyOrderData myOrderPrivateFund(QueryMyOrderEvent event){
        MyOrderData data =new MyOrderData();
        List<PrivateProductInfo>  privateProductInfos =
                privateProductMapper.queryAllPriProduct();
        Map<String,PrivateProductInfo> privateProductInfoMap =privateProductInfos.stream().
                collect(Collectors.toMap(PrivateProductInfo::getProduct_code,p->p));
        PageHelper.startPage(event.getPage(),event.getPageSize());
        List<OrderProductInfo>  orderProductInfos =orderProductMapper.queryOrderProductByUser(event.getUser_tid());
        List<MyOrderListPojo> myOrderListPojoList =new ArrayList<>();

        for(OrderProductInfo o : orderProductInfos){
            String result ="";
            PrivateProductInfo p =privateProductInfoMap.get(o.getProduct_code());
            MyOrderListPojo m =new MyOrderListPojo();

            switch (p.getProduct_type_id()) {
                case 10029:
                    result = "[阳光私募]";
                    break;
                case 10030:
                    result = "[定向增发]";
                    break;
                case 10027:
                    result = "[固定收益]";
                    break;
                case 10028:
                    result = "[固定收益]";
                    break;
                default:
                    result = "";
            }
            m.setId(p.getId());
            m.setProduct_type(p.getProduct_type_id());
            m.setProduct_name(p.getProduct_name()+result);
            myOrderListPojoList.add(m);
        }

        data.setPage(event.getPage());
        data.setPageSize(event.getPageSize());
        data.setList(myOrderListPojoList);
        long total =((Page) orderProductInfos).getTotal();
        data.setTotal(total);
        data.setTotalPage(NumberUtil.Division(event.getPageSize(),total));
        return  data;

    }
}
