package com.example.BackendPlanti;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class BackendPlantiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendPlantiApplication.class, args);
	}

	@GetMapping("/planti/show")
	@CrossOrigin(origins = "http://localhost:8100")
	public String viewStudents(@RequestParam(value = "id", defaultValue = "1") Integer id) {
		String PflanzenString = null;

		Pflanze student = DB.CallPflanzeById(id);

		ObjectMapper om = new ObjectMapper();
		om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		try {
			PflanzenString = om.writeValueAsString(student);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return PflanzenString;
	}
}
