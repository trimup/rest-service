package com.lihe.pojo;

import lombok.Data;

/**
 * Created by trimup on 2016/8/29.
 */
@Data
public class UserSafeCenterPojo {
    String telephone;
    String real_name;
    Integer risk_level;//风险等级
    EmerContactPojo emerContactPojo;
    boolean isEmerContact =false;// 是否有紧急联系人
    boolean  isBank=false;//是否绑定银行卡



}
