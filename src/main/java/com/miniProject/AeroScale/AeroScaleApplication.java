package com.miniProject.AeroScale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AeroScaleApplication {

	public static void main(String[] args) {
		SpringApplication.run(AeroScaleApplication.class, args);
	}

}
