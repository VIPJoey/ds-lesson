/*
 * Copyright (c) 2010-2020 Founder Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Founder. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with Founder.
 *
 */

package com.mmc.lesson.member.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * RedisConfig.
 *
 * @author VIPJoey
 * @date 2021/3/14 10:22 上午
 */
@Component("redis")
@ConfigurationProperties(prefix = "redis")
@Data
public class RedisConfig {

    /**
     * 默认过期时间.
     */
    private int expired;

}
