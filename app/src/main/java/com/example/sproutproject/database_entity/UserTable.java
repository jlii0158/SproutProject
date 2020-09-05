package com.example.sproutproject.database_entity;

public class UserTable {
    private String user_id;
    private String user_grow;
    private String user_nick;
    private String head_id;

    public UserTable(String user_grow, String user_nick, String head_id) {

        this.user_grow = user_grow;
        this.user_nick = user_nick;
        this.head_id = head_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_grow() {
        return user_grow;
    }

    public void setUser_grow(String user_grow) {
        this.user_grow = user_grow;
    }

    public String getUser_nick() {
        return user_nick;
    }

    public void setUser_nick(String user_nick) {
        this.user_nick = user_nick;
    }

    public String getHead_id() {
        return head_id;
    }

    public void setHead_id(String head_id) {
        this.head_id = head_id;
    }
}
