/*
 * Copyright (c) 2010-2020 Founder Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Founder. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with Founder.
 *
 */

package com.mmc.lesson.api;

import lombok.Data;

/**
 * Result.
 *
 * @author VIPJoey
 * @date 2021/3/26 4:47 下午
 */
@Data
public class Result {

    private int code;

    private String message;

    private Object data;

    public static Result ok(Object data) {

        Result r = new Result();
        r.setCode(0);
        r.setMessage("SUCCESS");
        r.setData(data);
        return r;
    }

    public static Result fail(String message) {
        Result r = new Result();
        r.setCode(-1);
        r.setMessage(message);
        r.setData(null);
        return r;
    }
}
