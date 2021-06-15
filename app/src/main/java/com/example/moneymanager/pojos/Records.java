package com.example.moneymanager.pojos;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tbl_records")
public class Records {

    private int managerId;
    private String amount;
    private String date;
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String type;
    private String month;

    public Records(int id, int managerId, String amount, String date, String type,String month) {
        this.managerId = managerId;
        this.amount = amount;
        this.date = date;
        this.type = type;
        this.id = id;
        this.month = month;
    }

    public int getManagerId() {
        return managerId;
    }

    public String getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getMonth(){ return month;}
}
