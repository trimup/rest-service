package com.lihe.hundSunreply;

import lombok.Data;

import java.util.List;

/**
 * Created by trimup on 2016/10/14.
 */
@Data
public class TradeConfirmReply {
    private String  code	      ;// 成功：ETS-5BP0000失败：其他
	private String  message	      ;//返回错误信息	S	60	0	N	v4.0.0.0
    private String  details;
    private List<TradeconfirmBean> tradeConfirmQuerys;//
    private Integer  rowcount	  ;//记录数	N	10	0	N	v4.0.0.0
    private Integer  total_count	  ;//总记录数
}
