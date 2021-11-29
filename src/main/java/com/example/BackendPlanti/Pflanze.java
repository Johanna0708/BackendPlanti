package com.example.BackendPlanti;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Pflanze {

    private Integer PID;
    private String Name;
    private int Standort;
    private int Schwierigkeit;
    private String Merkmale;
    private String WissName;
    private String Picture;


    public Pflanze() {}

    @Id
    public Integer getPID() {
        return PID;
    }

    public void setPID(Integer PID) {
        this.PID = PID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getStandort() {
        return Standort;
    }

    public void setStandort(int standort) {
        Standort = standort;
    }

    public int getSchwierigkeit() {
        return Schwierigkeit;
    }

    public void setSchwierigkeit(int schwierigkeit) {
        Schwierigkeit = schwierigkeit;
    }

    public String getMerkmale() {
        return Merkmale;
    }

    public void setMerkmale(String merkmale) {
        Merkmale = merkmale;
    }

    public String getWissName() {
        return WissName;
    }

    public void setWissName(String wissName) {
        WissName = wissName;
    }

    public String getPicture() {
        return Picture;
    }

    public void setPicture(String picture) {
        Picture = picture;
    }
}
