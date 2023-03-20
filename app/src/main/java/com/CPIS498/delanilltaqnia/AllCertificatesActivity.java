package com.CPIS498.delanilltaqnia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.CPIS498.delanilltaqnia.adapters.CertificatesScrollAdapter;
import com.CPIS498.delanilltaqnia.adapters.CoursesScrollAdapter;
import com.CPIS498.delanilltaqnia.models.Certificate;
import com.CPIS498.delanilltaqnia.models.Course;
import com.CPIS498.delanilltaqnia.models.FireUser;
import com.CPIS498.delanilltaqnia.services.FirebaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AllCertificatesActivity extends AppCompatActivity {
    ProgressBar progressBarCertificates;
    FirebaseFirestore mFireStore;
    FirebaseHelper mFireHelper;
    RecyclerView certificatesRecycleView;
    AppCompatButton btnRequestCertificate;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_certificates);

        //define components
        mFireStore=FirebaseFirestore.getInstance();
        mFireHelper=new FirebaseHelper(AllCertificatesActivity.this);
        certificatesRecycleView =findViewById(R.id.rv_certificates);
        progressBarCertificates =findViewById(R.id.progress_certificates);
        btnBack=findViewById(R.id.btn_back);
        btnRequestCertificate=findViewById(R.id.btn_request_certificate);

        //set onclick back arrow
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //set on click request to certificate button
        btnRequestCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               FireUser loggedUser= mFireHelper.getLoggedUser();
               // if no user has logged to system
                //inform user that this operation require login
               if(loggedUser==null){
                   Snackbar.make(btnRequestCertificate,"This Operation requires user login",Snackbar.LENGTH_LONG).show();
               }
               else{
                   //go to request page
                   Intent intent=new Intent(AllCertificatesActivity.this,RequestCertificateActivity.class);
                   startActivity(intent);

               }
            }
        });


        //Get all cf from firebase
        getAllCertificates();
    }

    public  void getAllCertificates()  {
        List<Certificate> certificates = new ArrayList<Certificate>();
        Query query=mFireStore.collection("certificates");
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                Certificate certificate=new Certificate();

                                certificate.setId(document.getId());
                                certificate.setTitle(data.get("title").toString());
                                certificates.add(certificate);

                            }


                            // Set adapter on recycler view
                            CertificatesScrollAdapter adapter=new CertificatesScrollAdapter(AllCertificatesActivity.this, certificates);
                            certificatesRecycleView.setAdapter(adapter);

                        }
                        else{
                            Toast.makeText(AllCertificatesActivity.this,"Failed to retrieve Certificates list :"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                        progressBarCertificates.setVisibility(View.GONE);

                    }
                });



    }
}