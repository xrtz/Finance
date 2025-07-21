package com.example.finance.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.finance.R;
import com.example.finance.SignInActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class AccFragment extends Fragment {


    public AccFragment() {

    }
    public static AccFragment newInstance() {
        AccFragment fragment = new AccFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_acc, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        TextView tv_email = view.findViewById(R.id.tv_login);
        tv_email.setText(auth.getCurrentUser().getEmail());
        ImageButton imb_logout = view.findViewById(R.id.imb_logout);
        TextView tv_name = view.findViewById(R.id.textView4);
        if (auth.getCurrentUser() != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(auth.getCurrentUser().getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String name = document.getString("name");
                                tv_name.setText(name);
                            }
                        }
                    });
        }
        imb_logout.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  FirebaseAuth.getInstance().signOut();
                  startActivity(new Intent(requireContext(), SignInActivity.class));
                  requireActivity().finish();
              }
          }
        );
    }
}