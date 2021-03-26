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
import java.io.IOException;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * KafkaSenderTest.
 *
 * @author VIPJoey
 * @date 2021/3/21 4:37 下午
 */
@Slf4j
@ActiveProfiles("dev")
@ExtendWith(SpringExtension.class)
@SpringBootTest
class KafkaSenderTest {


    @Resource
    private KafkaSender kafkaSender;

    @Test
    void sendMessage() throws IOException {

        String json = "hello";

        for (int i = 0; i < 10; i++) {

            kafkaSender.sendMessage(Const.KAFKA_SINGLE_DEMO_TOPIC, json);

        }

        System.in.read();

    }

}