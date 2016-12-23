package com.lihe.service;

import com.lihe.persistence.AdsCfgMapper;
import com.lihe.pojo.AdsCfgPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by trimup on 2016/8/18.
 */
@Service
public class AdsCfgService {




    @Autowired
    private AdsCfgMapper adsCfgMapper;



    public List<AdsCfgPojo> getHomeAdsCfg(){
        return  adsCfgMapper.queryAdsCfg();
    }
}
