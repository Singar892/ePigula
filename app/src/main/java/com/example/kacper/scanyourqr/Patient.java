package com.example.kacper.scanyourqr;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Kacper on 23.01.2018.
 */

public class Patient {

    private String forname;
    private String surname;
    private String number;
    private List<Temperature> temperatures = new ArrayList<>();
    List<String> stringsTemps = new ArrayList<>();

    public Patient(String forname, String surname, String number) {
        this.forname = forname;
        this.surname = surname;
        this.number = number;
    }

    public String getForname() {
        return forname;
    }

    public void setForname(String forname) {
        this.forname = forname;
    }

    public String getSurname() {
        return surname;
    }

    public List<String> getStringsTemps() {
        return stringsTemps;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setTemperatures(List<Temperature> temperatures) {
        this.temperatures = temperatures;
    }

    public String getNumber() {
        return number;
    }

    public List<Temperature> getTemperatures() {
        return temperatures;
    }

    public void addTemperature(double tempValue) {
        Temperature temp = new Temperature(tempValue);
        temperatures.add(temp);
        Calendar cal = Calendar.getInstance();
        cal.setTime(temp.date);
        stringsTemps.add(0, cal.get(Calendar.YEAR)+"-"+String.format("%02d",cal.get(Calendar.MONTH)+1)+"-"+String.format("%02d", cal.get(Calendar.DAY_OF_MONTH))
                +" "+temp.date.getHours()+":"+temp.date.getMinutes()+"        "+temp.value);
    }
}
