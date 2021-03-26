/*
 * Copyright (c) 2010-2020 Founder Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Founder. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with Founder.
 *
 */

package com.mmc.lesson.member.support;

import static com.mmc.lesson.member.context.Const.MMC_MEMBER_KEY_PREFIX;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmc.lesson.member.config.RedisConfig;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis cache configuration.
 *
 * @author Joey
 * @date 2018/6/17 11:20
 */
@Configuration
@EnableCaching
public class RedisConfiguration extends CachingConfigurerSupport {

    @Resource
    private RedisConfig redisConfig;

    @Bean("redisKeyPrefix")
    public String redisKeyPrefix() {
        return MMC_MEMBER_KEY_PREFIX;
    }

    @Bean("redisTemplate")
    public RedisTemplate<?, ?> getRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<?, ?> template = new StringRedisTemplate(connectionFactory);
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        template.setKeySerializer(keySerializer);
        // Jackson2JsonRedisSerializer<?> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        // ObjectMapper om = new ObjectMapper();
        // om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        // jackson2JsonRedisSerializer.setObjectMapper(om);
        // template.setValueSerializer(new JdkSerializationRedisSerializer());
        template.setValueSerializer(createJacksonRedisSerializer());
        template.setHashKeySerializer(keySerializer);
        template.setHashValueSerializer(createJacksonRedisSerializer());
        return template;
    }

    // 缓存管理器
    @Bean("memberCacheManager")
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {

        // 生成一个默认配置，通过config对象即可对缓存进行自定义配置
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(redisConfig.getExpired())) // 设置缓存的默认过期时间，也是使用Duration设置
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(createJacksonRedisSerializer()))
                .disableCachingNullValues(); // 不缓存空值

        // 对每个缓存空间应用不同的配置
        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();
        redisCacheConfigurationMap.put(redisKeyPrefix(), config);

        // 初始化一个RedisCacheWriter
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory);

        // 初始化RedisCacheManager
        return new RedisCacheManager(redisCacheWriter, config, redisCacheConfigurationMap);

        /*
        RedisCacheManager cacheManager = RedisCacheManager.builder(redisConnectionFactory)     // 使用自定义的缓存配置初始化一个cacheManager
                .initialCacheNames(cacheNames)  // 注意这两句的调用顺序，一定要先调用该方法设置初始化的缓存名，再初始化相关的配置
                .withInitialCacheConfigurations(configMap)
                .build();
         */

    }


    private Jackson2JsonRedisSerializer<?> createJacksonRedisSerializer() {
        Jackson2JsonRedisSerializer<?> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        return jackson2JsonRedisSerializer;
    }


    @Bean
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            String[] value = new String[1];
            Cacheable cacheable = method.getAnnotation(Cacheable.class);
            if (cacheable != null) {
                value = cacheable.value();
            }
            CachePut cachePut = method.getAnnotation(CachePut.class);
            if (cachePut != null) {
                value = cachePut.value();
            }
            CacheEvict cacheEvict = method.getAnnotation(CacheEvict.class);
            if (cacheEvict != null) {
                value = cacheEvict.value();
            }
            sb.append(value[0]);
            for (Object obj : params) {
                sb.append(":")
                        .append(obj.toString());
            }
            return sb.toString();
        };
    }
}
