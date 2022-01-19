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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
		ArrayList<Pflanze> pflanzenList;
		String PflanzenString = null;
		pflanzenList = DB.callAllPflanze();
		ObjectMapper om = new ObjectMapper();
		om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		try {
			PflanzenString = om.writerWithDefaultPrettyPrinter().writeValueAsString(pflanzenList);
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


	@GetMapping("/planti/getPW")
	@CrossOrigin(origins = "http://localhost:8100")
	public String getPW(@RequestParam(value = "name") String username) {
		String UserString = null;

		User user = DB.callUserByName(username);

		ObjectMapper om = new ObjectMapper();
		om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		try {
			UserString = om.writeValueAsString(user.getPassword());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return UserString;
	}


	@GetMapping("/planti/adduser")
	@CrossOrigin(origins = "http://localhost:8100")
	public void addUser(@RequestParam(value = "username") String username,
						@RequestParam(value = "pw") String pw) {

		User user = new User();
		user.setUsername(username);
		user.setPassword(pw);
		DB.persist(user);


	}

	@GetMapping(value = "/planti/meinePflanzen", produces = "application/json")
	@CrossOrigin(origins = "http://localhost:8100")
	public String viewAllMyPlants() {
		ArrayList<Pflanze> pflanzenList= new ArrayList<>();
		ArrayList<Sensor> sensorList;
		String MeinPflanzenString = null;
		sensorList = DB.callAllSensor();
		for (var i=0;i<sensorList.size(); i++){
			Pflanze pflanze=DB.callPflanzeById(sensorList.get(i).getPID());
			pflanzenList.add(pflanze);
		}
		ObjectMapper om = new ObjectMapper();
		om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		try {
			MeinPflanzenString = om.writerWithDefaultPrettyPrinter().writeValueAsString(pflanzenList);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return MeinPflanzenString;
	}

	@GetMapping("/planti/getData")
	@CrossOrigin(origins = "http://localhost:8100")
	public String getSensorData(@RequestParam(value = "SID") Integer SID) throws Exception {
		Sensor sensor = DB.callSensorById(SID);
		String AusgabenString = null;
		ArrayList<SData> FinaleListe = new ArrayList<>();
		String Ausgabe=getHTML(sensor.getLink());
		String [] AusgabenArray=Ausgabe.split("result");
		for (int i=0; i<AusgabenArray.length; i++ ){
			try {
				SData sData = new SData();
				try {
					sData.setMoisture(Float.parseFloat(AusgabenArray[i].substring(AusgabenArray[i].indexOf("e25") + 5, AusgabenArray[i].indexOf("e25") + 9).trim()));
				} catch (NumberFormatException e){
					continue;
				}
				String ZeitStr = AusgabenArray[i].substring(AusgabenArray[i].indexOf("time") + 7, AusgabenArray[i].indexOf("time") + 26);
				LocalDateTime time = LocalDateTime.parse(ZeitStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
				long millis = time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
				sData.setTime(millis);
				FinaleListe.add(sData);
			}
			catch (StringIndexOutOfBoundsException e){
				continue;
			}
		}

		ObjectMapper om = new ObjectMapper();
		om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		try {
			AusgabenString = om.writerWithDefaultPrettyPrinter().writeValueAsString(FinaleListe);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return AusgabenString;
	}

	public static String getHTML(String urlToRead) throws Exception {
		StringBuilder result = new StringBuilder();
		URL url = new URL(urlToRead);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-Type", "text/event-stream");
		conn.setRequestProperty("Accept", "text/event-stream");
		conn.setRequestProperty("Authorization", "Bearer NNSXS.YVMTHRSE23RDO26NK2GAFNYMHX5FCO7KGKSAWOA.KADWQTL46PKLHIOSSPR6IGMNUPRW2BQ7TY7ZNKVAREQGSXZIWUWA");
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(conn.getInputStream()))) {
			for (String line; (line = reader.readLine()) != null; ) {
				result.append(line);
			}
		}
		return result.toString();
	}

}
