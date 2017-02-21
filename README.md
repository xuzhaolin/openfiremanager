# 28game openfire

## 关于设置部分
注意安装了openfire后请使用restAPI进行安装，另外openfire版本使用最新4.1.1
smack使用4.2.3 及最新版本
## 关于配置说明:
application.yml
```yaml
xmpp:
  api:(关于restApi请求地址)
    users: '/users'
    chatRooms: '/chatrooms'
    messages: '/messages/users'
    groups: '/groups'
    group: '/group'
  groups:（关于初始化的三个用户组)
    member: 'normal'
    agent: 'agent'
    admin: 'admin'
  ip: '127.0.0.1' (xmpp的服务地址)
  domain: 'openfirelocal.com' (xmpp的域名以及serviceName)
  secret: 'ueXm49NFTHv7785B' (用于rest请求认证)
  user:(系统通知用户)
    username: 'notification'
    password: '123456'
    nickname: '系统通知'
  rest:(rest端口以及请求地址)
    port: '9090'
    prefix: '/plugins/restapi/v1'
```
## 关于openfire设置
1. 请先新增2个用户 主要用于admin管理openfire，以及service用于系统通知
2. 运行程序时会初始化group以及chatrooms
## 初始化
在commandRunner中以实现
其中用户为测试用户
Application.java

```java
	   groupRestService.initGroups();
		chatRoomRestService.initRooms();
		roleRestService.bindAdminRoleToChatRooms();
		roleRestService.bindMembersRoleToChatRooms();
		userRestService.initAdmin();
		Map<String,String> params = new HashMap<>();
		params.put("password","15828050106");
		params.put("username","15828050106");
		userRestService.createUser(params);
```

### 关于rest设置
初始化rest_template
com.daoyintech.config.XmppRestConfig

```java
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
```
工具类方法

com.daoyintech.config.XmppRestCommponent

```java
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
```

## 关于xmppConnection链接设置
主要用于群发消息
com.daoyintech.config.XmppConnectionConfig

```java

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
```

## 群发消息
package com.daoyintech.services.XmppService;

```java
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
```

## activemq监听消息
1. 用于监听开奖消息(注意测试部分请参考controller中实现方式)

```java
@Component
public class LotteryListener {

   @Autowired
   private XmppService xmppService;

   @JmsListener(destination = "get_lottery_success", containerFactory = "gameFactory")
   public void getNewLottery(Lottery lottery){
      xmppService.sendMessage(lottery);
   }
}

```

2.用于监听用户注册事件，创建用户

```java
@Component
public class UserListener {

    @Autowired
    private UserRestService userRestService;

    @JmsListener(destination = "user_register", containerFactory = "gameFactory")
    public void getNewLottery(Map<String,String> params){
        userRestService.createUser(params);
    }


}

```

## 注意事项:
主要用于lottery通过activemq传递时为序列化对象，请注意不同程序间package一致，另外建议可以使用map对象来这个处理。



