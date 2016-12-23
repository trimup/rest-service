package com.lihe.hundsunreply;

import lombok.Data;

/**
 * Created by trimup on 2016/7/28.]
 * 恒生 问腿选项
 */
@Data
public class HSQuesOption {

    private String  option_content   ; //选项内容
    private Integer  option_no        ; //选项编号
    private Integer  option_order     ; //选项顺序
    private Double option_score     ; //选项得分
    private String  option_state     ; //选项状态
    private Integer  question_no      ; //问题编号

}
