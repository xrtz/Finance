package com.example.finance.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.finance.R;
import com.example.finance.sampledata.FinanceModel;

import java.util.Calendar;

public class CustomDialog extends Dialog {

    private final Function listener;
    private DatePickerDialog datePickerDialog;
    private TextView tvDate;
    private int a;

    public interface Function {
        void onAdd(FinanceModel transaction);
    }

    public CustomDialog(@NonNull Context context, Function listener, int a) {
        super(context);
        this.listener = listener;
        this.a = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (a == 1)
            setContentView(R.layout.custom_dialog);
        else
            setContentView(R.layout.custom_dialog_inc);

        setupDatePicker();
        setupSpinner();
        setupButtons();
    }

    private void setupDatePicker() {
        tvDate = findViewById(R.id.tv_date_cust);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, selectedYear, selectedMonth, dayOfMonth) ->
                        tvDate.setText(dayOfMonth + "." + (selectedMonth + 1) + "." + selectedYear),
                year,
                month,
                day
        );

        ImageButton btnDate = findViewById(R.id.imb_date_cust);
        btnDate.setOnClickListener(v -> datePickerDialog.show());
    }

    private void setupSpinner() {
        Spinner spinner = findViewById(R.id.spinner);
        String[] category;
        if (a == 2) category = new String[]{"Аванс", "Зарплата", "Переводы", "Кешбек", "Другое"};

        else category = new String[]{"Развлечения", "Продукты", "Одежда", "Такси", "Другое"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                category
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void setupButtons() {
        if (a ==1){
            EditText name = findViewById(R.id.edt_name_cust);
            Button btnAdd = findViewById(R.id.button_cust_1);
            btnAdd.setOnClickListener(v -> {
                Spinner spinner = findViewById(R.id.spinner);
                EditText amount = findViewById(R.id.edt_cost_cust);
                if (amount.getText().toString().isEmpty()) {
                    amount.setError("Введите сумму");
                    return;
                }
                if (name.getText().toString().isEmpty()) {
                    name.setError("Введите название");
                    return;
                }
                FinanceModel transaction = new FinanceModel(
                        Long.parseLong(amount.getText().toString()),
                        "expenses",
                        spinner.getSelectedItem().toString(),
                        tvDate.getText().toString(),
                        name.getText().toString()
                );

                if (listener != null) {
                    listener.onAdd(transaction);
                }
                dismiss();
            });
        }
        else {
            Button btnAdd = findViewById(R.id.button_cust_1);
            btnAdd.setOnClickListener(v -> {
                Spinner spinner = findViewById(R.id.spinner);
                EditText amount = findViewById(R.id.edt_cost_cust);
                if (amount.getText().toString().isEmpty()) {
                    amount.setError("Введите сумму");
                    return;
                }
                FinanceModel transaction = new FinanceModel(
                        Long.parseLong(amount.getText().toString()),
                        "income",
                        spinner.getSelectedItem().toString(),
                        tvDate.getText().toString(),
                        "name1"
                );

                if (listener != null) {
                    listener.onAdd(transaction);
                }
                dismiss();
            });
        }
        Button btnCancel = findViewById(R.id.button_cust_2);
        btnCancel.setOnClickListener(v -> dismiss());
    }
}