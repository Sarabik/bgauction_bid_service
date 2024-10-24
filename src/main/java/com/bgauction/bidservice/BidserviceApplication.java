package com.bgauction.bidservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BidserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BidserviceApplication.class, args);
	}

}
