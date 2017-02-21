package com.daoyintech.services.rest;

import com.daoyintech.config.XmppRestComponent;
import com.daoyintech.entities.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuzhaolin on 2017/2/16.
 */
@Service
public class GroupRestService {

    @Value("${xmpp.api.groups}")
    private String apiUrl;

    @Value("${xmpp.groups.member}")
    private String memberGroupName;

    @Value("${xmpp.groups.agent}")
    private String agentGroupName;

    @Value("${xmpp.groups.admin}")
    private String adminGroupName;

    public static final List<Group> groups = new ArrayList<>();


    @Autowired
    private XmppRestComponent xmppRestComponent;



//    针对初始化时需要创建的小组
    public void initGroups(){
        createGroup(memberGroupName,memberGroupName);
        createGroup(agentGroupName,agentGroupName);
        createGroup(adminGroupName,adminGroupName);
    }


    private void createGroup(String groupName,String description){
        Group group = new Group();
        group.setName(groupName);
        group.setDescription(description);
        xmppRestComponent.postRequest(apiUrl,group);
        groups.add(group);
    }



}
