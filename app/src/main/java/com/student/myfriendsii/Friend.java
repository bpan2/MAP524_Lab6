package com.student.myfriendsii;

public class Friend {
    private int _id;
    private String name;
    private String email;
    private String phoneNumber;

    public Friend() {
        _id = 0;
        name = null;
        email = null;
        phoneNumber = null;
    }


    public Friend(int id, String name, String email, String phoneNumber) {
        this._id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
