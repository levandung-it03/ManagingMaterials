package com.CSDLPT.ManagingMaterials;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class ManagingMaterialsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManagingMaterialsApplication.class, args);
	}

}
