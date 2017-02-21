package com.daoyintech.services;

import com.daoyintech.entities.ChatRoom;
import com.daoyintech.entities.PlayType;
import com.daoyintech.entities.XmppInfo;
import com.daoyintech.entities.activemq.Lottery;
import com.daoyintech.services.rest.ChatRoomRestService;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.packet.DataForm;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Resourcepart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xuzhaolin on 2017/2/21.
 */
@Service
public class XmppService {

    @Autowired
    private XMPPTCPConnection xmpptcpConnection;

    @Autowired
    private MultiUserChatManager multiUserChatManager;

    @Value("${xmpp.user.nickname}")
    private String nickName;

    public void sendMessage(Lottery lottery){
        XmppInfo xmppInfo = new XmppInfo();
        xmppInfo.setLottery(lottery);
        if(lottery.getPlayType() == PlayType.BeiJing){
            sendBeijingMessage(xmppInfo);
        }else{
            sendCanadamessage(xmppInfo);
        }
    }

    public void sendBeijingMessage(ExtensionElement element){
        sendChatRoomMessage(ChatRoomRestService.beijingRooms,element);
    }

    public void sendCanadamessage(ExtensionElement element){
        sendChatRoomMessage(ChatRoomRestService.canadaRooms,element);
    }


    private void sendChatRoomMessage(List<ChatRoom> rooms, ExtensionElement object){
        rooms.forEach(room -> {
            try {
                EntityBareJid jid = JidCreate.entityBareFrom(new StringBuilder(room.getRoomName()).append("@conference.").append(xmpptcpConnection.getServiceName().toString()));
                MultiUserChat multiUserChat = multiUserChatManager.getMultiUserChat(jid);

                joinChatRoom(multiUserChat);
                Message message = new Message(jid,Message.Type.groupchat);

                message.setBody("开奖结果");
                message.addExtension(object);
                multiUserChat.sendMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void joinChatRoom(MultiUserChat multiUserChat){
        try {
            multiUserChat.join(Resourcepart.from(nickName));
            multiUserChat.sendConfigurationForm(new Form(DataForm.Type.submit));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
