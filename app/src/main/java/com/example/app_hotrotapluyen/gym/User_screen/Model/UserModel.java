package com.example.app_hotrotapluyen.gym.User_screen.Model;

import java.sql.Timestamp;

public class UserModel {
    private String idUser;
    private String name;
    private String phone;
    private String Email;
    private String cretificate;
    private String prize;
    private String Evaluate;
    private String weight;
    private String hight;
    private String gender;
    private String BMI;
    private Timestamp creatTimestamp;
    private  int Level;
    private  int Money;

    private int experience;
    private int managers;
    private float rate;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    private String img;
    private String[] prizeNames;
    private String[] prizeImgs;

    private  String tokenUS;

    // Constructors
    public UserModel() {
        // Empty constructor
    }

    public UserModel(String idUser) {
        this.idUser = idUser;
    }


    public UserModel(String idUser, String name, String img) {
        this.idUser = idUser;
        this.name = name;
        this.img = img;
    }

    public UserModel(String idUser, String name, String phone, String email) {
        this.idUser = idUser;
        this.name = name;
        this.phone = phone;
        Email = email;
    }

    public UserModel(String idUser, String name, String phone, String email, String img) {
        this.idUser = idUser;
        this.name = name;
        this.phone = phone;
        Email = email;
        this.img = img;
    }

    public UserModel(String idUser, String name, String phone, int level) {
        this.idUser = idUser;
        this.name = name;
        this.phone = phone;
        Level = level;
    }

    public UserModel(String name, int experience, int managers, float rate) {
        this.name = name;
        this.experience = experience;
        this.managers = managers;
        this.rate = rate;
    }

    public UserModel(String idUser, String name, int experience, int managers, float rate) {
        this.idUser = idUser;
        this.name = name;
        this.experience = experience;
        this.managers = managers;
        this.rate = rate;
    }

    public UserModel(String idUser, String name, String phone, String email, String BMI, int level) {
        this.idUser = idUser;
        this.name = name;
        this.phone = phone;
        Email = email;
        this.BMI = BMI;
        Level = level;
    }

    public UserModel(String idUser, String name, String phone, String cretificate, String prize, String evaluate, int experience, int managers, float rate) {
        this.idUser = idUser;
        this.name = name;
        this.phone = phone;
        this.cretificate = cretificate;
        this.prize = prize;
        Evaluate = evaluate;
        this.experience = experience;
        this.managers = managers;
        this.rate = rate;
    }

    public UserModel(String idUser, String name, String phone, String cretificate, String prize, String evaluate, int experience, int managers, float rate, String img) {
        this.idUser = idUser;
        this.name = name;
        this.phone = phone;
        this.cretificate = cretificate;
        this.prize = prize;
        Evaluate = evaluate;
        this.experience = experience;
        this.managers = managers;
        this.rate = rate;
        this.img = img;
    }

    public UserModel(String idUser, String name, String phone, String cretificate, String prize, String evaluate, int money, int experience, int managers, float rate, String img) {
        this.idUser = idUser;
        this.name = name;
        this.phone = phone;
        this.cretificate = cretificate;
        this.prize = prize;
        Evaluate = evaluate;
        Money = money;
        this.experience = experience;
        this.managers = managers;
        this.rate = rate;
        this.img = img;
    }

    public UserModel(String idUser, String name, String phone, String email, String weight, String hight, String gender) {
        this.idUser = idUser;
        this.name = name;
        this.phone = phone;
        Email = email;
        this.weight = weight;
        this.hight = hight;
        this.gender = gender;
    }

    public UserModel(String idUser, String name, String phone, String email, String weight, String hight, String gender, String BMI, String img) {
        this.idUser = idUser;
        this.name = name;
        this.phone = phone;
        Email = email;
        this.weight = weight;
        this.hight = hight;
        this.gender = gender;
        this.BMI = BMI;
        this.img = img;
    }

    public UserModel(String idUser, String name, String phone, String email, String weight, String hight, String gender, String BMI, int money, String img) {
        this.idUser = idUser;
        this.name = name;
        this.phone = phone;
        Email = email;
        this.weight = weight;
        this.hight = hight;
        this.gender = gender;
        this.BMI = BMI;
        Money = money;
        this.img = img;
    }

    public UserModel(String idUser, String name, String phone, String email, String weight, String hight, String gender, String BMI, int level, int experience, String img) {
        this.idUser = idUser;
        this.name = name;
        this.phone = phone;
        Email = email;
        this.weight = weight;
        this.hight = hight;
        this.gender = gender;
        this.BMI = BMI;
        Level = level;
        this.experience = experience;
        this.img = img;
    }

    public UserModel(String idUser, String name, String phone, String email, String weight, String hight, String gender, String BMI) {
        this.idUser = idUser;
        this.name = name;
        this.phone = phone;
        Email = email;
        this.weight = weight;
        this.hight = hight;
        this.gender = gender;
        this.BMI = BMI;
    }

    public UserModel(String idUser, String name, String phone, String email, String cretificate, String weight, String hight, String gender, String BMI, int experience, String img) {
        this.idUser = idUser;
        this.name = name;
        this.phone = phone;
        Email = email;
        this.cretificate = cretificate;
        this.weight = weight;
        this.hight = hight;
        this.gender = gender;
        this.BMI = BMI;
        this.experience = experience;
        this.img = img;
    }

    public UserModel(String idUser, String name, String phone, String email, String cretificate, String weight, String hight, String gender, String BMI, int experience, String img, String[] prizeNames, String[] prizeImgs) {
        this.idUser = idUser;
        this.name = name;
        this.phone = phone;
        Email = email;
        this.cretificate = cretificate;
        this.weight = weight;
        this.hight = hight;
        this.gender = gender;
        this.BMI = BMI;
        this.experience = experience;
        this.img = img;
        this.prizeNames = prizeNames;
        this.prizeImgs = prizeImgs;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public Timestamp getCreatTimestamp() {
        return creatTimestamp;
    }

    public void setCreatTimestamp(Timestamp creatTimestamp) {
        this.creatTimestamp = creatTimestamp;
    }

    public String getBMI() {
        return BMI;
    }

    public void setBMI(String BMI) {
        this.BMI = BMI;
    }

    public int getLevel() {
        return Level;
    }

    public void setLevel(int level) {
        Level = level;
    }

    public String getIdUser() {
        return idUser;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHight() {
        return hight;
    }

    public void setHight(String hight) {
        this.hight = hight;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getManagers() {
        return managers;
    }

    public void setManagers(int managers) {
        this.managers = managers;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
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

    public String getCretificate() {
        return cretificate;
    }

    public void setCretificate(String cretificate) {
        this.cretificate = cretificate;
    }

    public String getPrize() {
        return prize;
    }

    public int getMoney() {
        return Money;
    }

    public void setMoney(int money) {
        Money = money;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public String getEvaluate() {
        return Evaluate;
    }

    public void setEvaluate(String evaluate) {
        Evaluate = evaluate;
    }

    public String[] getPrizeNames() {
        return prizeNames;
    }

    public void setPrizeNames(String[] prizeNames) {
        this.prizeNames = prizeNames;
    }

    public String[] getPrizeImgs() {
        return prizeImgs;
    }

    public void setPrizeImgs(String[] prizeImgs) {
        this.prizeImgs = prizeImgs;
    }

    public String getTokenUS() {
        return tokenUS;
    }

    public void setTokenUS(String tokenUS) {
        this.tokenUS = tokenUS;
    }
}

