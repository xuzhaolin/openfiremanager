package com.daoyintech;

import com.daoyintech.services.rest.ChatRoomRestService;
import com.daoyintech.services.rest.GroupRestService;
import com.daoyintech.services.rest.RoleRestService;
import com.daoyintech.services.rest.UserRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class Application implements CommandLineRunner{

	@Autowired
	private GroupRestService groupRestService;

	@Autowired
	private ChatRoomRestService chatRoomRestService;

	@Autowired
	private RoleRestService roleRestService;

	@Autowired
	private UserRestService userRestService;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		groupRestService.initGroups();
		chatRoomRestService.initRooms();
		roleRestService.bindAdminRoleToChatRooms();
		roleRestService.bindMembersRoleToChatRooms();
		userRestService.initAdmin();
		Map<String,String> params = new HashMap<>();
		params.put("password","15828050106");
		params.put("username","xuzhaolin");
		userRestService.createUser(params);
	}
}
