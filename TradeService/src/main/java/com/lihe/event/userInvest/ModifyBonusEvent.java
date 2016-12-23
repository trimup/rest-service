package com.lihe.event.userInvest;

import com.google.common.base.Strings;
import lombok.Data;

/**
 * Created by trimup on 2016/9/23.
 * 修改分红方式时间
 */
@Data
public class ModifyBonusEvent {
    Integer user_tid;
    String auto_buy;//分红方式
    String fund_code;//基金代码
    String token;
    public boolean orCheck(){
        return  user_tid==null|| Strings.isNullOrEmpty(auto_buy)||
                Strings.isNullOrEmpty(fund_code)||Strings.isNullOrEmpty(token);
    }

}
