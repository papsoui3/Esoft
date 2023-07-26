package com.example.training;

public class UserDetails {
    private String userName;
    private String versionDate;


    public UserDetails(String userName,String versionDate) {
        this.versionDate = versionDate;
        this.userName = userName;
    }


    public String getVersionDate() {

        return versionDate;
    }

    public void setVersionDate(String versionDate) {
        this.versionDate = versionDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }



}
