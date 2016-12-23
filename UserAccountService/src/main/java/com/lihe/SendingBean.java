package com.lihe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

/**
 * Created by trimup on 2016/12/12.
 */
@Component
public class SendingBean {

    private MessageChannel messageChannel;

    @Autowired
    public SendingBean(MessageChannel output) {
        this.messageChannel = output;
    }

    public void sayHello(String name) {
        messageChannel.send(MessageBuilder.withPayload(name).build());
    }
}
