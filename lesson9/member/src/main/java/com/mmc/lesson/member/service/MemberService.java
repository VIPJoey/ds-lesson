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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmc.lesson.member.config.ElasticSearchConfig;
import com.mmc.lesson.member.mapper.TblMemberInfoMapper;
import com.mmc.lesson.member.model.TblMemberInfo;
import java.io.IOException;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Service;

/**
 * MemberService.
 *
 * @author VIPJoey
 * @date 2021/2/25 10:41 上午
 */
@Slf4j
@Service
public class MemberService {

    @Resource
    private TblMemberInfoMapper tblMemberInfoMapper;

    @Resource
    private ElasticSearchConfig elasticSearchConfig;

    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Resource(name = "esObjectMapper")
    private ObjectMapper objectMapper;

    public TblMemberInfo save(TblMemberInfo member) {

        tblMemberInfoMapper.insert(member);

        return member;
    }

    public void saveMemberIntoEs(String id, TblMemberInfo member) throws IOException {

        UpdateRequest req = new UpdateRequest(elasticSearchConfig.getIndex(),
                elasticSearchConfig.getType(), id);

        byte[] bytes;
        try {
            bytes = objectMapper.writeValueAsBytes(member);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            return;
        }
        req.doc(bytes, XContentType.JSON).docAsUpsert(true).retryOnConflict(3)
                .timeout(TimeValue.timeValueMinutes(2))
                .detectNoop(true);

        restHighLevelClient.update(req, RequestOptions.DEFAULT);
    }

    public TblMemberInfo getMemberFromEsById(String id) throws IOException {

        GetRequest req = new GetRequest(elasticSearchConfig.getIndex(), elasticSearchConfig.getType(), id);
        GetResponse respone = restHighLevelClient.get(req, RequestOptions.DEFAULT);

        return objectMapper.readValue(respone.getSourceAsString(), TblMemberInfo.class);
    }
}
