package com.example.doancuoiky.hostel.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "Users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_User")
    private Long ID_User;
    @Column(name = "Phone",nullable = false)
    private String Phone;
    @Column(name = "Pass",nullable = false)
    private String Pass;
    @Column(name = "Address")
    private String Address;
    @Column(name = "Email")
    private String Email;
    @Column(name = "avt")
    private String avt;
    @Column(name = "Name")
    private String Name;
    @Column(name = "Gender")
    private String Gender;
    @Column(name = "Old")
    private String Old;
    @Column(name = "Weight")
    private String Weight;
    @Column(name = "Height")
    private String Height;
    @Column(name = "BMI")
    private String BMI;
    @Column(name = "Money")
    private int Money;
    @Column(name = "cretificate")
    private String cretificate;
    @Column(name = "Evaluate")
    private String Evaluate;
    @Column(name = "Rate")
    private String Rate;
    @Column(name = "People")
    private String People;
    @Column(name = "Experience")
    private String Experience;
    @Column(name = "Level")
    private int level;
    @Column(name = "IMG")
    private String img;
    @Column(name = "Status")
    private String status;
    @Column(name = "Tokendevice")
    private String tokendevice;

}
