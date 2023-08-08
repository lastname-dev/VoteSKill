package io.openvidu.basic.java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
//@PropertySource(value = {"classpath:application.properties"}, ignoreResourceNotFound = false)
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

}
