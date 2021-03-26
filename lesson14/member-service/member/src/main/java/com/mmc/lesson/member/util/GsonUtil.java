/*
 * Copyright (c) 2010-2020 Founder Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Founder. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with Founder.
 *
 */

package com.mmc.lesson.member.util;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * GsonUtil.
 *
 * @author VIPJoey
 * @date 2021/3/2 2:54 下午
 */
public class GsonUtil {

    /**
     * 普通gson.
     */
    private static Gson gson = new Gson();

    /**
     * 下划线-驼峰 互转.
     */
    private static Gson specialGson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            //.registerTypeAdapter(Boolean.class, new BooleanSerializer())
            .disableHtmlEscaping()
            .create();

    /**
     * toJson.
     */
    public static String toJson(Object src) {

        return gson.toJson(src);
    }

    /**
     * toJson.
     */
    public static <T> T fromJsonSpeacial(String json, Class<T> jsonRootBeanClass) {
        return specialGson.fromJson(json, jsonRootBeanClass);
    }

    /**
     * 序列化对象，下划线转驼峰.
     */
    public static String toSpecialJson(Object source) {
        if (null == source) {
            return null;
        }
        return specialGson.toJson(source);
    }
}
