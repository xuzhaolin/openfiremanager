package com.daoyintech.entities;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.daoyintech.entities.activemq.Lottery;
import org.jivesoftware.smack.packet.ExtensionElement;

/**
 * Created by xuzhaolin on 2017/2/21.
 */
@XStreamAlias("info")
public class XmppInfo implements ExtensionElement{

    private Lottery lottery;

    private MessageType messageType = MessageType.result;

    @Override
    public String getNamespace() {
        return "game";
    }

    @Override
    public String getElementName() {
        return "message";
    }

    @Override
    public CharSequence toXML() {
        XStream xstream = new XStream();
        xstream.setMode(XStream.NO_REFERENCES);
        xstream.alias("info",XmppInfo.class);
        return xstream.toXML(this);
    }

    public Lottery getLottery() {
        return lottery;
    }

    public void setLottery(Lottery lottery) {
        this.lottery = lottery;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }
}
