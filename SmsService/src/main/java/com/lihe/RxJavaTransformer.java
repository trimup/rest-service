package com.lihe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.rxjava.EnableRxJavaProcessor;
import org.springframework.cloud.stream.annotation.rxjava.RxJavaProcessor;
import org.springframework.context.annotation.Bean;

import java.util.List;


/**
 * Created by trimup on 2016/12/6.
 */
@EnableRxJavaProcessor
public class RxJavaTransformer {

    private static Logger logger = LoggerFactory.getLogger(RxJavaTransformer.class);
    @Bean
    public RxJavaProcessor<String,String> processor() {
        return inputStream -> inputStream.map(data -> {
            logger.info("Got data = " + data);
            return data;
        }).buffer(5).map(data -> String.valueOf(avg(data)));
    }

    private static Double avg(List<String> data) {
        double sum = 0;
        double count = 0;
        for(String d : data) {
            count++;
            sum += Double.valueOf(d);
        }
        return sum/count;
    }
}
