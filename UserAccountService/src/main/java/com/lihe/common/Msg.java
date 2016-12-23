/**
 * @Version 1.0.0
 * Copyright (c) 2016上海相诚金融-版权所有
 */
package com.lihe.common;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @Class Msg
 * @Description 返回结果类
 * @Author 张超超
 * @Date 2016/3/24 14:51
 */
@Data
public class Msg implements Serializable {
    public static final int SUCCESS = 0;
    public static final int FAIL = 1;
    public static final int TOKEN_FAIL = 101;
    public static final String SUCCESS_MSG = "success";
    public static final String FAIL_MSG = "fail";
    private Integer code;
    private String msg;
    private Object data = new HashMap<>();

    public Msg(int i, String message) {
        this.code = i;
        this.msg = message;
    }

    public Msg() {

    }
}
