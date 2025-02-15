package com.sppxs.europa;

import org.springframework.boot.SpringApplication;

public class TestEuropaApplication {

	public static void main(String[] args) {
		SpringApplication.from(EuropaApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
