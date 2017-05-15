package com.example.kimja.a9th_homework;

import java.text.NumberFormat;

/**
 * Created by kimja on 2017-05-11.
 */

public class data {
    String year;
    String month;
    String date;
    String memo;
    data(String year, String month,String date , String memo){
        this.year = year;

        this.memo = memo;
        this.month = String.format("%02d",Integer.parseInt(month)+1);
        this.date = String.format("%02d",Integer.parseInt(date));
    }


    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getDate() {
        return date;
    }

    public String getMemo() {
        return memo;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setMonth(String month) {
        this.month = month+1;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Override
    public String toString() {
        return year+"-"+month+"-"+date+".memo";
    }
}
