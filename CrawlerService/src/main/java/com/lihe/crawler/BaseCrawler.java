/*
 * Copyright (c) 2016. lihe-fund All Rights Reserved.
 */

package com.lihe.crawler;

import com.lihe.service.CrawlerService;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.parser.ParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author leo
 * @version V1.0.0
 * @package com.qed.crawler
 * @date 16/7/13
 */
@Slf4j
public class BaseCrawler extends WebCrawler {
    private static final Pattern IMAGE_EXTENSIONS =
        Pattern.compile(".*\\.(bmp|gif|jpg|png|js|css)$");
    @Autowired RestTemplate restTemplate;
    @Autowired CrawlerService crawlerService;
    protected List<String> prefixs;

    /**
     * You should implement this function to specify whether the given url
     * should be crawled or not (based on your crawling logic).
     */
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        // Ignore the url if it has an extension that matches our defined set of image extensions.
        if (IMAGE_EXTENSIONS.matcher(href).matches()) {
            return false;
        }
        // Only accept the url if it is in the "fund.eastmoney.com" domain and protocol is "http".
        return shouldVisit(href);
    }

    protected boolean shouldVisit(String href) {
        return prefixs.stream().anyMatch(prefix -> href.startsWith(prefix));
    }

    /**
     * This function is called when a page is fetched and ready to be processed
     * by your program.
     */
    @Override
    public void visit(Page page) {
        int docid = page.getWebURL().getDocid();
        String url = page.getWebURL().getURL();
        String domain = page.getWebURL().getDomain();
        String path = page.getWebURL().getPath();
        String subDomain = page.getWebURL().getSubDomain();
        short depth = page.getWebURL().getDepth();
        logger.info("Docid: {}", docid);
//        logger.info("URL: {}", url);
//        logger.info("Domain: '{}'", domain);
//        logger.info("Sub-domain: '{}'", subDomain);
//        logger.info("Path: '{}'", path);
        logger.info("Depth: '{}'", depth);
        ParseData parseData = page.getParseData();
        if (parseData instanceof HtmlParseData) {
            parseData(page);
        } else {
            logger.info("Not Html: '{}'", parseData.toString());
        }
    }

    protected void parseData(Page page) {
    }
}
