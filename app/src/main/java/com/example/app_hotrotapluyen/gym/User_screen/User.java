package com.example.app_hotrotapluyen.gym.User_screen;

import java.sql.Timestamp;

public class User {
    private String idUser;
    private String name;
    private String phone;
    private Timestamp creatTimestamp;

    // Constructors
    public User() {
        // Empty constructor
    }

    public User(String idUser, String name, String phone) {
        this.idUser = idUser;
        this.name = name;
        this.phone = phone;
    }



    public String getIdUser() {
        return idUser;
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

