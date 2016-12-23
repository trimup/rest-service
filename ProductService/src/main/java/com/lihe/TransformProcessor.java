package com.lihe;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.server.quorum.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.reactive.FluxSender;
import org.springframework.messaging.handler.annotation.SendTo;
import reactor.core.publisher.Flux;

/**
 * Created by trimup on 2016/12/13.
 */
@EnableBinding(Processor.class)
@Slf4j
public class TransformProcessor {

    //spring Integration   写法
//    @Transformer(inputChannel = Processor.INPUT, outputChannel = Processor.OUTPUT)
//    public Object transform(String message) {
//        logger.info("transform is beginning");
//        return message+"transform";
//    }


 //spring cloud stream  写法  The distinction between @StreamListener and a Spring Integration @ServiceActivator
 // is seen when considering an inbound Message
 // that has a String payload and a contentType header of application/json.
 // In the case of @StreamListener, the MessageConverter mechanism will use the contentType
 // header to parse the String payload into a Vote object.
 // As with other Spring Messaging methods,
 // method arguments can be annotated with @Payload, @Headers and @Header.
     @StreamListener
     public void receive(@Input(Processor.INPUT) Flux<String> input,
                         @Output(Processor.OUTPUT) FluxSender output) {
         output.send(input.map(s -> "aaa"));
     }





}
