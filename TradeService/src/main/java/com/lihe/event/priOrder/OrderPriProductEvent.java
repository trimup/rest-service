package com.lihe.event.priOrder;

import com.google.common.base.Strings;
import lombok.Data;

/**
 * Created by trimup on 2016/9/7.
 */
@Data
public class OrderPriProductEvent {
    private Integer user_tid;
    private String  token;
    private String  product_code;


    public boolean orCheck(){
        return user_tid==null|| Strings.isNullOrEmpty(token)||Strings.isNullOrEmpty(product_code);
    }
}
