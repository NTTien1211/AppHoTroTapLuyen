package com.example.doancuoiky.hostel.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "Prize")
public class Prize {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Pri")
    private Long ID_Pri;
    @Column(name = "Name")
    private String Name;
    @Column(name = "IMG")
    private String Img;

    @ManyToOne
    @JoinColumn(name = "ID_User")
    private Users Users;
}