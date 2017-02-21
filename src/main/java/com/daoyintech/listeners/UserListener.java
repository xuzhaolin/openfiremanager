package com.daoyintech.listeners;

import com.daoyintech.services.rest.UserRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by xuzhaolin on 2017/2/21.
 */
@Component
public class UserListener {

    @Autowired
    private UserRestService userRestService;

    @JmsListener(destination = "user_register", containerFactory = "gameFactory")
    public void getNewLottery(Map<String,String> params){
        userRestService.createUser(params);
    }


}
