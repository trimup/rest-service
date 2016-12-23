package com.lihe.persistence;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by trimup on 2016/10/12.
 */
public interface EvalRiskLogMapper {


    @Select("select count(*) from  lh_app_risk_eval_log where user_tid =#{user_tid}  AND YEAR(create_time)=YEAR(NOW()) ")
    int countUserRiskEvalByYear(@Param("user_tid")Integer user_tid);


    @Insert("insert into  lh_app_risk_eval_log(user_tid,risk_level) values(#{user_tid},#{risk_level})")
    void insertUserRiskEval(@Param("user_tid")Integer user_tid,@Param("risk_level")String risk_level);
}
