package com.CPIS498.delanilltaqnia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.CPIS498.delanilltaqnia.adapters.ComputingFieldsScrollAdapter;
import com.CPIS498.delanilltaqnia.models.ComputingField;
import com.CPIS498.delanilltaqnia.services.FirebaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AllCFActivity extends AppCompatActivity {
    ProgressBar progressBarCf;
    FirebaseFirestore mFireStore;
    RecyclerView computingFieldsRecycleView;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_cfactivity);
        //define components
        mFireStore=FirebaseFirestore.getInstance();
        computingFieldsRecycleView=findViewById(R.id.rv_computing_fields);
        progressBarCf=findViewById(R.id.progress_cf);
        btnBack=findViewById(R.id.btn_back);

        //set onclick back arrow
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //Get all cf from firebase
        getAllComputingFields();

    }

    public  void getAllComputingFields()  {
        progressBarCf.setVisibility(View.VISIBLE);
        List<ComputingField> fields = new ArrayList<ComputingField>();
        Query query=mFireStore.collection("computing_fields");
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                //add uid to map
                                data.put("id", document.getId());
                                ComputingField field = new ComputingField(data);
                                fields.add(field);

                            }
                             progressBarCf.setVisibility(View.GONE);
                            // Set adapter on recycler view
                            ComputingFieldsScrollAdapter adapter=new ComputingFieldsScrollAdapter(AllCFActivity.this, fields);
                            computingFieldsRecycleView.setAdapter(adapter);

                        }
                        else{
                            Toast.makeText(AllCFActivity.this,"Failed to retrieve CF list :"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }

                    }
                });



    }
}