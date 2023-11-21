package com.example.app_hotrotapluyen.gym.User_screen.Model;

public class HomeU_pt {
    private String idPT;
    private String name;
    private int experience;
    private int managers;
    private String phone;
    private float rate;

    public HomeU_pt(String idPT, String name, String phone) {
        this.idPT = idPT;
        this.name = name;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public HomeU_pt(String name, int experience, int managers, float rate) {
        this.name = name;
        this.experience = experience;
        this.managers = managers;
        this.rate = rate;
    }

    public HomeU_pt(String idPT, String name, int experience, int managers, float rate) {
        this.idPT = idPT;
        this.name = name;
        this.experience = experience;
        this.managers = managers;
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public int getExperience() {
        return experience;
    }

    public int getManagers() {
        return managers;
    }

    public double getRate() {
        return rate;
    }

    public String getIdPT() {
        return idPT;
    }

    public void setIdPT(String idPT) {
        this.idPT = idPT;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public void setManagers(int managers) {
        this.managers = managers;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
}
