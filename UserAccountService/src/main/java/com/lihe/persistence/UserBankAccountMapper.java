package com.lihe.persistence;

import com.lihe.entity.BankAccountInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by trimup on 2016/7/27.
 */
public interface UserBankAccountMapper {


    /**
     * 插入用户银行账户
     */
    @Insert("insert into lh_app_user_bank_account" +
            "(user_tid,bank_account,bank_account_name,branch_bank_no,bank_no,bank_name,id_kind_gb,id_no," +
            "telephone,password,trade_acco,ta_acco,capital_mode,capital_mode_name,detail_fund_way) " +
            "values(#{user_tid},#{bank_account},#{bank_account_name},#{branch_bank_no}," +
            "#{bank_no},#{bank_name},#{id_kind_gb},#{id_no},#{telephone}," +
            "#{password},#{trade_acco},#{ta_acco},#{capital_mode},#{capital_mode_name},#{detail_fund_way})")
    public void insertUserBankAccount(BankAccountInfo bankAccountInfo);


    @Select("select * from lh_app_user_bank_account where user_tid=#{user_tid}")
    public List<BankAccountInfo> queryAccountByUserId(@Param("user_tid") Integer user_tid);


    @Update("update lh_app_user_bank_account set password =#{pwd} where id =#{id}")
    public void updateUserAccountPwd(@Param("pwd")String pwd ,@Param("id") Integer id);


    /**
     * 更新该用户的所有银行卡交易密码
     * @param pwd
     * @param id
     */
    @Update("update lh_app_user_bank_account set password =#{pwd} where user_tid =#{user_tid}")
    public void updateTradePwdByUser(@Param("pwd")String pwd ,@Param("user_tid") Integer id);

}
