package com.lihe.event.hsTrade;

import com.google.common.base.Strings;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by trimup on 2016/10/21
 * 计算申购基金费率.
 */
@Data
public class CalcPurchaseRateEvent {

    private String  fund_code;//基金代码
    private Integer bankAccounId; //用户基金账户信息ID
    private BigDecimal balance; //购买金额


    public boolean orCheck(){
        return Strings.isNullOrEmpty(fund_code)||bankAccounId==null||balance==null;
    }
}
