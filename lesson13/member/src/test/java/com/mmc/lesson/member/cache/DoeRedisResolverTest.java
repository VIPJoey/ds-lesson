/*
 * Copyright (c) 2010-2020 Founder Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Founder. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with Founder.
 *
 */

package com.mmc.lesson.member.cache;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.mmc.lesson.member.model.TblMemberInfo;
import java.util.Date;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * DoeRedisResolverTest.
 *
 * @author VIPJoey
 * @date 2021/3/14 10:35 上午
 */
@Slf4j
@ActiveProfiles("dev")
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class DoeRedisResolverTest {

    @Resource
    private RedisResolver redisResolver;

    @Test
    void testAdd() {

        String key = "mmc:member:1";

        TblMemberInfo member = new TblMemberInfo();
        member.setUid(8888L);
        member.setUname("zhangsan");
        member.setUsex(1);
        member.setUbirth(new Date());
        member.setUtel("888");
        member.setUaddr("凌霄殿");
        member.setState(0);
        member.setDelFlag(0);
        member.setUphoto(null);

        redisResolver.set(key, member, 30);

        TblMemberInfo data = (TblMemberInfo) redisResolver.get(key);

        assertThat(data.getUname()).isEqualTo("zhangsan");
    }

}