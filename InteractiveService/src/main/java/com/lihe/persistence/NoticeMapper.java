package com.lihe.persistence;

import com.lihe.entity.NoticeInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by trimup on 2016/9/13.
 */
public interface NoticeMapper {


    @Select("select * from  lh_notice  where start_time <=now() and end_time >= now() and status=1 and type=#{type} order by update_time desc ")
    public List<NoticeInfo> queryNotice(@Param("type")Integer type);
}
