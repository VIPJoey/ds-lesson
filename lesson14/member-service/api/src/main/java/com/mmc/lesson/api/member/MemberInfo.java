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

import java.util.Date;
import lombok.Data;

/**
 * MemberInfo.
 *
 * @author VIPJoey
 * @date 2021/3/26 4:07 下午
 */
@Data
public class MemberInfo {

    private Long uid;

    private String uname;

    private Integer usex;

    private Date ubirth;

    private String utel;

    private String uaddr;

    private Date createTime;

    private Date updateTime;

    private Integer state;

    private Integer delFlag;

    private byte[] uphoto;
}
