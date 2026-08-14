package com.bbva.chatbot.helix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableCaching
@EnableFeignClients
public class HelixChatbotApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelixChatbotApplication.class, args);
	}

}
