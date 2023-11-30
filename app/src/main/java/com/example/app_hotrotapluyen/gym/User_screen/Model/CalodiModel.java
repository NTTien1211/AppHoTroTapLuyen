package com.example.app_hotrotapluyen.gym.User_screen.Model;

import java.sql.Date;
import java.sql.Date;

public class CalodiModel {

    private Long ID_Ca;

    private Date TimeDay;

    private String CaloOut;

    private String CaloIn;

    private String ColoSum;

    public CalodiModel() {
    }

    public CalodiModel(Long ID_Ca, Date timeDay, String caloOut) {
        this.ID_Ca = ID_Ca;
        TimeDay = timeDay;
        CaloOut = caloOut;
    }

    public Long getID_Ca() {
        return ID_Ca;
    }

    public void setID_Ca(Long ID_Ca) {
        this.ID_Ca = ID_Ca;
    }

    public Date getTimeDay() {
        return TimeDay;
    }

    public void setTimeDay(Date timeDay) {
        TimeDay = timeDay;
    }

    public String getCaloOut() {
        return CaloOut;
    }

    public void setCaloOut(String caloOut) {
        CaloOut = caloOut;
    }

    public String getCaloIn() {
        return CaloIn;
    }

    public void setCaloIn(String caloIn) {
        CaloIn = caloIn;
    }

    public String getColoSum() {
        return ColoSum;
    }

    public void setColoSum(String coloSum) {
        ColoSum = coloSum;
    }
}
