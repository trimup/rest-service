package com.lihe.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lihe.Event.QueryNoticeData;
import com.lihe.Event.QueryNoticeEvent;
import com.lihe.entity.NoticeInfo;
import com.lihe.persistence.NoticeMapper;
import com.lihe.pojo.NoticePojo;
import com.lihe.until.NumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by trimup on 2016/9/13.
 */
@Service
public class NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;
    private static final SimpleDateFormat Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public QueryNoticeData queryNoticeList(QueryNoticeEvent event){

        QueryNoticeData data =new QueryNoticeData();
        PageHelper.startPage(event.getPage(),event.getPageSize());
        List<NoticeInfo>  noticeInfos =noticeMapper.queryNotice(event.getType());
        List<NoticePojo> noticePojos =new ArrayList<>();
        for(NoticeInfo n :noticeInfos){
            NoticePojo p =new NoticePojo();
            p.setContent(n.getContent());
            p.setLogo(n.getLogo());
            p.setTitle(n.getTitle());
            p.setUrl(n.getUrl());
            p.setDate(Format.format(n.getUpdate_time()));
            noticePojos.add(p);
        }
        data.setList(noticePojos);
        data.setPage(event.getPage());
        data.setPageSize(event.getPageSize());
        long total =((Page) noticeInfos).getTotal();
        data.setTotal(total);
        data.setTotalPage(NumberUtil.Division(event.getPageSize(),total));
        return  data;


    }


}
