package com.CPIS498.delanilltaqnia;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.CPIS498.delanilltaqnia.models.Certificate;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class ViewCertificateActivity extends AppCompatActivity {
    TextView txtCFWelcome, txtCFbrief;
    ProgressBar progressBarCertificates;
    ImageView ivCfLogo;
    FirebaseFirestore mFireStore;
    LinearLayoutCompat lyCFInformation;
    AppCompatButton btnLink;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_certificate);

        //initialize Variables
        txtCFWelcome = findViewById(R.id.txt_cf_welcome);
        txtCFbrief = findViewById(R.id.txt_cf_brief);
        ivCfLogo = findViewById(R.id.iv_certificate_logo);
        progressBarCertificates = findViewById(R.id.progress_cf);
        btnLink = findViewById(R.id.btn_external_link);
        btnBack=findViewById(R.id.btn_back);


        lyCFInformation = findViewById(R.id.ly_cf_info);


        mFireStore = FirebaseFirestore.getInstance();

        //Get cf id sent with intent
        Intent intent = getIntent();
        String cf_id = intent.getStringExtra("certificate_id");

        //get Certificate info from firebase
        getCFInforamtion(cf_id);

        //set onclick back arrow
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void getCFInforamtion(String cf_id) {
        DocumentReference document = mFireStore.collection("certificates").document(cf_id);
        document.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            Map<String, Object> data = document.getData();
                            //add uid to map
                            data.put("id", document.getId());
                            Certificate certificate = new Certificate(data);
                            //fill texts with info
                            txtCFbrief.setText(certificate.getBrief());
                            txtCFWelcome.setText("Hello , \nYou are in " + certificate.getTitle());

                            btnLink.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (certificate.getUrl() == null || certificate.getUrl().isEmpty())
                                        return;
                                    Uri uri = Uri.parse(certificate.getUrl()); // missing 'http://' will cause crashed
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(intent);
                                }
                            });


                            //get logo url
                            //load logo

                            String logourl = certificate.getLogo();
                            Glide.with(ViewCertificateActivity.this)
                                    .load(logourl)
                                    .placeholder(R.drawable.ic_if_badge)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(ivCfLogo);


                            //hide progress
                            progressBarCertificates.setVisibility(View.GONE);
                            //show hidden info layout
                            lyCFInformation.setVisibility(View.VISIBLE);

                            //hide progress
                            progressBarCertificates.setVisibility(View.GONE);
                            //show hidden info layout
                            lyCFInformation.setVisibility(View.VISIBLE);


                        } else {
                            Toast.makeText(ViewCertificateActivity.this, "Failed to retrieve CF info :" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }
}