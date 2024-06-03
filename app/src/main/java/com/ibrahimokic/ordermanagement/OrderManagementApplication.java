package com.ibrahimokic.ordermanagement;

import com.ibrahimokic.ordermanagement.adapters.UserConsoleAdapter;
import com.ibrahimokic.ordermanagement.service.AddressService;
import com.ibrahimokic.ordermanagement.service.OrderService;
import com.ibrahimokic.ordermanagement.service.ProductService;
import com.ibrahimokic.ordermanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class OrderManagementApplication implements CommandLineRunner {
	private final AddressService addressService;
	private final UserService userService;
	private final ProductService productService;
	private final OrderService orderService;

	@Value("${environment}")
	private String environment;

	public static void main(String[] args) {
		SpringApplication.run(OrderManagementApplication.class, args);
	}

	@Override
	public void run(String... args) {
		String gitEnvironment = System.getenv("ENVIRONMENT");
		if (!"github-actions".equals(gitEnvironment) && !"testing".equals(environment)) {
			UserConsoleAdapter userConsoleAdapter = new UserConsoleAdapter(userService, addressService, productService,
					orderService);
			userConsoleAdapter.userMainForm();
		}
	}
}
