package com.example.inventoryobat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeActivity extends AppCompatActivity {
    private static final long DELAY_MS = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        TextView tvWelcomeTitle = findViewById(R.id.textWelcomeTitle);
        TextView tvWelcomeMessage = findViewById(R.id.textWelcomeMessage);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null && user.getEmail() != null) {
            String email = user.getEmail();

            String username = email.split("@")[0];

            if (username.length() > 0) {
                username = username.substring(0, 1).toUpperCase() + username.substring(1);
            }

            tvWelcomeTitle.setText("Selamat Datang, " + username + "!");
            tvWelcomeMessage.setText("Anda login sebagai: " + email + "\nSemoga hari Anda menyenangkan!");
        } else {
            tvWelcomeTitle.setText("Selamat Datang!");
        }

        new Handler().postDelayed(() -> {
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            finish();
        }, DELAY_MS);
    }
}