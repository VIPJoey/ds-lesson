/*
 * Copyright (c) 2010-2020 Founder Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Founder. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with Founder.
 *
 */

package com.mmc.lesson.member.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mmc.lesson.api.Result;
import com.mmc.lesson.api.member.MemberApi;
import com.mmc.lesson.api.member.MemberInfo;
import com.mmc.lesson.member.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;


/**
 * DubboMemberServiceTest.
 *
 * @author VIPJoey
 * @date 2021/3/26 5:27 下午
 */
@Slf4j
@ActiveProfiles("dev")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class DubboMemberServiceTest {

    @Reference
    private MemberApi memberApi;

    @Test
    void testGet() {

        MemberInfo member = new MemberInfo();
        member.setUid(888L);

        Result ret = memberApi.get(member);
        log.info("--------------------------------------------------------");
        log.info("ret: {}", GsonUtil.toJson(ret));
    }

}
