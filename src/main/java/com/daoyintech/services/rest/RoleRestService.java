package com.daoyintech.services.rest;

import com.daoyintech.config.XmppRestComponent;
import com.daoyintech.entities.ChatRoom;
import com.daoyintech.entities.ChatRoomRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 将小组和聊天室绑定起来
 * Created by xuzhaolin on 2017/2/21.
 */
@Service
public class RoleRestService {

    @Value("${xmpp.api.chatRooms}")
    private String chatRoomsUrl;

    @Value("${xmpp.api.group}")
    private String groupUrl;


    @Value("${xmpp.groups.member}")
    private String memberGroupName;

    @Value("${xmpp.groups.agent}")
    private String agentGroupName;

    @Value("${xmpp.groups.admin}")
    private String adminGroupName;


    @Autowired
    private XmppRestComponent xmppRestComponent;


    public void initRoles(){
        bindAdminRoleToChatRooms();
        bindMembersRoleToChatRooms();
    }

    public void bindAdminRoleToChatRooms(){
        ChatRoomRestService.beijingRooms.forEach(room -> {
            bindGroupRoleToChatRoom(adminGroupName,room,ChatRoomRole.admins);
        });
        ChatRoomRestService.canadaRooms.forEach(room -> {
            bindGroupRoleToChatRoom(adminGroupName,room,ChatRoomRole.admins);
        });
    }

    public void bindMembersRoleToChatRooms(){
        ChatRoomRestService.beijingRooms.forEach(room -> {
            bindGroupRoleToChatRoom(memberGroupName,room,ChatRoomRole.members);
        });
        ChatRoomRestService.canadaRooms.forEach(room -> {
            bindGroupRoleToChatRoom(memberGroupName,room,ChatRoomRole.members);
        });
    }

    public void bindGroupRoleToChatRoom(String groupName, ChatRoom chatRoom, ChatRoomRole roomRole){
        String apiUrl =  new StringBuilder(chatRoomsUrl).append("/")
                            .append(chatRoom.getRoomName()).append("/").append(roomRole.name())
                            .append(groupUrl).append("/").append(groupName)
                            .toString();
        xmppRestComponent.postRequest(apiUrl,null);
    }
}
