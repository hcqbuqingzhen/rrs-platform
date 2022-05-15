package com.springboot.demo.bean;

public class UserBean implements Mybean{
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserBean(String username) {
        this.username = username;
    }
    public UserBean() {

    }

    @Override
    public String toString() {
        return "UserBean{" +
                "username='" + username + '\'' +
                '}';
    }
}
