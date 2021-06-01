package com.example.coachlea.data_access;

import java.io.Serializable;
import java.util.Date;

public class PatientDA implements Serializable {
    private static final long serialVersionUID = -7060210544600464481L;

    private Long userId;

    private String govtId;

    private String username;

    private Date birthday;

    private String gender;

    private String side;

    private String type;

    private boolean smoker;


    public PatientDA(Long userId, String govtId, String username, Date birthday,
                     String gender, String side, boolean smoker, String type) {
        this.userId = userId;
        this.govtId = govtId;
        this.username = username;
        this.birthday = birthday;
        this.gender = gender;
        this.side = side;
        this.smoker = smoker;
        this.type = type;
    }

    public PatientDA() {
    }

    public PatientDA(String username, String govtId) {
        this.username = username;
        this.govtId = govtId;
    }

    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public Date getBirthday() {
        return this.birthday;
    }
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
    public String getGender() {
        return this.gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getSide() {
        return this.side;
    }
    public void setSide(String side) {
        this.side = side;
    }
    public boolean getSmoker() {
        return this.smoker;
    }
    public void setSmoker(boolean smoker) {
        this.smoker = smoker;
    }
    public Long getUserId() {
        return this.userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getGovtId() {
        return this.govtId;
    }
    public void setGovtId(String govtId) {
        this.govtId = govtId;
    }
    public String getType(){return this.type;}
    public void setType(String type) { this.type = type;}

}
