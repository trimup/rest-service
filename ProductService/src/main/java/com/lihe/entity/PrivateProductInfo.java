package com.lihe.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by trimup on 2016/8/12.
 * 私募基金项目
 */
@Data
public class PrivateProductInfo {
    private Integer   id                       ;// int(11) NOT NULL自增ID
    private String   product_code             ;// varchar(20) NULL项目编号
    private String   product_name             ;// varchar(100) NULL项目名称
    private Integer   product_type_id          ;// char(5) NULL项目类型，10027代表集合信托，10028代表集合资管，10029代表阳光私募，10030代表定向增发，10031代表PE/VC
    private String   product_tag_id           ;// varchar(100) NULL项目标签，10032代表热销，10033代表活动
    private Date     raise_start_time         ;// timestamp NOT NULL募集开始时间
    private Date   raise_end_time           ;// timestamp NOT NULL募集结束时间
    private Date   project_start_time       ;// timestamp NULL产品成立开始时间
    private Date   project_end_time         ;// timestamp NULL产品成立结束时间
    private Date   project_forecast_time    ;// timestamp NULL预计项目开始时间
    private Date   interest_time            ;// timestamp NULL计息时间
    private BigDecimal collect_count            ;// decimal(16,4) NULL项目融资规模
    private BigDecimal   real_collect_count       ;// decimal(16,4) NULL实际融资额度
    private BigDecimal   product_min_invest       ;// decimal(16,4) NULL最低投资额，该字段只对：集合信托和集合资管有用
    private BigDecimal   product_max_invest       ;// decimal(16,0) NULL最高投资额，该字段只对：集合信托和集合资管有用
    private String   province                 ;// varchar(20) NULL项目所在省份，该字段只对：集合信托和集合资管有用
    private String   city                     ;// varchar(20) NULL项目所在市区，该字段只对：集合信托和集合资管有用
    private String   profit_type_id           ;// char(5) NULL项目收益类型，该字段只对：集合信托和集合资管有用，10034代表保本固定收益，10035代表保本浮动收益，10036代表非保本浮动收益，10037代表固定收益
    private String   product_deadline         ;// varchar(20) NULL项目期限，按月为单位
    private String   product_rates_id         ;// varchar(30) NULL项目评级，该字段只对：集合信托和集合资管有用，10038代表AAA，10039代表AA，10040代表A，10041代表BBB，10042代表BB，10043代表B，10044代表CCC，10045代表CC，10046代表C，10047代表无评级
    private String   product_management_fees  ;// varchar(5) NULL项目管理费用，单位为：%，该费用针对：阳光私募，定向增发，PE/VC类型的项目有用
    private String   pay_interest_type_id     ;// char(5) NULL付息方式，该字段只对：集合信托和集合资管有用
    private String   invest_field_id          ;// char(5) NULL投资领域，该字段只对：集合信托和集合资管有用，10052代表工商企业类，10053代表金融市场类，10054代表基础设施类，10055代表房地产类，10056代表资金池，10057代表其他类
    private String   publisher_type_id        ;// char(5) NULL发行人类型，该字段只对：集合资管有用，10058代表基金子公司，10059代表券商资管，10060代表期货资管，10061代表保险资管，10062代表银行资管
    private String   publish_channel_id       ;// char(5) NULL发行通道，该字段只对：阳光私募，定向增发，PE/VC类型的项目有用，10063代表自主发行，10064代表信托，10065代表基金子公司，10066代表其他
    private String   management_orgvar        ;// char(100) NULL管理机构，该字段只对：阳光私募，定向增发，PE/VC类型的项目有用
    private String   fund_type_id             ;// char(5) NULL基金类型，该字段只对：阳光私募，定向增发，PE/VC类型的项目有用，10067代表基础设施，10068代表房地产，10069代表工商企业，10070代表金融市场
    private BigDecimal   revenue_share            ;// float(8,4) NULL收益分成，单位：%，该费用针对：阳光私募，定向增发，PE/VC类型的项目有用
    private Date   product_open_time        ;// date NULL项目开放日期，该类型只对：定向增发，PE/VC类型的项目有用
    private String   mortgage_rate_id         ;// char(5) NULL项目的抵押率，该字段只对：信托和资管类型的项目起作用，10071代表30%以下，10072代表30%-50%，10073代表50%以上，10074代表无抵押
    private String   size_valuevar            ;// char(50) NULL严格配比时填写的值
    private String   size_match_id            ;// char(5) NULL大小配比，该字段只对：信托和资管类型的项目起作用，10075代表小额畅打，10076代表已配出小额，10077代表严格配比，10078代表全大额
    private String   product_status_id        ;// char(5) NULL项目状态，10079代表提交中，10080代表不通过，10081代表预热中，10082代表募资中，10083代表进行中，10084代表已退出
    private String   process_descvar          ;// char(255) NULL项目进度说明
    private String   platform_comment         ;// varchar(255) NULL平台点评，该字段只对：信托和资管类型的项目起作用
    private String   is_structured            ;// char(1) NULL是否结构化，该字段针对：阳光私募，定向增发，PE/VC类型的项目有用，1代表是，2代表非，
    private String   raise_entity             ;// varchar(100) NULL，该字段只对：信托和资管类型的项目起作用
    private String   publish_agency           ;// varchar(100) NULL发行机构，该字段只对：信托和资管类型的项目起作用
    private String   rish_test_type_id        ;// char(5) NULL风险评测类型，10085代表保守型，10086代表安稳型，10087代表稳健型，10088代表成长型，10089代表积极型
    private String   product_rish_type_id     ;// char(5) NULL项目风险等级，10090代表低，10091代表中低及以下，10092代表中等及以下，10093代表中高及以下，10094代表高及以下
    private String   raise_capital_account    ;// text NULL募集资金账户
    private String   fund_uses                ;// text NULL资金用途
    private String   rish_contro_step         ;// text NULL风控措施
    private String   product_excellence       ;// text NULL项目亮点
    private String   memo                     ;// text NULL项目补充说明
    private String   product_detail_info      ;// text NULL项目资料
    private Date     create_time              ;// timestamp NOT NULL项目创建时间
    private String   raise                    ;// varchar(200) NOT NULL融资主体
    private String   payment_resource         ;// text NULL还款来源
    private Integer   scoremoney               ;// int(11) NULL分数资金
    private Integer   score                    ;// int(11) NULL分数
}
