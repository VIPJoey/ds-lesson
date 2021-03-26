/*
 * Copyright (c) 2010-2020 Founder Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Founder. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with Founder.
 *
 */

package com.mmc.lesson.api.member;

import com.mmc.lesson.api.Result;

/**
 * Membervice.
 *
 * @author VIPJoey
 * @date 2021/3/26 4:05 下午
 */
public interface MemberApi {

    /**
     * 获取会员.
     */
    Result get(MemberInfo member);

    /**
     * 保存会员.
     */
    Result save(MemberInfo member);
}
