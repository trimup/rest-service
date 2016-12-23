package com.lihe.Event;

import com.lihe.pojo.NoticePojo;
import lombok.Data;

import java.util.List;

/**
 * Created by trimup on 2016/9/13.
 */
@Data
public class QueryNoticeData {
    private int page;//第几页
    private int pageSize;//每页大小
    private long total;//总数
    private int totalPage;//总页数
    private List<NoticePojo> list;
}
