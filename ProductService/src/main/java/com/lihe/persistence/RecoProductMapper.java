package com.lihe.persistence;

import com.lihe.entity.RecommendProductInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by trimup on 2016/8/31.
 */
public interface RecoProductMapper {

    /**
     * 查询首页推荐项目
     * @param cate
     * @return
     */
    @Select("select * from lh_recommend where cate =#{cate} order by sort ")
    public List<RecommendProductInfo>  queryReProduct(@Param("cate")Integer cate);

}
