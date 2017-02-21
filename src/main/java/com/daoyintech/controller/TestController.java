package com.daoyintech.controller;

import com.daoyintech.entities.PlayType;
import com.daoyintech.entities.activemq.Lottery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by xuzhaolin on 2017/2/21.
 */
@RestController
@RequestMapping("/send")
public class TestController {

    @Autowired
    private JmsTemplate jmsTemplate;

    @RequestMapping(value = "/",method = RequestMethod.GET)
    public void sendMessage(){
        Lottery lottery = new Lottery();
        lottery.setIssue("2132131");
        lottery.setResultCodes("13231231");
        lottery.setResultAt("21312321312");
        lottery.setPlayType(PlayType.BeiJing);
        jmsTemplate.convertAndSend("get_lottery_success",lottery);
    }
}
