package com.meftaul.oms_config_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class OmsConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(OmsConfigServerApplication.class, args);
	}

}
