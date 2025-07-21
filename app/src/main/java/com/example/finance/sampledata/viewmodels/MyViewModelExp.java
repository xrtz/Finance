package com.example.finance.sampledata.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.finance.MainActivity;
import com.example.finance.sampledata.AppDatabase;
import com.example.finance.sampledata.FinanceModel;

import java.util.List;

public class MyViewModelExp extends AndroidViewModel {
    private final LiveData<List<FinanceModel>> allTransactions;

    public MyViewModelExp(@NonNull Application application) {
        super(application);
        AppDatabase db = MainActivity.getDatabase_exp();
        allTransactions = db.financeModelDao().getAllTransactions();
    }

    public LiveData<List<FinanceModel>> getAllTransactions() {
        return allTransactions;
    }
}