package com.matiasjara.spring.cloud.msvc.usuarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MscvUsuariosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MscvUsuariosApplication.class, args);
	}

}
