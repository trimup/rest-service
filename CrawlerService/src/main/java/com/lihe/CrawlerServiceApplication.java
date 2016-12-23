/*
 * Copyright (c) 2016. lihe-fund All Rights Reserved.
 */

package com.lihe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

/**
 * 爬虫服务
 *
 * @author leo
 * @version V1.0.0
 * @package com.lihe
 * @date 8/25/16
 */
@EnableAsync
@SpringBootApplication
@EnableDiscoveryClient
public class CrawlerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrawlerServiceApplication.class, args);
    }


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
