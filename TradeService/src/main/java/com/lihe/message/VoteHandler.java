package com.lihe.message;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.server.quorum.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.handler.annotation.Payload;

/**
 * Created by trimup on 2016/12/12.
 */
@Slf4j
@EnableBinding(Sink.class)
public class VoteHandler {

//      spring Integration   写法
//    @ServiceActivator(inputChannel=Sink.INPUT)
//    public void loggerSink(Object payload) {
//        log.info("Received: " + payload);
//    }


    //spring cloud stream 写法 Complementary to its Spring Integration support,
    // Spring Cloud Stream provides its own @StreamListener annotation,
    // modeled after other Spring Messaging annotations
    // (e.g. @MessageMapping, @JmsListener, @RabbitListener, etc.).
    // The @StreamListener annotation provides a simpler model for handling inbound messages,
    // especially when dealing with use cases that involve content type management and type coercion.
    @StreamListener(Sink.INPUT)
    public void handle(String message) {
        log.info(message);
    }
}
