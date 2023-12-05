package com.example.doancuoiky.hostel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.DateTimeException;

@Entity
@Data
@Table(name = "Feedback")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Fe")
    private Long ID_Fe;
    @ManyToOne
    @JoinColumn(name = "ID_User_Give")
    private Users User_Give;
    @Column(name = "Time")
    private Timestamp TimeDay;
    @Column(name = "Information")
    private String Information;
    @Column(name = "Rate")
    private Double Rate;
    @ManyToOne
    @JoinColumn(name = "ID_User")
    private Users Users;
}
