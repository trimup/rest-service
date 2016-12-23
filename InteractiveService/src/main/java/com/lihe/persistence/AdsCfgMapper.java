package com.lihe.persistence;

import com.lihe.pojo.AdsCfgPojo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by trimup on 2016/8/18.
 */
public interface AdsCfgMapper {


    /**
     *  查首页广告
     * @return
     */
    @Select(" select name,url,img,img_small from lh_advert  where start_time <=now() and " +
            " end_time >= now() and channel =1 and is_view=1 and  is_online =1 order by sort   ")
    public List<AdsCfgPojo> queryAdsCfg();
}
