package com.lihe.hundsunreply;

import lombok.Data;

import java.util.List;

/**
 * Created by trimup on 2016/7/28.
 * 恒生问题
 */
@Data
public class HSQuestion {

    private String cust_type          ; //  客户类别
    private String fund_code          ; //  基金代码
    private String question_content   ; //  问题内容
    private Integer question_order     ; //  顺序号
    private String question_state     ; // 问题状态
    private String question_type      ; // 问题类型
    private Integer question_no;
    private List<HSQuesOption> questionOptions ;//问题选项

}
