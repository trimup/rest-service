package com.lihe.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lihe.common.Constant;
import com.lihe.common.Msg;
import com.lihe.entity.FaRelationInfo;
import com.lihe.entity.FianPlanInfo;
import com.lihe.event.FinaPlan.ConcernedFinaPlanEvent;
import com.lihe.event.FinaPlan.FinalPlanData;
import com.lihe.event.FinaPlan.QueryFinalPlanEvent;
import com.lihe.persistence.FaRelationMapper;
import com.lihe.persistence.UserInfoMapper;
import com.lihe.pojo.FPDetailPojo;
import com.lihe.pojo.FPListPojo;
import com.lihe.until.NumberUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trimup on 2016/9/27.
 */
@Service
public class FinancialPlannerService {

    private static final Logger L = LoggerFactory.getLogger(FinancialPlannerService.class);


    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private FaRelationMapper faRelationMapper;

    /**
     * 获取所有的理财经理
     * @return
     */
    public FinalPlanData getAllFinaPlan(QueryFinalPlanEvent event){
        FinalPlanData data =new FinalPlanData();
        PageHelper.startPage(event.getPage(),event.getPageSize());
        List<FianPlanInfo> fianPlanInfos =userInfoMapper.queryAllLhUser();
        List<FPListPojo> fpListPojos =new ArrayList<>();
        for(FianPlanInfo f :fianPlanInfos){
            FPListPojo p =new FPListPojo();
            p.setFp_id(f.getId());
            p.setReal_name(f.getReal_name());
            p.setUser_logo(f.getUser_logo());
            fpListPojos.add(p);
        }
        long total =((Page)fianPlanInfos).getTotal();
        data.setList(fpListPojos);
        data.setPage(event.getPage());
        data.setPageSize(event.getPageSize());
        data.setTotal(total);
        data.setTotalPage(NumberUtil.Division(event.getPageSize(),total));
        return  data;
    }


    /**
     * 获取所有的理财经理
     * @return
     */
    public FPDetailPojo getFinaPlanDetail(Integer fp_id){
        FinalPlanData data =new FinalPlanData();

        FianPlanInfo fianPlanInfo =userInfoMapper.queryLhUserById(fp_id);
        FPDetailPojo fpDetailPojo =new FPDetailPojo();
        fpDetailPojo.setReal_name(fianPlanInfo.getReal_name());
        fpDetailPojo.setUser_introduce(fianPlanInfo.getUser_introduce());
        fpDetailPojo.setUser_logo(fianPlanInfo.getUser_logo());
        fpDetailPojo.setTelephone(fianPlanInfo.getTelephone());

        return  fpDetailPojo;
    }


    public Msg  concernedFinaPlan(ConcernedFinaPlanEvent event){
        if(faRelationMapper.countFaRelationByUserId(event.getUser_tid())>0)
            return new Msg(Constant.FAIL,"该用户已经关注过理财师");
        FaRelationInfo newRelation =new FaRelationInfo();
        newRelation.setFinancial_advisor_id(event.getFp_id());
        newRelation.setLh_app_user_id(event.getUser_tid());
        newRelation.setWidth(1);
        faRelationMapper.insertFaRealtion(newRelation);
        Msg msg =new Msg();
        msg.setCode(Constant.SUCCESS);
        msg.setMsg(Constant.SUCCESS_MSG);
        return  msg;
    }


}
