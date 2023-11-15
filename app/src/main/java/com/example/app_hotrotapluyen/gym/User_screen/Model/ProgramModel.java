package com.example.app_hotrotapluyen.gym.User_screen.Model;

public class ProgramModel {
    private  int ID_Pro;
    private  String NamePro;
    private  String NameUserAdd;
    private String level;

    public ProgramModel() {
    }

    public ProgramModel(String namePro, String nameUserAdd, String level) {
        NamePro = namePro;
        NameUserAdd = nameUserAdd;
        this.level = level;
    }

    public ProgramModel(int ID_Pro, String namePro, String nameUserAdd, String level) {
        this.ID_Pro = ID_Pro;
        NamePro = namePro;
        NameUserAdd = nameUserAdd;
        this.level = level;
    }

    public int getID_Pro() {
        return ID_Pro;
    }

    public void setID_Pro(int ID_Pro) {
        this.ID_Pro = ID_Pro;
    }

    public String getNamePro() {
        return NamePro;
    }

    public void setNamePro(String namePro) {
        NamePro = namePro;
    }

    public String getNameUserAdd() {
        return NameUserAdd;
    }

    public void setNameUserAdd(String nameUserAdd) {
        NameUserAdd = nameUserAdd;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
