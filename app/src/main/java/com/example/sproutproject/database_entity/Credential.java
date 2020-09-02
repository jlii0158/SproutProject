package com.example.sproutproject.database_entity;

public class Credential {
    String cre_id;
    String user_name;
    String password_hash;
    String sign_up_date;
    String user_id;

    public Credential(String cre_id, String user_name, String password_hash, String sign_up_date, String user_id) {
        this.cre_id = cre_id;
        this.user_name = user_name;
        this.password_hash = password_hash;
        this.sign_up_date = sign_up_date;
        this.user_id = user_id;
    }
}
