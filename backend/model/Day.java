package com.example.doancuoiky.hostel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "Day")
public class Day {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Day")
    private Long ID_Day;
    @Column(name = "Day")
    private String Day;
    @ManyToOne
    @JoinColumn(name = "ID_Pro")
    private Program program;
}
