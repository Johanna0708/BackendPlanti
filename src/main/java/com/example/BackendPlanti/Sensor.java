package com.example.BackendPlanti;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Sensor {

    private int SID;
    private String Link;
    private int PID;

    public Sensor() {
    }

    @Id
    public int getSID() {
        return SID;
    }

    public void setSID(int SID) {
        this.SID = SID;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public int getPID() {
        return PID;
    }

    public void setPID(int PID) {
        this.PID = PID;
    }
}
