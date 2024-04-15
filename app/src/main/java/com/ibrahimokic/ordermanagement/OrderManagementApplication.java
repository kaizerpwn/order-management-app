package com.ibrahimokic.ordermanagement;

import com.ibrahimokic.ordermanagement.controller.console.UserConsoleController;
import com.ibrahimokic.ordermanagement.repository.AddressRepository;
import com.ibrahimokic.ordermanagement.repository.OrderRepository;
import com.ibrahimokic.ordermanagement.repository.ProductRepository;
import com.ibrahimokic.ordermanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@RequiredArgsConstructor
public class OrderManagementApplication implements CommandLineRunner {
	private final UserRepository userRepository;
	private final AddressRepository addressRepository;
	private final ProductRepository productRepository;
	private final OrderRepository orderRepository;

	public static void main(String[] args) {
		SpringApplication.run(OrderManagementApplication.class, args);
	}

	@Override
	public void run(String... args) {
		String environment = System.getenv("ENVIRONMENT");
		if (environment == null || !"github-actions".equals(environment)) {
			UserConsoleController userConsoleController = new UserConsoleController(userRepository, addressRepository,
					productRepository, orderRepository);
			userConsoleController.userMainForm();
		}
	}

}
