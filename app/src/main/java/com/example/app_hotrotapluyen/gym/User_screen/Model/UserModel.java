package com.example.app_hotrotapluyen.gym.User_screen.Model;

import java.sql.Timestamp;

public class UserModel {
    private String idUser;
    private String name;
    private String phone;
    private String Email;
    private Timestamp creatTimestamp;
    private  int Level;
    private int experience;
    private int managers;
    private float rate;

    // Constructors
    public UserModel() {
        // Empty constructor
    }

    public UserModel(String idUser, String name, String phone) {
        this.idUser = idUser;
        this.name = name;
        this.phone = phone;
    }

    public UserModel(String idUser, String name, String phone, String email) {
        this.idUser = idUser;
        this.name = name;
        this.phone = phone;
        Email = email;
    }

    public UserModel(String idUser, String name, String phone, int level) {
        this.idUser = idUser;
        this.name = name;
        this.phone = phone;
        Level = level;
    }

    public UserModel(String name, int experience, int managers, float rate) {
        this.name = name;
        this.experience = experience;
        this.managers = managers;
        this.rate = rate;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public Timestamp getCreatTimestamp() {
        return creatTimestamp;
    }

    public void setCreatTimestamp(Timestamp creatTimestamp) {
        this.creatTimestamp = creatTimestamp;
    }

    public int getLevel() {
        return Level;
    }

    public void setLevel(int level) {
        Level = level;
    }

    public String getIdUser() {
        return idUser;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getManagers() {
        return managers;
    }

    public void setManagers(int managers) {
        this.managers = managers;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


}

