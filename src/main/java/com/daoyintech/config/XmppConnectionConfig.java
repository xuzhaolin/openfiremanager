package com.daoyintech.config;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jxmpp.stringprep.XmppStringprepException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 设置xmpp Connection的部分属性
 * Created by xuzhaolin on 2017/2/16.
 */
@Configuration
public class XmppConnectionConfig {

    @Value("${xmpp.ip}")
    private String ip;

    @Value("${xmpp.domain}")
    private String domain;

    @Value("${xmpp.user.username}")
    private String name;

    @Value("${xmpp.user.password}")
    private String password;

    @Bean
    public XMPPTCPConnectionConfiguration xmppTcpConnectionConfiguration() throws XmppStringprepException {
        XMPPTCPConnectionConfiguration configuration = XMPPTCPConnectionConfiguration.builder()
                .setUsernameAndPassword(name,password)
                .setHost(ip)
                .setConnectTimeout(3000)
                .setXmppDomain(domain)
                .setDebuggerEnabled(true)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .build();
        return configuration;
    }


    @Bean
    public XMPPTCPConnection xmpptcpConnection(@Qualifier("xmppTcpConnectionConfiguration") XMPPTCPConnectionConfiguration xmppTcpConnectionConfiguration){
        XMPPTCPConnection connection = new XMPPTCPConnection(xmppTcpConnectionConfiguration);
        try {
            connection.setPacketReplyTimeout(20000);
            connection.connect().login();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    @Bean
    public ChatManager chatManager(@Qualifier("xmpptcpConnection") XMPPTCPConnection xmpptcpConnection){
        ChatManager chatManager =  ChatManager.getInstanceFor(xmpptcpConnection);
        return chatManager;
    }

    @Bean
    public MultiUserChatManager multiUserChatManager(@Qualifier("xmpptcpConnection") XMPPTCPConnection xmpptcpConnection){
        MultiUserChatManager chatManager = MultiUserChatManager.getInstanceFor(xmpptcpConnection);
        return chatManager;
    }
}
