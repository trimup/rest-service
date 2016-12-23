package com.lihe.event.register;

import com.google.common.base.Strings;
import lombok.Data;

/**
 * Created by trimup on 2016/8/29.
 * 添加紧急联系人事件
 */
@Data
public class AddEmeryContactEvent {
    Integer user_tid;//用户id
    String token; //凭证
    String emer_contact_telephone; //紧急联系人手机号
    String emer_contact_name;   //紧急联系人姓名
    String emer_contact_relation;//紧急联系人关系




    public boolean isCheck(){
       return user_tid==null|| Strings.isNullOrEmpty(token)
               ||Strings.isNullOrEmpty(emer_contact_telephone)
               ||Strings.isNullOrEmpty(emer_contact_name)
               ||Strings.isNullOrEmpty(emer_contact_telephone);
    }
}
