package com.example.app_hotrotapluyen.gym.User_screen.Model;
import java.sql.Timestamp;

public class BookModel {
    private Long ID_Book;
    private UserModel User_Give;
    private Timestamp TimeDay;
    private Double Money;
    private String Status;
    private UserModel Users;

    public BookModel(Long ID_Book, UserModel user_Give, Timestamp timeDay, Double money, String status, UserModel users) {
        this.ID_Book = ID_Book;
        User_Give = user_Give;
        TimeDay = timeDay;
        Money = money;
        Status = status;
        Users = users;
    }

    public Long getID_Book() {
        return ID_Book;
    }

    public void setID_Book(Long ID_Book) {
        this.ID_Book = ID_Book;
    }

    public UserModel getUser_Give() {
        return User_Give;
    }

    public void setUser_Give(UserModel user_Give) {
        User_Give = user_Give;
    }

    public Timestamp getTimeDay() {
        return TimeDay;
    }

    public void setTimeDay(Timestamp timeDay) {
        TimeDay = timeDay;
    }

    public Double getMoney() {
        return Money;
    }

    public void setMoney(Double money) {
        Money = money;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public UserModel getUsers() {
        return Users;
    }

    public void setUsers(UserModel users) {
        Users = users;
    }
}
