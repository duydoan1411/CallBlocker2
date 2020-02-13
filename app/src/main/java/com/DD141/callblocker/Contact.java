package com.DD141.callblocker;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "contacts")
public class Contact {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id = 0;
    private String name, number;

    public Contact(){

    }
    @Ignore
    public Contact(String name, String number){
        this.name = name;
        this.number = number;
    }


    public int getId(){
        return id;
    }


    public void setId(int id){
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public String getNumber() {
        return number;
    }


    public void setName(String name) {
        this.name = name;
    }


    public void setNumber(String number) {
        this.number = number;
    }

    @Ignore
    @NonNull
    @Override
    public String toString() {
        return "[" + id + "]" + "[" + name +"]" +"["+number+"]";
    }
}
