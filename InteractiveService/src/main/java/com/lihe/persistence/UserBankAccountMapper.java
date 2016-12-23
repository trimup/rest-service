package com.lihe.persistence;

import com.lihe.entity.BankAccountInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by trimup on 2016/7/28.
 */
public interface UserBankAccountMapper {


    @Select("select * from lh_app_user_bank_account where user_tid =#{user_tid} ")
    public List<BankAccountInfo>  queryUserBankAccountByUserId(@Param("user_tid") Integer user_tid);
}
