package com.primelife.authentication.bean;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class AmazonCognito {

    @Value("${amazon.cognito.poolId}")
    public String poolId;

    @Value("${amazon.cognito.url}")
    public String url;

    @Value("${amazon.cognito.clientId}")
    public String clientId;

    @Value("${amazon.cognito.region}")
    public String region;
}
