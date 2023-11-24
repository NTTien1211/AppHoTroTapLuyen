package com.example.app_hotrotapluyen.gym.User_screen.Model;

public class FoodModel {
    private Long ID_Foo;

    private String Name;

    private String Calo;

    private String Unit;

    private String Session;

    private String Img;
    private String Information;


    public FoodModel() {
    }

    public FoodModel(Long ID_Foo, String name, String calo, String unit, String session, String img) {
        this.ID_Foo = ID_Foo;
        Name = name;
        Calo = calo;
        Unit = unit;
        Session = session;
        Img = img;
    }

    public FoodModel(Long ID_Foo, String name, String calo, String unit, String session, String img, String information) {
        this.ID_Foo = ID_Foo;
        Name = name;
        Calo = calo;
        Unit = unit;
        Session = session;
        Img = img;
        Information = information;
    }

    public String getInformation() {
        return Information;
    }

    public void setInformation(String information) {
        Information = information;
    }

    public Long getID_Foo() {
        return ID_Foo;
    }

    public void setID_Foo(Long ID_Foo) {
        this.ID_Foo = ID_Foo;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCalo() {
        return Calo;
    }

    public void setCalo(String calo) {
        Calo = calo;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getSession() {
        return Session;
    }

    public void setSession(String session) {
        Session = session;
    }

    public String getImg() {
        return Img;
    }

    public void setImg(String img) {
        Img = img;
    }
}
