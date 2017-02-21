package com.daoyintech.services.rest;

import com.daoyintech.config.XmppRestComponent;
import com.daoyintech.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by xuzhaolin on 2017/2/21.
 */
@Service
public class UserRestService {

    @Value("${xmpp.api.users}")
    private String usersUrl;

    @Value("${xmpp.api.groups}")
    private String groupsUrl;

    @Value("${xmpp.groups.member}")
    private String memberGroupName;

    @Value("${xmpp.groups.admin}")
    private String adminGroupName;

    @Value("${xmpp.user.username}")
    private String adminUserName;

    @Autowired
    private XmppRestComponent xmppRestComponent;


    public void initAdmin(){
        String url = new StringBuilder(usersUrl).append("/").append(adminUserName).append(groupsUrl).append("/").append(adminGroupName).toString();
        xmppRestComponent.postRequest(url,null);
    }

    public void createUser(Map<String,String> params){
        User user = new User();
        user.setPassword(params.get("password"));
        user.setUsername(params.get("username"));
        xmppRestComponent.postRequest(usersUrl,user);
        bindUserJoinGroup(user);
    }

    public void bindUserJoinGroup(User user){
        String url = new StringBuilder(usersUrl).append("/").append(user.getUsername()).append(groupsUrl).append("/").append(memberGroupName).toString();
        xmppRestComponent.postRequest(url,null);
    }


}
