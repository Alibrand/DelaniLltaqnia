package com.CPIS498.delanilltaqnia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.CPIS498.delanilltaqnia.adapters.CoursesScrollAdapter;
import com.CPIS498.delanilltaqnia.models.Course;
import com.CPIS498.delanilltaqnia.services.FirebaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {

    ImageView btnLogOut;
    AppCompatButton btnRequests,btnExperts;
    FirebaseHelper mFirehelper;
    FirebaseFirestore mFireStore;
    TextView requestCountBadge;
    int requestcount=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        //initiate variables
        btnLogOut=findViewById(R.id.btn_logout);
        requestCountBadge=findViewById(R.id.badge_request_count);
        btnRequests=findViewById(R.id.btn_requests);
        btnExperts=findViewById(R.id.btn_experts);
        mFirehelper=new FirebaseHelper(this);
        mFireStore=FirebaseFirestore.getInstance();

        //get requests count
        getRequestsCount();


        //experts listener
        btnExperts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminActivity.this,AddExpertActivity.class);
                startActivity(intent);
            }
        });


        //requests button listener
        btnRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminActivity.this,AllRequestActivity.class);
                startActivity(intent);
            }
        });





        //Logout button listener
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirehelper.logout();
                //go back to welcome screen
                Intent intent=new Intent(AdminActivity.this, WelcomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });
    }

    private void getRequestsCount() {
        //show loading
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.show();

        Query query=mFireStore.collection("requests");
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                               requestcount++;

                            }
                            if(requestcount==0)
                                requestCountBadge.setVisibility(View.INVISIBLE);
                            else
                                requestCountBadge.setText(String.valueOf(requestcount) );

                        }
                        else{
                            Toast.makeText(AdminActivity.this,"Failed to retrieve requests count :"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                        //hide progress bar
                       progressDialog.dismiss();

                    }
                });



    }
}