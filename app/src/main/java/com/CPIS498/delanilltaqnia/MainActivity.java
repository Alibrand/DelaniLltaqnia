package com.CPIS498.delanilltaqnia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity {
    AppCompatButton btnGoLogin,btnGuest;
    TextView txtSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGoLogin=findViewById(R.id.btn_login);
        btnGuest=findViewById(R.id.btn_guset);
        txtSignUp=findViewById(R.id.txt_sign_up);
        //set on click listeners
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(MainActivity.this,SignUpActivity.class);
               startActivity(intent);
            }
        });
        btnGoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        btnGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}