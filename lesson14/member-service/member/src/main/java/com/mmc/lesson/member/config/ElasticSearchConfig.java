package com.mmc.lesson.member.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component("elasticSearchConfig")
@ConfigurationProperties(prefix = "elasticsearch")
@Data
public class ElasticSearchConfig {

    private String host;
    private int port;
    private String username;
    private String password;
    private String type;
    private String index;
}