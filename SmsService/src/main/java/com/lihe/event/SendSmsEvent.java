package com.lihe.event;

import com.google.common.base.Strings;
import lombok.Data;

/**
 * Created by trimup on 2016/7/25.
 */
@Data
public class SendSmsEvent {
    private String telephone;
    private String content;

    public boolean isCheck(){
        return Strings.isNullOrEmpty(telephone)||
                Strings.isNullOrEmpty(content);
    }
}
