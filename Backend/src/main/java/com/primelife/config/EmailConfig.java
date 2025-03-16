package com.primelife.config;


import com.primelife.exception.KeyVaultException;
import com.primelife.service.KeyFetchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

    @Autowired
    KeyFetchService keyFetchService;

    @Value("${spring.mail.host}")
    String host;

    @Value("${spring.mail.port}")
    int port;

    @Bean
    public JavaMailSender javaMailSender() throws KeyVaultException {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        String username = keyFetchService.fetchEmailUsername();
        String password = keyFetchService.fetchEmailPassword();

        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        mailSender.setJavaMailProperties(properties);
        return mailSender;
    }
}