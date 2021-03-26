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

import static com.sun.tools.internal.ws.wsdl.parser.Util.fail;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmc.lesson.api.Result;
import com.mmc.lesson.api.member.MemberApi;
import com.mmc.lesson.api.member.MemberInfo;
import com.mmc.lesson.member.config.ElasticSearchConfig;
import com.mmc.lesson.member.context.Const;
import com.mmc.lesson.member.kafka.KafkaSender;
import com.mmc.lesson.member.mapper.TblMemberInfoMapper;
import com.mmc.lesson.member.model.TblMemberInfo;
import java.io.IOException;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * MemberService.
 *
 * @author VIPJoey
 * @date 2021/2/25 10:41 上午
 */
@Slf4j
@Service
@com.alibaba.dubbo.config.annotation.Service(interfaceClass = MemberApi.class)
public class MemberService implements MemberApi {

    @Resource
    private TblMemberInfoMapper tblMemberInfoMapper;

    @Resource
    private ElasticSearchConfig elasticSearchConfig;

    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Resource(name = "esObjectMapper")
    private ObjectMapper objectMapper;

    @Resource
    private KafkaSender kafkaSender;

    @Override
    public Result get(MemberInfo member) {

        TblMemberInfo info = convertMember(member);
        TblMemberInfo data = get(info);
        if (null == data) {
            return Result.fail("会员不存在");
        }
        return Result.ok(convertMember(data));
    }

    @Override
    public Result save(MemberInfo member) {
        TblMemberInfo info = convertMember(member);
        TblMemberInfo data;
        try {
            data = save(info);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
        return Result.ok(convertMember(data));
    }

    private TblMemberInfo convertMember(MemberInfo member) {
        TblMemberInfo tblMemberInfo = new TblMemberInfo();
        tblMemberInfo.setUid(member.getUid());
        tblMemberInfo.setUname(member.getUname());
        tblMemberInfo.setUsex(member.getUsex());
        tblMemberInfo.setUbirth(member.getUbirth());
        tblMemberInfo.setUtel(member.getUtel());
        tblMemberInfo.setUaddr(member.getUaddr());
        tblMemberInfo.setCreateTime(member.getCreateTime());
        tblMemberInfo.setUpdateTime(member.getUpdateTime());
        tblMemberInfo.setState(member.getState());
        tblMemberInfo.setDelFlag(member.getDelFlag());
        tblMemberInfo.setUphoto(member.getUphoto());
        return tblMemberInfo;
    }

    private MemberInfo convertMember(TblMemberInfo data) {
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setUid(data.getUid());
        memberInfo.setUname(data.getUname());
        memberInfo.setUsex(data.getUsex());
        memberInfo.setUbirth(data.getUbirth());
        memberInfo.setUtel(data.getUtel());
        memberInfo.setUaddr(data.getUaddr());
        memberInfo.setCreateTime(data.getCreateTime());
        memberInfo.setUpdateTime(data.getUpdateTime());
        memberInfo.setState(data.getState());
        memberInfo.setDelFlag(data.getDelFlag());
        memberInfo.setUphoto(data.getUphoto());
        return memberInfo;
    }

    // 但数据有更新的时候使缓存失效，这里是为了举例，缓存数据一致性问题先不考虑
    @CacheEvict(key = "#member.uid", cacheNames = {Const.MMC_MEMBER_KEY_PREFIX})
    public TblMemberInfo save(TblMemberInfo member) throws JsonProcessingException {

        tblMemberInfoMapper.upsert(member);

        // 发送消息给下游系统，做解耦
        // 使用memberId作为分区，保证一定的消息时序性
        kafkaSender.sendMessage(Const.KAFKA_SINGLE_DEMO_TOPIC,
                member.getUid() + "", objectMapper.writeValueAsString(member));

        return member;
    }

    // 增加缓存，常量值为mmc:member，拼接后的key为mmc:member:id
    @Cacheable(key = "#member.uid", cacheNames = {Const.MMC_MEMBER_KEY_PREFIX})
    public TblMemberInfo get(TblMemberInfo member) {

        log.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!  load data from db.  !!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        return tblMemberInfoMapper.selectByPrimaryKey(member.getUid());
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
