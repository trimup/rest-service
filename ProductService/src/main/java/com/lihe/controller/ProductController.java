package com.lihe.controller;

import com.lihe.common.Constant;
import com.lihe.common.Msg;
import com.lihe.event.*;
import com.lihe.pojo.FixIncomeDetailPojo;
import com.lihe.pojo.PrPlaceDetailPojo;
import com.lihe.pojo.productDetail.SunProductPojo;
import com.lihe.service.HSProductService;
import com.lihe.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by trimup on 2016/8/15.
 */
@RestController
@RequestMapping(value = "/lihe/product/product")
public class ProductController {
    private static final Logger L = LoggerFactory.getLogger(ProductController.class);

    private static final SimpleDateFormat Format = new SimpleDateFormat("yyyyMMddHHmmss");


    @Autowired
    private HSProductService hsProductService;
    @Autowired
    private UserService userService;


    /**
     * 查询公募基金项目列表
     *
     * @param event
     * @return
     */
    @ApiOperation(value = "查询公募基金项目列表")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "公募基金列表信息", response = HSProListData.class)
    })
    @RequestMapping(value = "/queryPublicProduct", method = {RequestMethod.POST})
    public Msg queryPublicProduct(@RequestBody QueryHSProEvent event) {
        if (event.isCheck())
            return new Msg(Constant.FAIL, "参数错误");
        Msg msg = new Msg();
        HSProListData data = hsProductService.queryPublicProduct(event);
        msg.setData(data);
        msg.setCode(Constant.SUCCESS);
        msg.setMsg(Constant.SUCCESS_MSG);
        return msg;
    }


    /**
     * 查询固定收益项目列表
     *
     * @param event
     * @return
     */
    @ApiOperation(value = "查询固定收益项目列表")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "固定收益项目列表信息", response = FixedIncomListData.class)
    })
    @RequestMapping(value = "/queryFixedIncomeProduct", method = {RequestMethod.POST})
    public Msg queryFixedIncomeProduct(@RequestBody QueryFixIncomEvent event) {
        Msg msg = new Msg();
        FixedIncomListData data = hsProductService.queryFixedIncomeProduct(event);
        msg.setData(data);
        msg.setCode(Constant.SUCCESS);
        msg.setMsg(Constant.SUCCESS_MSG);
        return msg;
    }

    /**
     * 查询阳光私募和定向增发项目列表
     */
    @ApiOperation(value = "查询阳光私募和定向增发项目列表")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "阳光私募和定向增发项目列表信息", response = SunAndDirListData.class)
    })
    @RequestMapping(value = "/querySunAndDirec", method = {RequestMethod.POST})
    public Msg querySunAndDirec(@RequestBody QuerySunAndDireEvent event) {
        if (event.isCheck())
            return new Msg(Constant.FAIL, "参数有误");
        Msg msg = new Msg();
        SunAndDirListData data = hsProductService.querySunAndDirec(event);
        msg.setData(data);
        msg.setCode(Constant.SUCCESS);
        msg.setMsg(Constant.SUCCESS_MSG);
        return msg;
    }

    /**
     * 查询固定收益详情
     */
    @ApiOperation(value = "查询固定收益详情")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "固定收益详情", response = FixIncomeDetailPojo.class)
    })
    @RequestMapping(value = "/queryFixedIncomeDetail", method = {RequestMethod.POST})
    public Msg queryFixedIncomeDetail(@RequestBody QueryDetailEvent event) {
        if (event.getProduct_id() == null)
            return new Msg(Constant.FAIL, "参数有误");
        Msg msg = new Msg();
        FixIncomeDetailPojo data = hsProductService.queryFixedIncomeDetail(event);
        msg.setData(data);
        msg.setCode(Constant.SUCCESS);
        msg.setMsg(Constant.SUCCESS_MSG);
        return msg;
    }


    /**
     * 查询固定收益详情
     */
    @ApiOperation(value = "查询阳光私募详情")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "查询阳光私募详情", response = SunProductPojo.class)
    })
    @RequestMapping(value = "/querySunProductDetail", method = {RequestMethod.POST})
    public Msg querySunProductDetail(@RequestBody QueryDetailEvent event) {
        if (event.getProduct_id() == null)
            return new Msg(Constant.FAIL, "参数有误");
        Msg msg = new Msg();
        SunProductPojo data = hsProductService.querySunProductDetail(event);
        msg.setData(data);
        msg.setCode(Constant.SUCCESS);
        msg.setMsg(Constant.SUCCESS_MSG);
        return msg;
    }

    /**
     * 获取公募基金净值趋势信息
     * @param event
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "getSunTrend" ,method = RequestMethod.POST)
    public Msg getSunTrend(@RequestBody QuerySunTrendEvent event) throws Exception{
        if(event.isCheck())
            return new Msg(Constant.FAIL,"参数有误");
        Msg msg  =hsProductService.getSunTrend(event.getProduct_id(),event.getTime_limit());
        return  msg;
    }

    /**
     * 查询定向增发详情
     */
    @ApiOperation(value = "查询定向增发详情")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "定向增发详情", response = PrPlaceDetailPojo.class)
    })
    @RequestMapping(value = "/queryPrPlaceDetail", method = {RequestMethod.POST})
    public Msg queryPrPlaceDetail(@RequestBody QueryDetailEvent event) {
        if (event.getProduct_id() == null)
            return new Msg(Constant.FAIL, "参数有误");
        Msg msg = new Msg();
        PrPlaceDetailPojo data = hsProductService.queryPrPlaceDetail(event);
        msg.setData(data);
        msg.setCode(Constant.SUCCESS);
        msg.setMsg(Constant.SUCCESS_MSG);
        return msg;
    }


    /**
     * 查询主页推荐项目
     * @return
     */
    @RequestMapping(value = "/queryHomeReProduct", method = {RequestMethod.POST})
    public Msg queryHomeReProduct() {

        Msg msg = new Msg();
        List homeList =hsProductService.queryHomeReProduct();
        msg.setCode(Constant.SUCCESS);
        msg.setData(homeList);
        msg.setMsg(Constant.SUCCESS_MSG);
        return msg;
    }


    /**
     * 查询主页推荐项目
     * @return
     */
    @RequestMapping(value = "/queryHotProduct", method = {RequestMethod.POST})
    public Msg queryHotProduct() {
        Msg msg = new Msg();
        List hotList =hsProductService.queryHotProduct();
        msg.setCode(Constant.SUCCESS);
        msg.setData(hotList);
        msg.setMsg(Constant.SUCCESS_MSG);
        return msg;
    }


    /**
     * 查询所有项目信息
     * @return
     */
    @RequestMapping(value = "/allSearchProduct", method = {RequestMethod.POST})
    public Msg allSearchProduct() {
        Msg msg = new Msg();
        List searchList =hsProductService.allSearchProduct();
        msg.setCode(Constant.SUCCESS);
        msg.setData(searchList);
        msg.setMsg(Constant.SUCCESS_MSG);
        return msg;
    }

    /**
     * 关注某项目
     */
    @RequestMapping(value = "/concernedProduct", method = {RequestMethod.POST})
    public  Msg concernedProduct(@RequestBody ConcProductEvent event){
        if(event.orCheck())
            return  new Msg(Constant.FAIL,"参数有误");
        Msg msg =userService.checkToken(event.getUser_tid(),event.getToken());
        if(msg.getCode() !=Constant.SUCCESS)
            return  msg;
        msg =hsProductService.concernedProduct(event);
        return msg;
    }


    /**
     * 关注的项目List
     */
    @RequestMapping(value = "/queryConcernedList", method = {RequestMethod.POST})
    public  Msg queryConcernedList(@RequestBody QueryConcernedListEvent event){
        if(event.orCheck())
            return  new Msg(Constant.FAIL,"参数有误");
        Msg msg =userService.checkToken(event.getUser_tid(),event.getToken());
        if(msg.getCode() !=Constant.SUCCESS)
            return  msg;
        List pojolist =new ArrayList<>();
        if(event.getType()==1){
            pojolist=hsProductService.queryPubConcernedList(event.getUser_tid());
        }else {
            pojolist=hsProductService.queryPriConcernedList(event.getUser_tid());
        }
        msg.setCode(Constant.SUCCESS);
        msg.setData(pojolist);
        msg.setMsg(Constant.SUCCESS_MSG);
        return msg;
    }

    /**
     * 移除关注
     */
    @RequestMapping(value = "/removeConcerned", method = {RequestMethod.POST})
    public  Msg removeConcerned( @RequestBody RemoveConcernedEvent event){
        if(event.orCheck())
            return  new Msg(Constant.FAIL,"参数有误");
        Msg msg =userService.checkToken(event.getUser_tid(),event.getToken());
        if(msg.getCode() !=Constant.SUCCESS)
            return  msg;
        hsProductService.removeConcerned(event.getRemoveId());
        msg.setCode(Constant.SUCCESS);
        msg.setMsg(Constant.SUCCESS_MSG);
        return msg;
    }


    /**
     * 移除关注
     */
    @RequestMapping(value = "/haveConcerned", method = {RequestMethod.POST})
    public  Msg haveConcerned( @RequestBody HaveConcernedEvent event){
        if(event.isCheck())
            return  new Msg(Constant.FAIL,"参数有误");
        Msg msg =userService.checkToken(event.getUser_tid(),event.getToken());
        if(msg.getCode() !=Constant.SUCCESS)
            return  msg;
        msg.setCode(Constant.SUCCESS);
        msg.setData(hsProductService.haveConcerned(event));
        msg.setMsg(Constant.SUCCESS_MSG);
        return msg;
    }



}
