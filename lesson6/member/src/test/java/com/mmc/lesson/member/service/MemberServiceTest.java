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


import com.mmc.lesson.member.model.TblMemberInfo;
import java.util.Date;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
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
//@Transactional
public class MemberServiceTest {

    @Resource
    private MemberService memberService;

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

        Assertions.assertThat(ret).isNotNull();
        log.info("--------------------------------------------------");
        log.info(ret.getUname());


    }

}