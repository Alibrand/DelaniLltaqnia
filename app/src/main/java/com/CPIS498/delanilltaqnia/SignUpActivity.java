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


import com.CPIS498.delanilltaqnia.models.FireUser;
import com.CPIS498.delanilltaqnia.services.FirebaseHelper;

public class SignUpActivity extends AppCompatActivity {
    TextView txtLogin;
    EditText txtName,txtEmail,txtPassword,txtPasswordConfirm;
    AppCompatButton btnSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //define layout components
         txtLogin=findViewById(R.id.txt_log_in);
         txtName=findViewById(R.id.txt_name);
         txtEmail=findViewById(R.id.txt_email);
         txtPassword=findViewById(R.id.txt_password);
         txtPasswordConfirm=findViewById(R.id.txt_password_confirm);
         btnSignUp=findViewById(R.id.btn_sign_up);
        //set passwords mode
        txtPassword.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        txtPasswordConfirm.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);

        //set text click listener to move to login form
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //jump to Login Activity
                Intent intent =new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //set sign up click listener
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get info from fields
                String name=txtName.getText().toString();
                String email=txtEmail.getText().toString();
                String password=txtPassword.getText().toString();
                String passwordConfirm=txtPasswordConfirm.getText().toString();
                //check empty variables
                if(name.trim().isEmpty()||email.trim().isEmpty()
                        ||password.trim().isEmpty() ||passwordConfirm.trim().isEmpty())
                {
                    Toast.makeText(SignUpActivity.this,"You should fill all fields",Toast.LENGTH_SHORT).show();
                    return;
                }
                //check if passwords match
                if(!password.equals(passwordConfirm))
                {
                    Toast.makeText(SignUpActivity.this,"Passwords don't match",Toast.LENGTH_SHORT).show();
                    return;
                }
                //free to call firebase signup
                FirebaseHelper firebaseHelper=new FirebaseHelper(SignUpActivity.this);
                FireUser newUser=new FireUser();
                newUser.setName(name);
                newUser.setEmail(email);
                newUser.setPassword(password);
                firebaseHelper.registerNewUser(newUser);

            }
        });
    }
}