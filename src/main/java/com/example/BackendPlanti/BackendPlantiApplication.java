package com.example.BackendPlanti;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
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
	public String viewPflanze(@RequestParam(value = "id", defaultValue = "1") Integer id) {
		String PflanzenString = null;

		Pflanze pflanze = DB.callPflanzeById(id);

		ObjectMapper om = new ObjectMapper();
		om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		try {
			PflanzenString = om.writeValueAsString(pflanze);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return PflanzenString;
	}

	@GetMapping(value = "/planti/showall", produces = "application/json")
	@CrossOrigin(origins = "http://localhost:8100")
	public String viewAllPlants() {
		Pflanze[] studentList=new Pflanze[6];
		String PflanzenString = null;
		for (int i = 1; i < 7; i++) {
			Pflanze pflanze = DB.callPflanzeById(i);
			studentList[i - 1] = pflanze;
		}
		ObjectMapper om = new ObjectMapper();
		om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		try {
			PflanzenString = om.writerWithDefaultPrettyPrinter().writeValueAsString(studentList);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return PflanzenString;
	}

	@GetMapping("/planti/addsensor")
	@CrossOrigin(origins = "http://localhost:8100")
	public void addSensor(@RequestParam(value = "pid") Integer pid,
						  @RequestParam(value = "link") String link) {

		Sensor sensor = new Sensor();
		sensor.setLink(link);
		sensor.setPID(pid);
		DB.persist(sensor);


	}
}
