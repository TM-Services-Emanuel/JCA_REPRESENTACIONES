package com.example.latinodistribuidora.Modelos;

import java.io.Serializable;

public class Division implements Serializable {

    public int iddivision;
    public String division;

    public Division(int iddivision, String division){
        setIddivision(iddivision);
        setDivision(division);
    }
    public Division(){
    }

    public int getIddivision() {
        return iddivision;
    }

    public void setIddivision(int iddivision) {
        this.iddivision = iddivision;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }
}
