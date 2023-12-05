package com.example.doancuoiky.hostel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.DateTimeException;

@Entity
@Data
@Table(name = "Book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Book")
    private Long ID_Book;
    @ManyToOne
    @JoinColumn(name = "ID_User_Give")
    private Users User_Give;
    @Column(name = "Time")
    private Timestamp TimeDay;
    @Column(name = "Money")
    private Double Money;
    @Column(name = "Status")
    private String Status;
    @Column(name = "TimeinDay")
    private String TimeinDay;
    @Column(name = "Duration")
    private int Duration;
    @ManyToOne
    @JoinColumn(name = "ID_User")
    private Users Users;
}
