package com.example.finance.sampledata;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FinanceModelDAO {
    @Insert
    void insert(FinanceModel transaction);

    @Query("SELECT * FROM financeModel")
    LiveData<List<FinanceModel>> getAllTransactions();

    @Query("DELETE FROM financeModel WHERE id = :id")
    void delete(int id);
}