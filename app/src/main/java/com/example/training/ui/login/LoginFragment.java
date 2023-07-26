package com.example.training.ui.login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;


import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.training.MainActivity;
import com.example.training.R;


public class LoginFragment extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiy_login);

        TextView password = findViewById(R.id.password);
        TextView username = findViewById(R.id.username);

        Button loginbtn = findViewById(R.id.loginbtn);
        loginbtn.setOnClickListener(v -> {
            if (password.getText().toString().equals("admin") && username.getText().toString().equals("admin")){
                Toast.makeText(LoginFragment.this,"Login Successful",Toast.LENGTH_LONG).show();
                Intent home = new Intent(LoginFragment.this, MainActivity.class);
                startActivity(home);
            }
            else{
                Toast.makeText(LoginFragment.this,"Invalid Password or Username",Toast.LENGTH_LONG).show();
            }
        });
    }
}
