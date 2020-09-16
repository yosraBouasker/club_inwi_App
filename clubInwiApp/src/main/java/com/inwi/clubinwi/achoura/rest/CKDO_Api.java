package com.inwi.clubinwi.achoura.rest;

import com.google.gson.annotations.Expose;

public class CKDO_Api {

    @Expose
    private String phone;
    private String token;
    private String lang;

    public String getPhone() {
        return phone;
    }

    public String getToken() {
        return token;
    }

    public String getLang() {
        return lang;
    }
}
