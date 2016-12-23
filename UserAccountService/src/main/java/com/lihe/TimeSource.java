package com.lihe;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.core.MessageSource;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by trimup on 2016/12/6.
 */
@EnableBinding(Source.class)
@Service
@Slf4j
public class TimeSource {


   private String format="yyyyMMddHHmmss";


   @Bean
   @InboundChannelAdapter(value = Source.OUTPUT,poller = @Poller(fixedDelay = "5000", maxMessagesPerPoll = "1"))
   public MessageSource<String> timerMessageSource(){
       log.info("send"+"aaaBBBCCdd");
       return ()-> new GenericMessage<>("aaaBBBCCdd111");
   }


}
