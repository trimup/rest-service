package com.lihe.hundsunreply;

import lombok.Data;

/**
 * Created by trimup on 2016/8/4.
 */
@Data
public class EvalResultReply {
    private String code       ;  //成功：ETS-5BP0000失败：其他
    private String invest_risk_tolerance ; //数据字典（现场可调整）：1安全型2保守型3稳健型4积极型5进取型
    private String message ;
    private String paper_id    ;

}
