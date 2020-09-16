package com.inwi.clubinwi.Beans;

public class SousMenu {
    private int icon;
    private String name;
    private Boolean isActive;
    private Boolean isPhoneNumber;

    public SousMenu() {
        super();
    }


    public SousMenu(int icon, String name, Boolean isActive, Boolean isPhoneNumber) {
        super();
        this.icon = icon;
        this.name = name;
        this.isActive = isActive;
        this.isPhoneNumber = isPhoneNumber;
    }


    public int getIcon() {
        return icon;
    }


    public void setIcon(int icon) {
        this.icon = icon;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public Boolean getIsActive() {
        return isActive;
    }


    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }


    public Boolean getIsPhoneNumber() {
        return isPhoneNumber;
    }


    public void setIsPhoneNumber(Boolean isPhoneNumber) {
        this.isPhoneNumber = isPhoneNumber;
    }


    @Override
    public String toString() {
        return "SousMenu [name=" + name + ", isNumber=" + isActive + "]";
    }

}
