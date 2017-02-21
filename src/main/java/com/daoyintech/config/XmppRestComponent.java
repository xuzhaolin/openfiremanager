package com.daoyintech.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

/**
 *
 * openfire rest api 管理模板
 * Created by xuzhaolin on 2017/2/16.
 */
@Component
public class XmppRestComponent {
    @Autowired
    private RestTemplate xmppRestTemplate;

    @Autowired
    private HttpHeaders httpHeaders;


    @Value("${xmpp.ip}")
    private String ip;

    @Value("${xmpp.rest.port}")
    private String port;

    @Value("${xmpp.rest.prefix}")
    private String prefix;

    private String apiPrefixUrl;

    @PostConstruct
    public void initPrefixApiUrl() {
        this.apiPrefixUrl = new StringBuilder("http://").append(ip).append(":").append(port).append("/").append(prefix).toString();
    }

    public <T> T getRequest(String apiMethod, Class<T> t) {
        HttpEntity<String> request = new HttpEntity<>(httpHeaders);
        String url = new StringBuilder(apiPrefixUrl).append(apiMethod).toString();
        ResponseEntity<T> result = xmppRestTemplate.exchange(url, HttpMethod.GET,request,t);
        return result.getBody();
    }

    public Boolean postRequest(String apiMethod, Object object)  {
        String url = new StringBuilder(apiPrefixUrl).append(apiMethod).toString();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String objectStr =  objectMapper.writeValueAsString(object);
            HttpEntity<String> request = new HttpEntity<>(objectStr,httpHeaders);
            ResponseEntity<String> result = xmppRestTemplate.exchange(url,HttpMethod.POST,request,String.class);
            if(result.getStatusCodeValue() == 201)
                return true;

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (HttpClientErrorException e) {
            e.getRawStatusCode();
        }
        return false;
    }

    public Boolean deleteRequest(String apiMethod){
        String url = new StringBuilder(apiPrefixUrl).append(apiMethod).toString();
        HttpEntity<String> request = new HttpEntity<>(httpHeaders);
        try {
            ResponseEntity<String> result = xmppRestTemplate.exchange(url, HttpMethod.DELETE, request, String.class);
            if(result.getStatusCodeValue() == 200){
                return true;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public Boolean updateRequest(String apiMethod,Object object){
        String url = new StringBuilder(apiPrefixUrl).append(apiMethod).toString();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String objectStr =  objectMapper.writeValueAsString(object);
            HttpEntity<String> request = new HttpEntity<>(objectStr,httpHeaders);
            ResponseEntity<String> result = xmppRestTemplate.exchange(url,HttpMethod.PUT,request,String.class);
            if(result.getStatusCodeValue() == 200)
                return true;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (HttpClientErrorException e) {
            e.getRawStatusCode();
        }
        return false;
    }
}
