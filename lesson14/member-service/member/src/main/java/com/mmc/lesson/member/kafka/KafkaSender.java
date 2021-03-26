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

import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * KafkaSender.
 *
 * @author VIPJoey
 * @date 2021/3/21 4:22 下午
 */
@Slf4j
@Component
public class KafkaSender {

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 发送消息到kafka
     */
    public void sendMessage(String topic, String message) {
        log.info("sendMessage to kafka ,topic =[{}],message=[{}]", topic, message);
        kafkaTemplate.send(topic, message);
    }

    /**
     * 发送消息到kafka
     */
    public void sendMessage(String topic, String partionKey, String message) {
        log.info("sendMessage to kafka ,topic =[{}],partionKey=[{}],message=[{}]", topic, partionKey, message);
        kafkaTemplate.send(topic, partionKey, message);
    }

}
