/*
 * Copyright (c) 2016. lihe-fund All Rights Reserved.
 */

package com.lihe.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author leo
 * @version V1.0.0
 * @package com.lihe.entity
 * @date 21/12/2016
 */
@Builder
@Getter
@ToString
public class FundValueEntity {
    private String code;
    private String title;
    private List<Map<String, String>> datas = new ArrayList<>();
}
