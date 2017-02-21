package com.daoyintech.listeners;

import com.daoyintech.entities.activemq.Lottery;
import com.daoyintech.services.XmppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;


/**
 * Created by xuzhaolin on 2017/2/21.
 */
@Component
public class LotteryListener {

   @Autowired
   private XmppService xmppService;

   @JmsListener(destination = "get_lottery_success", containerFactory = "gameFactory")
   public void getNewLottery(Lottery lottery){
      xmppService.sendMessage(lottery);
   }


}
