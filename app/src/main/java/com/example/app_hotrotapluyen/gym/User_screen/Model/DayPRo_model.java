package com.example.app_hotrotapluyen.gym.User_screen.Model;

public class DayPRo_model {
    private  long ID_Day;
    private  String NameDay;
    private int Slbtap;

    public DayPRo_model() {
    }

    public DayPRo_model(long ID_Day, String nameDay) {
        this.ID_Day = ID_Day;
        NameDay = nameDay;
    }

    public DayPRo_model(String nameDay, int slbtap) {
        NameDay = nameDay;
        Slbtap = slbtap;
    }

    public DayPRo_model(long ID_Day, String nameDay, int slbtap) {
        this.ID_Day = ID_Day;
        NameDay = nameDay;
        Slbtap = slbtap;
    }

    public long getID_Day() {
        return ID_Day;
    }

    public void setID_Day(int ID_Day) {
        this.ID_Day = ID_Day;
    }

    public String getNameDay() {
        return NameDay;
    }

    public void setNameDay(String nameDay) {
        NameDay = nameDay;
    }

    public int getSlbtap() {
        return Slbtap;
    }

    public void setSlbtap(int slbtap) {
        Slbtap = slbtap;
    }
}
