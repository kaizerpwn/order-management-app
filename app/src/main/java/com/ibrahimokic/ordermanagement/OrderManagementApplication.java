package com.ibrahimokic.ordermanagement;

import com.ibrahimokic.ordermanagement.controller.console.UserConsoleController;
import com.ibrahimokic.ordermanagement.repository.AddressRepository;
import com.ibrahimokic.ordermanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class OrderManagementApplication implements CommandLineRunner {
	private final UserRepository userRepository;
	private final AddressRepository addressRepository;

	@Autowired
	public OrderManagementApplication(UserRepository userRepository, AddressRepository addressRepository) {
		this.userRepository = userRepository;
		this.addressRepository = addressRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(OrderManagementApplication.class, args);
	}

	@Override
	public void run(String ...args) {
		if (!"github-actions".equals(System.getenv("ENVIRONMENT"))) {
			UserConsoleController userConsoleController = new UserConsoleController(userRepository, addressRepository);
			userConsoleController.userMainForm();
		}
	}

}
