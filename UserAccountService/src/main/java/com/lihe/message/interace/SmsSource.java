package com.lihe.message.interace;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * Created by trimup on 2016/12/15.
 */
public interface SmsSource {
    String OUTPUT = "sms";

    @Output(SmsSource.OUTPUT)
    MessageChannel output();
}
