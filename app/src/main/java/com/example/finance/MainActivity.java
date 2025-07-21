package com.example.finance;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import com.example.finance.fragments.HomeFragment;
import com.example.finance.sampledata.AppDatabase;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private static AppDatabase database_exp;
    private static AppDatabase database_inc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        database_inc = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                "financeinc-db"
        ).build();
        database_exp = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                "financeexp-db"
        ).build();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }
        else{
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(
                            R.id.mainLayout,
                            new HomeFragment()
                    )
                    .commit();
        }
    }
    public static AppDatabase getDatabase_exp() {
        return database_exp;
    }
    public static AppDatabase getDatabase_inc() {
        return database_inc;
    }
}