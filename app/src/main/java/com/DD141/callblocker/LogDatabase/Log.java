package com.DD141.callblocker.LogDatabase;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;


@Entity (tableName = "logs")
public class Log {
    @NonNull
    @PrimaryKey(autoGenerate =  true)
    private int id = 0;
    private String name, number;
    private Date date;

    @Ignore
    private Boolean isHeader;


    @Ignore
    private String header;

    public Log(){

    }

    @Ignore
    public Log(String header) {
        this.header = header;
        this.isHeader = true;
    }

    @Ignore
    public Log(String name, String number, Date date) {
        this.name = name;
        this.number = number;
        this.date = date;
        this.isHeader = false;
    }

    @Ignore
    public String getHeader() {
        return header;
    }

    @Ignore
    public void setHeader(String header) {
        this.header = header;
    }

    @Ignore
    public boolean getIsHeader() {
        return isHeader;
    }

    @Ignore
    public void setIsHeader(boolean header) {
        this.isHeader = header;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Ignore
    @NonNull
    @Override
    public String toString() {
        return "["+id+"]["+name+"]["+number+"]["+date.toString()+"]";
    }
}
