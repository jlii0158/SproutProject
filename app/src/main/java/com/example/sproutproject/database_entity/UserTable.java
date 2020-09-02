package com.example.sproutproject.database_entity;

public class UserTable {
    private String user_id;
    private String user_grow;
    private String user_nick;
    private String head_id;

    public UserTable(String user_id, String user_grow, String user_nick, String head_id) {
        this.user_id = user_id;
        this.user_grow = user_grow;
        this.user_nick = user_nick;
        this.head_id = head_id;
    }
}
