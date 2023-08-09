package com.voteskill.gameserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

//@EnableScheduling
@SpringBootApplication
public class GameserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GameserverApplication.class, args);
	}
}
