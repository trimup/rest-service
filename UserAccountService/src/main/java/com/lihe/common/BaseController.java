/**
 * @Version 1.0.0
 * Copyright (c) 2016上海相诚金融-版权所有
 */
package com.lihe.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

public class BaseController {

    private static final Logger l = LoggerFactory.getLogger(BaseController.class);

    /**
     * 错误统一返回
     *
     * @param e
     * @return
     */
    @ExceptionHandler({Exception.class})
    @ResponseBody
    public Msg exception(Exception e) {
        l.error(e.getMessage(), e);
        Msg reply = new Msg();
        reply.setCode(Constant.FAIL);
        reply.setMsg(e.getMessage());
        return reply;
    }
}
