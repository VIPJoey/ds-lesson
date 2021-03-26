/*
 * Copyright (c) 2010-2020 Founder Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Founder. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with Founder.
 *
 */

package com.mmc.lesson.member.kafka;

import com.mmc.lesson.member.context.Const;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * KafkaReceiver.
 *
 * @author VIPJoey
 * @date 2021/3/21 4:25 下午
 */
@Slf4j
//@Component
public class KafkaReceiver {


    @KafkaListener(id = "kafka-single-demo", topics = Const.KAFKA_SINGLE_DEMO_TOPIC)
    public void receiveMesage(ConsumerRecord<String, String> record) {

        if (null == record || StringUtils.hasText(record.value())) {

            log.warn("KafkaReceiver record is null or record.value is empty.");
            return;
        }

        String reqJson = record.value();
        log.info("KafkaReceiver {}", reqJson);


    }
}
