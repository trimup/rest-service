/*
 * Copyright (c) 2016. lihe-fund All Rights Reserved.
 */

package com.lihe.entity;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by leo on 4/3/16.
 */
public class CopyFromNotNullBeanUtilsBean extends BeanUtilsBean{

    private final String[] ignore;

    public CopyFromNotNullBeanUtilsBean(String... ignore) {
        this.ignore = ignore;
    }

    @Override
    public void copyProperty(Object bean, String name, Object value) throws IllegalAccessException,
        InvocationTargetException {
        if ( StringUtils.isEmpty(value)) {
            return;
        }
        if (!ArrayUtils.isEmpty(ignore)) {
            for (String n : ignore) {
                if (n.equalsIgnoreCase(name))
                    return;
            }
        }
        super.copyProperty(bean, name, value);
    }
}
