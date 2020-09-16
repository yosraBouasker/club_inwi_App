package com.inwi.clubinwi.Beans;

import java.util.ArrayList;

public class Menu {
    private String name;
    private int icon;
    private int count;
    private ArrayList<SousMenu> mSubmenu = new ArrayList<SousMenu>();

    public Menu() {
        super();
    }

    public Menu(String name, int icon, int count, ArrayList<SousMenu> mSubmenu) {
        super();
        this.name = name;
        this.icon = icon;
        this.count = count;
        this.mSubmenu = mSubmenu;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<SousMenu> getmSubmenu() {
        return mSubmenu;
    }

    public void setmSubmenu(ArrayList<SousMenu> mSubmenu) {
        this.mSubmenu = mSubmenu;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Menu [name=" + name + ", icon=" + icon + ", mSubmenu=" + mSubmenu + "]";
    }

}
