package com.example.BackendPlanti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@SpringBootApplication
@RestController
public class BackendPlantiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendPlantiApplication.class, args);
    }

    @GetMapping("/planti/show")
    @CrossOrigin(origins = "http://localhost:8100")
    public String viewPflanze(@RequestParam(value = "id", defaultValue = "1") Integer id) {
        Pflanze pflanze = DB.callPflanzeById(id);
        return Miscellaneous.MapObject(pflanze);
    }

    @GetMapping(value = "/planti/showall", produces = "application/json")
    @CrossOrigin(origins = "http://localhost:8100")
    public String viewAllPlants() {
        ArrayList<Pflanze> pflanzenList = DB.callAllPflanze();
        return Miscellaneous.MapObjectList(pflanzenList);
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
        User user = DB.callUserByName(username);
        return Miscellaneous.MapObject(user.getPassword());
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
        ArrayList<Pflanze> pflanzenList = new ArrayList<>();
        ArrayList<Sensor> sensorList = DB.callAllSensor();
        for (Sensor sensor : sensorList) {
            Pflanze pflanze = DB.callPflanzeById(sensor.getPID());
            pflanzenList.add(pflanze);
        }
        return Miscellaneous.MapObjectList(pflanzenList);
    }

    @GetMapping("/planti/getData")
    @CrossOrigin(origins = "http://localhost:8100")
    public String getSensorData(@RequestParam(value = "SID") Integer SID) throws Exception {
        ArrayList<SData> FinaleListe = new ArrayList<>();
        String Ausgabe = Miscellaneous.getSensorOutput(DB.callSensorById(SID).getLink());
        String[] AusgabenArray = Ausgabe.split("result");
        for (String s : AusgabenArray) {
            try {
                SData sData = new SData();
                try {
                    sData.setMoisture(Float.parseFloat(s.substring(s.indexOf("e25") + 5, s.indexOf("e25") + 9).trim()));
                } catch (NumberFormatException e) {
                    continue;
                }
                String ZeitStr = s.substring(s.indexOf("time") + 7, s.indexOf("time") + 26);
                LocalDateTime time = LocalDateTime.parse(ZeitStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                long millis = time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                sData.setTime(millis);
                FinaleListe.add(sData);
            } catch (StringIndexOutOfBoundsException ignored) {

            }
        }
        return Miscellaneous.MapObjectList(FinaleListe);
    }
}
