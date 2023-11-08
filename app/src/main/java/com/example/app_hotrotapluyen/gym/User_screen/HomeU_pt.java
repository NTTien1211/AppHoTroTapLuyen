package com.example.app_hotrotapluyen.gym.User_screen;

public class HomeU_pt {
    private String name;
    private int experience;
    private int managers;
    private double rate;

    public HomeU_pt(String name, int experience, int managers, double rate) {
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
}
