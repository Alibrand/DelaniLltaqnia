package com.CPIS498.delanilltaqnia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.CPIS498.delanilltaqnia.models.FireUser;
import com.CPIS498.delanilltaqnia.services.FirebaseHelper;
import com.CPIS498.delanilltaqnia.services.MySharedPreferences;

public class WelcomeActivity extends AppCompatActivity {
    AppCompatButton btnLetsGo;
    MySharedPreferences mySharedPreferences;
    FirebaseHelper mFirehelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        btnLetsGo=findViewById(R.id.btn_letsgo)  ;
        mySharedPreferences=new MySharedPreferences(WelcomeActivity.this);
        mFirehelper=new FirebaseHelper(this);
        //if user logged go to home activity
        checkIfUserLoggedIn();

        btnLetsGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(WelcomeActivity.this,MainActivity.class);
                startActivity(intent);

            }
        });


    }

    private void checkIfUserLoggedIn() {
        //check if a user has logged to system
        Boolean logged=mySharedPreferences.getIsLogged();
         if(logged==true)
         {
             FireUser loggedUser=mFirehelper.getLoggedUser();
             String userEmail=loggedUser.getEmail();
             //check if user s the admin
             if(userEmail.equals("admin@delani.com"))
             {
                 //navigate to admin page
                 Intent intent = new Intent(this, AdminActivity.class);
                 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                 startActivity(intent);
             }
             else
             {//navigate to home screen
                 Intent intent = new Intent(this, HomeActivity.class);
                 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                 startActivity(intent);}


         }


    }


}