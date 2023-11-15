package com.example.app_hotrotapluyen.gym.User_screen.Model;

import android.widget.ImageView;

public class Program_child_Model {
    long ID_Program_Child;
    private  String NameProChi;
    private  String NameDay;
    private String Calo;
    private String Unil;
    private String Infomat;
    private String Img;

    public Program_child_Model() {
    }

    public Program_child_Model(String nameProChi, String unil, String img) {
        NameProChi = nameProChi;
        Unil = unil;
        Img = img;
    }

    public Program_child_Model(String nameProChi, String calo, String unil, String infomat) {
        NameProChi = nameProChi;
        Calo = calo;
        Unil = unil;
        Infomat = infomat;
    }

    public Program_child_Model(String nameProChi, String calo, String unil, String infomat, String img) {
        NameProChi = nameProChi;
        Calo = calo;
        Unil = unil;
        Infomat = infomat;
        Img = img;
    }

    public Program_child_Model(long ID_Program_Child, String nameProChi, String calo, String unil, String infomat, String img) {
        this.ID_Program_Child = ID_Program_Child;
        NameProChi = nameProChi;
        Calo = calo;
        Unil = unil;
        Infomat = infomat;
        Img = img;
    }

    public Program_child_Model(String nameProChi, String unil) {
        NameProChi = nameProChi;
        Unil = unil;
    }

    public long getID_Program_Child() {
        return ID_Program_Child;
    }

    public void setID_Program_Child(int ID_Program_Child) {
        this.ID_Program_Child = ID_Program_Child;
    }

    public String getNameProChi() {
        return NameProChi;
    }

    public void setNameProChi(String nameProChi) {
        NameProChi = nameProChi;
    }

    public String getNameDay() {
        return NameDay;
    }

    public void setNameDay(String nameDay) {
        NameDay = nameDay;
    }

    public String getCalo() {
        return Calo;
    }

    public void setCalo(String calo) {
        Calo = calo;
    }

    public String getUnil() {
        return Unil;
    }

    public void setUnil(String unil) {
        Unil = unil;
    }

    public String getInfomat() {
        return Infomat;
    }

    public void setInfomat(String infomat) {
        Infomat = infomat;
    }


    public String getImg() {
        return Img;
    }

    public void setImg(String img) {
        Img = img;
    }
}
