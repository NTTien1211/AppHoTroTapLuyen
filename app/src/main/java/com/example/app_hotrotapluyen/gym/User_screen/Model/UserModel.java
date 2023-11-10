package com.example.app_hotrotapluyen.gym.User_screen.Model;

import java.sql.Timestamp;

public class UserModel {
    private String idUser;
    private String name;
    private String phone;
    private Timestamp creatTimestamp;

    // Constructors
    public UserModel() {
        // Empty constructor
    }

    public UserModel(String idUser, String name, String phone) {
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

