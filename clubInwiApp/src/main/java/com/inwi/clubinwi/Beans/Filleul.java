package com.inwi.clubinwi.Beans;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Filleul {

    private String msisdn;
    private String status;
    private String date;
    private String full_name;
    private String avatar;

    public Filleul() {
        super();
        // TODO Auto-generated constructor stub
    }

    public Filleul(String msisdn, String status, String date, String full_name, String avatar) {
        super();
        this.msisdn = msisdn;
        this.status = status;
        this.date = date;
        this.full_name = full_name;
        this.avatar = avatar;
    }

    public static Filleul parseFilleul(JSONObject mResponseObject) {
        try {
            Filleul mFilleul = new Filleul();

            mFilleul.setMsisdn(mResponseObject.getString("msisdn"));
            mFilleul.setFull_name(mResponseObject.getString("full_name"));
            mFilleul.setStatus(mResponseObject.getString("status"));
            mFilleul.setDate(mResponseObject.getString("date"));
            mFilleul.setAvatar(mResponseObject.getString("avatar"));

            return mFilleul;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Filleul> parseFilleuls(JSONArray mResultsArray) {
        if (mResultsArray == null)
            return null;
        try {
            ArrayList<Filleul> mFilleuls = new ArrayList<Filleul>();
            for (int i = 0; i < mResultsArray.length(); i++) {
                mFilleuls.add(parseFilleul(mResultsArray.getJSONObject(i)));
            }
            return mFilleuls;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
