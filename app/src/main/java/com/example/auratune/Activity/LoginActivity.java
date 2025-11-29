package com.example.auratune.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.ImageView;
import android.text.InputType;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.auratune.R;

public class LoginActivity extends AppCompatActivity {
    private EditText userEdit, passEdit;
    private Button loginBtn;
    private TextView forgetText;
    private ImageView fbBtn, ggBtn, ttBtn; // added social buttons

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initView();
        setVariable();
    }
    private void setVariable(){
        loginBtn.setOnClickListener(v -> {
            if (userEdit.getText().toString().isEmpty() &&
                    passEdit.getText().toString().isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please complete filling the login form", Toast.LENGTH_SHORT).show();
            }else if (userEdit.getText().toString().equals("aura") &&
                    passEdit.getText().toString().equals("1")) {
                startActivity(new Intent(LoginActivity.this, MenuPlayerActivity.class));
            }
        });

        forgetText.setOnClickListener(v -> {
            final EditText input = new EditText(LoginActivity.this);
            input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            input.setHint("Enter your email");

            new AlertDialog.Builder(LoginActivity.this)
                .setTitle("Reset Password")
                .setMessage("Enter your account email to receive a password reset link.")
                .setView(input)
                .setPositiveButton("Send", (dialog, which) -> {
                    String email = input.getText() != null ? input.getText().toString().trim() : "";
                    if (email.isEmpty()) {
                        Toast.makeText(LoginActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "A password reset link has been sent to " + email, Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
        });

        // Social buttons: simple demo handlers (replace with real OAuth flows)
        fbBtn.setOnClickListener(v -> {
            Toast.makeText(LoginActivity.this, "Continue with Facebook", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, MenuPlayerActivity.class));
        });
        ggBtn.setOnClickListener(v -> {
            Toast.makeText(LoginActivity.this, "Continue with Google", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, MenuPlayerActivity.class));
        });
        ttBtn.setOnClickListener(v -> {
            Toast.makeText(LoginActivity.this, "Continue with Twitter", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, MenuPlayerActivity.class));
        });
    }
    private void initView(){
        userEdit = findViewById(R.id.editTextText);
        passEdit = findViewById(R.id.editTextTextPassword);
        loginBtn = findViewById(R.id.btnLogin);
        forgetText = findViewById(R.id.textView4);
        fbBtn = findViewById(R.id.imageView5);
        ggBtn = findViewById(R.id.imageView6);
        ttBtn = findViewById(R.id.imageView8);
    }
}