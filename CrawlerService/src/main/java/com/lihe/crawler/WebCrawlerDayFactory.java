/*
 * Copyright (c) 2016. lihe-fund All Rights Reserved.
 */

package com.lihe.crawler;

import edu.uci.ics.crawler4j.crawler.CrawlController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author leo
 * @version V1.0.0
 * @package com.lihe.crawler
 * @date 8/26/16
 */
@Component
public class WebCrawlerDayFactory<T extends BaseCrawler>
    implements CrawlController.WebCrawlerFactory {

    @Autowired
    private ApplicationContext context;

    @Override
    public T newInstance() throws Exception {
        return (T) context.getBean(OneDayCrawler.class);
    }

}
