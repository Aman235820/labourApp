package com.example.labourApp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LabourAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(LabourAppApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper(){
		 return new ModelMapper();
	}


}
