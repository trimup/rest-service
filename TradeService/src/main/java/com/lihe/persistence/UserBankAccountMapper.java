package com.lihe.persistence;

import com.lihe.entity.UserBankAccount;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by trimup on 2016/7/27.
 */
public interface UserBankAccountMapper {

    @Select("SELECT * FROM `lh_app_user_bank_account` WHERE `user_tid` = #{user_tid}")
    public List<UserBankAccount> queryAccountByUserId(@Param("user_tid") Integer user_tid);

    @Select("SELECT * FROM `lh_app_user_bank_account` WHERE `id` = #{id}")
    public UserBankAccount findUserBankAccountById(@Param("id") Integer id);

}
