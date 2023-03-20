package com.CPIS498.delanilltaqnia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.CPIS498.delanilltaqnia.services.FirebaseHelper;

public class LoginActivity extends AppCompatActivity {
    TextView txtSignUp;
    EditText txtEmail,txtPassword;
    AppCompatButton btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //define layout components
         txtSignUp=findViewById(R.id.txt_sign_up);
         txtEmail=findViewById(R.id.txt_email);
         txtPassword=findViewById(R.id.txt_password);
         btnLogin=findViewById(R.id.btn_login);
        //set passwords mode
        txtPassword.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        //set text click listener to move to signup form
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });


        //set Log in click listener
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get info from fields
                String email=txtEmail.getText().toString();
                String password=txtPassword.getText().toString();
                //check empty variables
                if(email.trim().isEmpty() ||password.trim().isEmpty())
                {
                    Toast.makeText(LoginActivity.this,"You should fill all fields",Toast.LENGTH_SHORT).show();
                    return;
                }

                //free to call firebase login
                FirebaseHelper firebaseHelper=new FirebaseHelper(LoginActivity.this);
                firebaseHelper.login(email,password);
            }
        });

    }


}