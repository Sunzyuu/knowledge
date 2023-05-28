package com.github.forest;

import com.github.forest.entity.User;
import com.github.forest.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class KnowledgeApplicationTests {

	@Resource
	private UserService userService;


	@Test
	void contextLoads() {
		List<User> list = userService.list();

		System.out.println(list);
	}

}
