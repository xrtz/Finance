package com.example.finance.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finance.MainActivity;
import com.example.finance.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginFragment extends Fragment {

    private EditText etEmail, etPassword;
    private FirebaseAuth mAuth;

    public LoginFragment() {
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        etEmail = view.findViewById(R.id.edt_log_email);
        etPassword = view.findViewById(R.id.edt_log_pass);
        Button btnLogin = view.findViewById(R.id.btn_toApp);
        Button btnReg = view.findViewById(R.id.btn_toReg);
        btnLogin.setOnClickListener(v -> loginUser());
        btnReg.setOnClickListener(v-> goToRegister());
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Успешно",
                                Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(requireContext(), MainActivity.class));
                        requireActivity().finish();
                    } else {
                        Toast.makeText(getContext(), "Ошибка",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void goToRegister() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.signinlayout, new RegisterFragment())
                .addToBackStack(null)
                .commit();
    }
}