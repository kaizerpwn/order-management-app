package com.ibrahimokic.ordermanagement;

import com.ibrahimokic.ordermanagement.adapters.UserConsoleAdapter;
import com.ibrahimokic.ordermanagement.service.AddressService;
import com.ibrahimokic.ordermanagement.service.OrderService;
import com.ibrahimokic.ordermanagement.service.ProductService;
import com.ibrahimokic.ordermanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@RequiredArgsConstructor
public class OrderManagementApplication implements CommandLineRunner {
	private final UserService userService;
	private final AddressService addressService;
	private final ProductService productService;
	private final OrderService orderService;

	public static void main(String[] args) {
		SpringApplication.run(OrderManagementApplication.class, args);
	}

	@Override
	public void run(String... args) {
		String environment = System.getenv("ENVIRONMENT");
		if (environment == null || !"github-actions".equals(environment)) {
			UserConsoleAdapter userConsoleAdapter = new UserConsoleAdapter(userService, addressService,
					productService, orderService);
			userConsoleAdapter.userMainForm();
		}
	}

}
