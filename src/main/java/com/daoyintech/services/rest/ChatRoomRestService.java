package com.daoyintech.services.rest;

import com.daoyintech.config.XmppRestComponent;
import com.daoyintech.entities.ChatRoom;
import com.daoyintech.entities.RoomLevel;
import com.daoyintech.entities.RoomType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * 涉及创建chatRoom,更新ChatRoom
 * Created by xuzhaolin on 2017/2/16.
 */
@Service
public class ChatRoomRestService {

    @Value("${xmpp.api.chatRooms}")
    private String apiUrl;

    @Autowired
    private XmppRestComponent xmppRestComponent;

    public static final List<ChatRoom> beijingRooms = new ArrayList<>();
    public static final List<ChatRoom> canadaRooms = new ArrayList<>();


    public void createChatRoom(ChatRoom room) {
        xmppRestComponent.postRequest(apiUrl,room);
    }




    public void initRooms() {
        for(RoomType roomType: RoomType.values()) {
            for(RoomLevel roomLevel: RoomLevel.values()) {
                List<ChatRoom> roomsList = new ArrayList<>();
                if(roomLevel == RoomLevel.high){
                     roomsList = createChatRooms(roomType,roomLevel,2);
                }else{
                    roomsList = createChatRooms(roomType,roomLevel,4);
                }
                if(roomType == RoomType.beijing){
                    beijingRooms.addAll(roomsList);
                }else{
                    canadaRooms.addAll(roomsList);
                }
            }
        }
    }

    private List<ChatRoom> createChatRooms(RoomType roomType,RoomLevel roomLevel,int count){
        List<ChatRoom> roomList = new ArrayList<>();
        String roomName = new StringBuilder(roomType.name()).append("-").append(roomLevel.name()).toString();
        StringBuilder showNameBuilder = new StringBuilder();
        switch (roomType) {
            case beijing:
                showNameBuilder.append("北京");
                break;
            default:
                showNameBuilder.append("加拿大");
        }
        showNameBuilder.append("-");
        switch (roomLevel) {
            case junior:
                showNameBuilder.append("初级");
                break;
            case medium:
                showNameBuilder.append("中级");
                break;
            case high:
                showNameBuilder.append("高级");
                break;
        }
        for(int i = 1;i<=count; i++){
            ChatRoom room = new ChatRoom();
            String buffer = new StringBuilder(showNameBuilder).append("-").append(i).toString();
            room.setRoomName(new StringBuilder(roomName).append("-").append(i).toString());
            room.setNaturalName(buffer);
            room.setDescription(buffer);
            roomList.add(room);
            createChatRoom(room);
        }
        return roomList;

    }



}
