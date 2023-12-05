package com.example.doancuoiky.hostel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.DateTimeException;

@Entity
@Data
@Table(name = "Calodi")
public class Calodi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Ca")
    private Long ID_Ca;
    @Column(name = "Time")
    private Date TimeDay;
    @Column(name = "CaloOUT")
    private String CaloOut;
    @Column(name = "CaloIn")
    private String CaloIn;
    @Column(name = "CaloSum")
    private String ColoSum;
    @Column(name = "desire")
    private String desire;
    @ManyToOne
    @JoinColumn(name = "ID_User")
    private Users Users;
}
