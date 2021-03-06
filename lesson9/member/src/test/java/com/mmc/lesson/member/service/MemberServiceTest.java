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


import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmc.lesson.member.model.TblMemberInfo;
import java.io.IOException;
import java.util.Date;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.elasticsearch.common.inject.internal.ToStringBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;


/**
 * MemberServiceTest.
 *
 * @author Joey
 * @date 2021/2/25 10:42 上午
 */
@Slf4j
@ActiveProfiles("dev")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Resource
    private MemberService memberService;

    @Resource(name = "esObjectMapper")
    private ObjectMapper objectMapper;

    /**
     * add.
     */
    @Test
    public void testAdd() {

        TblMemberInfo member = new TblMemberInfo();
        member.setUname("zhangsan");
        member.setUsex(1);
        member.setUbirth(new Date());
        member.setUtel("888");
        member.setUaddr("凌霄殿");
        member.setState(0);
        member.setDelFlag(0);
        member.setUphoto(null);

        TblMemberInfo ret = memberService.save(member);

        assertThat(ret).isNotNull();
        log.info("--------------------------------------------------");
        log.info(ret.getUname());


    }


    @Test
    void getMemberFromEsById() throws IOException {

        TblMemberInfo member = new TblMemberInfo();
        member.setUid(1L);
        member.setUname("zhangsan");
        member.setUsex(1);
        member.setUbirth(new Date());
        member.setUtel("888");
        member.setUaddr("凌霄殿");
        member.setState(0);
        member.setDelFlag(0);
        member.setUphoto(null);

        memberService.saveMemberIntoEs(member.getUid() + "", member);

        TblMemberInfo ret = memberService.getMemberFromEsById(member.getUid() + "");
        log.info("--------------------------------------------------");
        log.info("result: {}", objectMapper.writeValueAsString(ret));

        assertThat(ret).isNotNull();
        assertThat(ret.getUname()).isEqualTo(member.getUname());
    }
}