package com.example.doancuoiky.hostel.model;


import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "Program")
public class Program {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Pro")
    private Long ID_Pro;
    @Column(name = "Name")
    private String Name;
    @Column(name = "Calo")
    private String Calo;
    @Column(name = "Unit")
    private String Unit;
    @Column(name = "Level")
    private String Level;
    @ManyToOne
    @JoinColumn(name = "ID_User")
    private Users Users;
}
