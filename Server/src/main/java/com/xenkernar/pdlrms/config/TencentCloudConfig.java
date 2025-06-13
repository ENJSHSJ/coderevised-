package com.xenkernar.pdlrms.config;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.nlp.v20190408.NlpClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


@Component
@Configuration
@ConfigurationProperties(prefix = "tencent.cloud")
public class TencentCloudConfig {
    private String secretId;
    private String secretKey;
    private String region;

    // Getters and Setters（必须）
    public String getSecretId() {
        return secretId;
    }

    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Bean
    public NlpClient nlpClient() {
        Credential cred = new Credential(secretId, secretKey);
        return new NlpClient(cred, region);
    }
}
