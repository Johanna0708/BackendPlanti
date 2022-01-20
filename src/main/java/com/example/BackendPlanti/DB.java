package com.example.BackendPlanti;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.ArrayList;

public class DB {

    //Ruft ein Pflanzenobjekt nach seiner PID aus der Datenbank ab
    public static Pflanze callPflanzeById(Integer input) {
        StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().configure().build();

        Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build();
        SessionFactory factory = meta.getSessionFactoryBuilder().build();
        Session session = factory.openSession();


        session.beginTransaction();
        Pflanze pflanze = session.load(Pflanze.class, input);
        session.flush();
        return pflanze;
    }

    //Ruft eine Liste aller gespeicherten Pflanzen aus der Datenbank ab
    public static ArrayList<Pflanze> callAllPflanze() {
        StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().configure().build();

        Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build();
        SessionFactory factory = meta.getSessionFactoryBuilder().build();
        Session session = factory.openSession();

        session.beginTransaction();
        ArrayList<Pflanze> entries = (ArrayList<Pflanze>) session.createCriteria(Pflanze.class).list();
        session.flush();
        return entries;
    }

    //Persistiert das eingegebene Objekt
    public static void persist(Object Eingabe) {
        StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().configure().build();

        Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build();
        SessionFactory factory = meta.getSessionFactoryBuilder().build();
        Session session = factory.openSession();

        session.beginTransaction();
        session.persist(Eingabe);
        session.flush();
        session.close();
    }

    //Ruft ein Nutzerobjekt (Nutzerdaten) nach seinem Nutzernamen aus der Datenbank ab
    public static User callUserByName(String input) {
        StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().configure().build();

        Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build();
        SessionFactory factory = meta.getSessionFactoryBuilder().build();
        Session session = factory.openSession();


        session.beginTransaction();
        User user = session.load(User.class, input);
        session.flush();
        return user;
    }

    //Ruft ein Sensorobjekt nach seiner SID aus der Datenbank ab
    public static Sensor callSensorById(Integer input) {
        StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().configure().build();

        Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build();
        SessionFactory factory = meta.getSessionFactoryBuilder().build();
        Session session = factory.openSession();


        session.beginTransaction();
        Sensor sensor = session.load(Sensor.class, input);
        session.flush();
        return sensor;
    }

    //Ruft eine Liste aller gespeicherten Sensoren aus der Datenbank ab
    public static ArrayList<Sensor> callAllSensor() {
        StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().configure().build();

        Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build();
        SessionFactory factory = meta.getSessionFactoryBuilder().build();
        Session session = factory.openSession();

        session.beginTransaction();
        ArrayList<Sensor> entries = (ArrayList<Sensor>) session.createCriteria(Sensor.class).list();
        session.flush();
        return entries;
    }
}
