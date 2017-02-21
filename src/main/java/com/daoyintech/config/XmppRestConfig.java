package com.daoyintech.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

/**
 * 设置openfire的RestAPI设置
 * Created by xuzhaolin on 2017/2/16.
 */
@Configuration
public class XmppRestConfig {
    @Value("${xmpp.secret}")
    private String secret;


    @Bean
    public RestTemplate xmppRestTemplate(@Qualifier("stringHttpMessageConverter") StringHttpMessageConverter stringHttpMessageConverter){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(0,stringHttpMessageConverter);
        return restTemplate;
    }

    @Bean
    public StringHttpMessageConverter stringHttpMessageConverter(){
        StringHttpMessageConverter stringHttpMessageConverter =  new StringHttpMessageConverter();
        stringHttpMessageConverter.setDefaultCharset(Charset.forName("UTF-8"));
        return stringHttpMessageConverter;
    }

    @Bean
    public HttpHeaders httpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(mediaType);
        headers.add("Authorization", secret);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        return headers;
    }
}
