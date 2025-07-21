package com.example.finance.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finance.MainActivity;
import com.example.finance.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class RegisterFragment extends Fragment {
    private EditText etEmail, etPassword, edtName;
    private FirebaseAuth mAuth;

    public RegisterFragment() {
    }

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        edtName = view.findViewById(R.id.edt_name);
        etEmail = view.findViewById(R.id.edt_reg_email);
        etPassword = view.findViewById(R.id.edt_reg_pass);
        Button btnRegister = view.findViewById(R.id.btn_toAppFromReg);
        btnRegister.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty() || edtName.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        saveUserToDatabase();
                        startActivity(new Intent(requireContext(), MainActivity.class));
                        Toast.makeText(getContext(), "Аккаунт зарегистрирован",
                                Toast.LENGTH_SHORT).show();
                        requireActivity().finish();
                    } else {
                        Toast.makeText(getContext(), "Ошибка",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserToDatabase() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Map<String, Object> userData = new HashMap<>();
            userData.put("email", user.getEmail());
            userData.put("name", edtName.getText().toString());
            userData.put("createdAt", new Date());

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(user.getUid())
                    .set(userData)
                    .addOnSuccessListener(aVoid -> Log.d("TAG", "User saved"))
                    .addOnFailureListener(e -> Log.e("TAG", "Error saving user", e));
        }
    }
}