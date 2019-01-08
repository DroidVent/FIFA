package com.firstbit.fifaworldcup2018highlights.data;

import java.util.ArrayList;

public class Group {
    private String group;
    private ArrayList<Standing> standings;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public ArrayList<Standing> getStandings() {
        return standings;
    }

    public void setStandings(ArrayList<Standing> standings) {
        this.standings = standings;
    }
}
