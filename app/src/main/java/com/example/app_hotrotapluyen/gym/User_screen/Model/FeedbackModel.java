package com.example.app_hotrotapluyen.gym.User_screen.Model;

import java.sql.Timestamp;

public class FeedbackModel {

    private Long ID_Fe;

    private UserModel User_Give;

    private Timestamp TimeDay;

    private String Information;

    private Double Rate;

    private UserModel Users;

    public FeedbackModel(Long ID_Fe, Timestamp timeDay, String information, Double rate) {
        this.ID_Fe = ID_Fe;
        TimeDay = timeDay;
        Information = information;
        Rate = rate;
    }

    public FeedbackModel(Long ID_Fe, UserModel user_Give, Timestamp timeDay, String information, Double rate, UserModel users) {
        this.ID_Fe = ID_Fe;
        User_Give = user_Give;
        TimeDay = timeDay;
        Information = information;
        Rate = rate;
        Users = users;
    }

    public Long getID_Fe() {
        return ID_Fe;
    }

    public void setID_Fe(Long ID_Fe) {
        this.ID_Fe = ID_Fe;
    }

    public UserModel getUser_Give() {
        return User_Give;
    }

    public void setUser_Give(UserModel user_Give) {
        User_Give = user_Give;
    }

    public Timestamp getTimeDay() {
        return TimeDay;
    }

    public void setTimeDay(Timestamp timeDay) {
        TimeDay = timeDay;
    }

    public String getInformation() {
        return Information;
    }

    public void setInformation(String information) {
        Information = information;
    }

    public Double getRate() {
        return Rate;
    }

    public void setRate(Double rate) {
        Rate = rate;
    }

    public UserModel getUsers() {
        return Users;
    }

    public void setUsers(UserModel users) {
        Users = users;
    }
}
