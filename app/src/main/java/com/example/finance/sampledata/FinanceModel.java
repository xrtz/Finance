package com.example.finance.sampledata;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "financeModel")
public class FinanceModel {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private long amount;
    private String type;
    private String nameOfTransaction;
    private String category;
    private String date;

    public FinanceModel(long amount, String type, String category, String date, String nameOfTransaction) {
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.date = date;
        this.nameOfTransaction = nameOfTransaction;
    }

    public String getNameOfTransaction() {
        return nameOfTransaction;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }
}