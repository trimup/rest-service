package com.lihe.persistence;


import com.lihe.entity.UserShareInfo;
import com.lihe.event.register.CreateShareLinkEvent;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

/**
 * Created by trimup on 2016/6/16.
 */
public interface UserShareActivityMapper {


    @Select("select count(1) from  lh_share_activity_info WHERE random=#{random}  AND share_user_tid=#{user_tid}  AND extend_type=#{extend_type}")
    public Integer checkSharelink(CreateShareLinkEvent event);

    @Insert("insert into lh_share_activity_info(share_user_tid," +
            "random,extend_type,end_time,create_time) values(#{share_user_tid},#{random},#{extend_type},#{end_time},now())")
    public void insertShareLink(UserShareInfo userShareInfo);
}
