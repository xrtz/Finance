package com.example.finance.sampledata;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {FinanceModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FinanceModelDAO financeModelDao();
}