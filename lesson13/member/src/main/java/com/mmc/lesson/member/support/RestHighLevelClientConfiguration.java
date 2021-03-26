package com.mmc.lesson.member.support;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mmc.lesson.member.config.ElasticSearchConfig;
import java.text.SimpleDateFormat;
import javax.annotation.Resource;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestHighLevelClientConfiguration {

    @Resource
    private ElasticSearchConfig elasticSearchConfig;

    @Bean("esObjectMapper")
    public ObjectMapper getEsObjectMapper() {
        return new ObjectMapper()
                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
                // .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Bean
    RestHighLevelClient restHighLevelClient() {

        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(elasticSearchConfig.getUsername(), elasticSearchConfig.getPassword()));

        RestClientBuilder builder = RestClient
                .builder(new HttpHost(elasticSearchConfig.getHost(), elasticSearchConfig.getPort()))
                .setHttpClientConfigCallback(httpClientBuilder -> {
                    RequestConfig.Builder requestConfigBuilder = RequestConfig.custom()
                            // 超时时间
                            .setConnectTimeout(5 * 1000)
                            // 这就是Socket超时时间设置
                            .setSocketTimeout(5 * 1000);
                    httpClientBuilder
                            .setDefaultCredentialsProvider(credentialsProvider)
                            .setDefaultRequestConfig(requestConfigBuilder.build());
                    return httpClientBuilder;

                });

        return new RestHighLevelClient(builder);
    }
}