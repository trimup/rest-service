package com.lihe.Event;

import com.lihe.hundsunreply.HSQuestion;
import lombok.Data;

import java.util.List;

/**
 * Created by trimup on 2016/7/27.
 * 请求在线评测返回
 */
@Data
public class ReonlineJudgeReturn {
    private Integer  rowcount      ; //记录数
    private Integer  total_count   ; //总记录数
    private List<HSQuestion> questions       ; // 题目

}
