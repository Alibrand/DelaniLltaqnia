package com.CPIS498.delanilltaqnia.services;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.CPIS498.delanilltaqnia.AdminActivity;
import com.CPIS498.delanilltaqnia.HomeActivity;
import com.CPIS498.delanilltaqnia.models.ComputingField;
import com.CPIS498.delanilltaqnia.models.FireUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FirebaseHelper {
    Context mContext;
    FirebaseAuth mAuth;
    FirebaseFirestore mFireStore;
    ProgressDialog progressDialog;
    MySharedPreferences mySharedPreferences;



    public FirebaseHelper(Context mContext) {
        this.mContext = mContext;
        mAuth = FirebaseAuth.getInstance();
        mFireStore = FirebaseFirestore.getInstance();
        mySharedPreferences = new MySharedPreferences(mContext);
    }

    public void registerNewUser(FireUser user) {
        showDialog("Please Wait...");
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener((Activity) mContext, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(mContext, "User created Successfully", Toast.LENGTH_SHORT).show();
                            FirebaseUser newUser = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(user.getName()).build();
                            newUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isComplete()) {
                                        //set is logged to true
                                        mySharedPreferences.setIsLogged(true);
                                        //navigate to home screen
                                        Intent intent = new Intent(mContext, HomeActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        mContext.startActivity(intent);
                                    } else {
                                        Log.w("TAG", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(mContext, "Create User Failed:" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });


                        } else {
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(mContext, "Create User Failed:" + task.getException().getMessage(), Toast.LENGTH_LONG).show();

                        }
                        dismissDialog();
                    }
                });
    }

    public void login(String email, String password) {
        showDialog("Please Wait...");
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                              //set is logged to true
                            mySharedPreferences.setIsLogged(true);
                            String loggedUserEmail=user.getEmail();
                            //check if admin
                            if(loggedUserEmail.equals("admin@delani.com"))
                            {
                                //navigate to admin page
                                Intent intent = new Intent(mContext, AdminActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                mContext.startActivity(intent);
                            }
                            else
                            {//navigate to home screen
                            Intent intent = new Intent(mContext, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            mContext.startActivity(intent);}
                        } else {
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(mContext, "Login Failed:" + task.getException().getMessage(), Toast.LENGTH_LONG).show();

                        }
                        dismissDialog();
                    }
                });
    }



    public void resetPassword(String email) {
        showDialog("Please Wait...");
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(mContext, "A link was sent to " + email, Toast.LENGTH_LONG).show();
                        } else {
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(mContext, "Sending link failed:" + task.getException().getMessage(), Toast.LENGTH_LONG).show();

                        }
                        dismissDialog();
                    }
                });
    }

    public FireUser getLoggedUser() {
        FireUser user = new FireUser();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            user.setName(currentUser.getDisplayName());
            user.setEmail(currentUser.getEmail());
            user.setUid(currentUser.getUid());
            return user;
        } else
            return null;

    }

    public void logout() {
        mAuth.signOut();
        mySharedPreferences.setIsLogged(false);
    }




    //dialog functions

    private void showDialog(String message) {

        progressDialog = new ProgressDialog(mContext);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    private void dismissDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
