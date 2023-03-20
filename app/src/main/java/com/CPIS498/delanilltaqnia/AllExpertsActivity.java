package com.CPIS498.delanilltaqnia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.CPIS498.delanilltaqnia.adapters.ExpertsScrollAdapter;
import com.CPIS498.delanilltaqnia.models.Expert;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AllExpertsActivity extends AppCompatActivity {
    ProgressBar progressBarExperts;
    FirebaseFirestore mFireStore;
    RecyclerView expertsRecycleView;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_experts);

        //define components
        mFireStore=FirebaseFirestore.getInstance();
        expertsRecycleView=findViewById(R.id.rv_experts);
        progressBarExperts=findViewById(R.id.progress_experts);
        btnBack=findViewById(R.id.btn_back);

        //set onclick back arrow
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Get all cf from firebase
        getAllExperts();
    }

    public  void getAllExperts()  {
        //show loading
        progressBarExperts.setVisibility(View.VISIBLE);
        //clear current results
        expertsRecycleView.setAdapter(null);
        List<Expert> experts = new ArrayList<Expert>();
        Query query=mFireStore.collection("experts");
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                data.put("id",document.getId());
                                Expert expert=new Expert(data);
                                experts.add(expert);

                            }
                            if(experts.size()==0)
                                Toast.makeText(AllExpertsActivity.this,"No Expert was found",Toast.LENGTH_LONG).show();


                            // Set adapter on recycler view
                            ExpertsScrollAdapter adapter=new ExpertsScrollAdapter(AllExpertsActivity.this, experts);
                            expertsRecycleView.setAdapter(adapter);

                        }
                        else{
                            Toast.makeText(AllExpertsActivity.this,"Failed to retrieve Cources list :"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                        //hide progress bar
                        progressBarExperts.setVisibility(View.GONE);

                    }
                });



    }
}