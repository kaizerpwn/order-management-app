package com.ibrahimokic.ordermanagement;

import com.ibrahimokic.ordermanagement.controller.console.UserConsoleController;
import com.ibrahimokic.ordermanagement.utils.Utils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class OrdermanagementApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(OrdermanagementApplication.class, args);
	}

	@Override
	public void run(String ...args) throws Exception {
		UserConsoleController.userMainForm();
	}

}
