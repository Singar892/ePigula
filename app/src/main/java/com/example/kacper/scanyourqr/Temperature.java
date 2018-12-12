package com.example.kacper.scanyourqr;

import java.util.Comparator;
import java.util.Date;

public class Temperature {

    double value;
    Date date;

    public Temperature(double value) {
        this.value = value;
        this.date = new Date();
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    static class CompareTempAsc implements Comparator<Temperature> {

        @Override
        public int compare(Temperature o1, Temperature o2) {
            return o1.date.compareTo(o2.date);
        }
    }
}
