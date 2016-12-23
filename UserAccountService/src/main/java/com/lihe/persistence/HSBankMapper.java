package com.lihe.persistence;

import com.lihe.entity.HSBankCodeInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by trimup on 2016/8/22.
 */
public interface HSBankMapper {



    @Select("select * from lh_hs_bank_info where hs_code =#{hs_code} and status =1")
    public HSBankCodeInfo  queryHsBankCode(@Param("hs_code")String hs_code);

    @Select("select * from  lh_hs_bank_info where status =1")
    public List<HSBankCodeInfo> getAllBankCode();
 }
