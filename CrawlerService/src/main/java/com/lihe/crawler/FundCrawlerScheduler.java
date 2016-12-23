/*
 * Copyright (c) 2016. lihe-fund All Rights Reserved.
 */

package com.lihe.crawler;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author leo
 * @version V1.0.0
 * @package com.qed.component
 * @date 16/7/13
 */
@Slf4j
@Component
public class FundCrawlerScheduler {

    private final WebCrawlerDayFactory fundWebCrawlerDayFactory;

    /**
     * crawlStorageFolder is a folder where intermediate crawl data is
     * stored.
     */
    @Value("${crawler_tmp_path:tmp_files}")
    String crawlStorageFolder = "target/tmp";
    @Value("${fund_codes:210012}")
    String[] crawlFundCodes = {};

    @Autowired
    public FundCrawlerScheduler(WebCrawlerDayFactory fundWebCrawlerDayFactory) {
        this.fundWebCrawlerDayFactory = fundWebCrawlerDayFactory;
    }


    /**
     * the top of every hour of every day.
     */
    //    @Scheduled(cron = "0 0 * * * *")
    @Scheduled(fixedRate = 60 * 60000)
    //    @Scheduled(fixedRate = 60000)
    public void crawlerByTime() {
        /**
         * Instantiate the controller for this crawl.
         */
        CrawlController controller = getCrawlController();
        /**
         * For each crawl, you need to add some seed urls. These are the first
         * URLs that are fetched and then the crawler starts following links
         * which are found in these pages
         */
        if (crawlFundCodes == null || crawlFundCodes.length == 0) {
            controller.addSeed("http://fund.eastmoney.com/fund.html");
        } else {
            log.info("crawler fund codes=>" + Arrays.toString(crawlFundCodes));
            Arrays.asList(crawlFundCodes).parallelStream()
                .forEach(code -> controller.addSeed("http://fund.eastmoney.com/" + code + ".html"));
        }

        /**
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
        controller.startNonBlocking(fundWebCrawlerDayFactory, 1);
    }

    @Bean
    public CrawlController getCrawlController() {

        CrawlConfig config = getCrawlConfig();
        /**
         * Instantiate the controller for this crawl.
         */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = null;
        try {
            controller = new CrawlController(config, pageFetcher, robotstxtServer);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return controller;
    }


    private CrawlConfig getCrawlConfig() {
        CrawlConfig config = new CrawlConfig();

        config.setCrawlStorageFolder(crawlStorageFolder);
        /**
         * Be polite: Make sure that we don't send more than 1 request per
         * second (1000 milliseconds between requests).
         */
        config.setPolitenessDelay(1000);

        /**
         * You can set the maximum crawl depth here. The default value is -1 for
         * unlimited depth
         */
        config.setMaxDepthOfCrawling(1);

        /**
         * You can set the maximum number of pages to crawl. The default value
         * is -1 for unlimited number of pages
         */
        config.setMaxPagesToFetch(1000);

        /**
         * Do you want crawler4j to crawl also binary data ?
         * example: the contents of pdf, or the metadata of images etc
         */
        config.setIncludeBinaryContentInCrawling(false);

        /**
         * Do you need to set a proxy? If so, you can use:
         * config.setProxyHost("proxyserver.example.com");
         * config.setProxyPort(8080);
         *
         * If your proxy also needs authentication:
         * config.setProxyUsername(username); config.getProxyPassword(password);
         */

        config.setUserAgentString("BaiduSpider");
        /**
         * This config parameter can be used to set your crawl to be resumable
         * (meaning that you can resume the crawl from a previously
         * interrupted/crashed crawl). Note: if you enable resuming feature and
         * want to start a fresh crawl, you need to delete the contents of
         * rootFolder manually.
         */
        config.setResumableCrawling(false);
        return config;
    }
}
