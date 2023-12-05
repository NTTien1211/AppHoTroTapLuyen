package com.example.doancuoiky.hostel.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "Program_Child")
public class Program_Child {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Program_Child")
    private Long ID_Program_Child;
    @Column(name = "Name")
    private String Name;
    @Column(name = "Calo")
    private String Calo;
    @Column(name = "Information")
    private String Information;
    @Column(name = "Img")
    private String Img;
    @Column(name = "Unit")
    private String Unit;

    @ManyToOne
    @JoinColumn(name = "ID_Day")
    private Day day;
}
