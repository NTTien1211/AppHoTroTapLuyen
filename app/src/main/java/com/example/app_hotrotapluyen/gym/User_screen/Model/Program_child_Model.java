package com.example.app_hotrotapluyen.gym.User_screen.Model;

import android.widget.ImageView;

import java.io.Serializable;

public class Program_child_Model implements Serializable {
    long ID_Program_Child;
    private  String NameProChi;
    private  String NameDay;
    private String Calo;
    private long Unil;
    private String Infomat;
    private String Img;

    public Program_child_Model() {
    }

    public Program_child_Model(long unil) {
        Unil = unil;
    }

    public Program_child_Model(String nameProChi, long unil) {
        NameProChi = nameProChi;
        Unil = unil;
    }

    public Program_child_Model(String nameProChi, long unil, String img) {
        NameProChi = nameProChi;
        Unil = unil;
        Img = img;
    }

    public Program_child_Model(String nameProChi, String nameDay, long unil, String infomat) {
        NameProChi = nameProChi;
        NameDay = nameDay;
        Unil = unil;
        Infomat = infomat;
    }

    public Program_child_Model(String nameProChi, String nameDay, long unil, String infomat, String img) {
        NameProChi = nameProChi;
        NameDay = nameDay;
        Unil = unil;
        Infomat = infomat;
        Img = img;
    }

    public Program_child_Model(String nameProChi, String nameDay, long unil) {
        NameProChi = nameProChi;
        NameDay = nameDay;
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

    public long getUnil() {
        return Unil;
    }

    public void setUnil(long unil) {
        Unil = unil;
    }

    public String getInfomat() {
        return Infomat;
    }

    public void setInfomat(String infomat) {
        Infomat = infomat;
    }

    public int compareTo(Program_child_Model another) {
        // Giả sử tên ngày có dạng "Day1", "Day2", ..., "Day7"
        int ngay1 = Integer.parseInt(this.getNameDay().substring(3));
        int ngay2 = Integer.parseInt(another.getNameDay().substring(3));
        return Integer.compare(ngay1, ngay2);
    }
    public String getImg() {
        return Img;
    }

    public void setImg(String img) {
        Img = img;
    }
}
