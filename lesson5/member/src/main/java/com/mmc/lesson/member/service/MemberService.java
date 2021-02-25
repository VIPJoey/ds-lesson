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

import com.mmc.lesson.member.mapper.TblMemberInfoMapper;
import com.mmc.lesson.member.model.TblMemberInfo;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * MemberService.
 *
 * @author tenkye
 * @date 2021/2/25 10:41 上午
 */
@Service
public class MemberService {

    @Resource
    private TblMemberInfoMapper tblMemberInfoMapper;

    public TblMemberInfo save(TblMemberInfo member) {

        int affect = tblMemberInfoMapper.insert(member);

        return member;
    }
}
