package com.example.doancuoiky.hostel.model;


import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "Food")
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Foo")
    private Long ID_Foo;
    @Column(name = "Name")
    private String Name;
    @Column(name = "Calo")
    private String Calo;
    @Column(name = "Unit")
    private String Unit;
    @Column(name = "Session")
    private String Session;
    @Column(name = "Img")
    private String Img;
    @Column(name = "Information")
    private String Information;
    @ManyToOne
    @JoinColumn(name = "ID_User")
    private Users Users;
}
