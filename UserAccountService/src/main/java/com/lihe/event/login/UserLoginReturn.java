package com.lihe.event.login;

import lombok.Data;

/**
 * Created by trimup on 2016/7/22.
 */
@Data
public class UserLoginReturn {

    private Integer    id            ;
    private String  real_name     ;
    private String  user_name     ;
    private String  identity_card ;
    private String  telephone     ;
    private String  token         ;
    private Boolean have_fp =false;    //是否有理财师
    private Integer fp_id;   //理财师id
    private String  fp_url="";
    private Boolean  have_risk=false;
    private Boolean  bind_bank_card=false;//是否有绑定银行卡

}
