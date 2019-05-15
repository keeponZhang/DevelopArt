package com.itheima.gsondemo;

import com.google.gson.annotations.SerializedName;

public class User {
    //省略其它
    public String name;
    public int age;
    @SerializedName(value = "emailAddress",alternate = {"email","email_address"})
    public String emailAddress;
    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public User(String name, int age, String emailAddress) {
        this.name = name;
        this.age = age;
        this.emailAddress = emailAddress;
    }
}