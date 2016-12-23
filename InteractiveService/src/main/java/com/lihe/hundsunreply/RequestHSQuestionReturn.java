package com.lihe.hundsunreply;

import com.google.common.base.Strings;
import lombok.Data;

import java.util.List;

/**
 * Created by trimup on 2016/7/28.
 */
@Data
public class RequestHSQuestionReturn {
    private String  code          ;  //返回错误码  成功：ETS-5BP0000失败：其他
    private String  message       ; //返回错误信息
    private Integer  rowcount      ; //记录数
    private Integer  total_count   ; //总记录数
    private List<HSQuestion> questions       ; // 题目


}
