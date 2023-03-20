package com.CPIS498.delanilltaqnia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.CPIS498.delanilltaqnia.adapters.RequestsScrollAdapter;
import com.CPIS498.delanilltaqnia.models.Request;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class AllRequestActivity extends AppCompatActivity {
    RecyclerView requestsRecycleView;
    FirebaseFirestore mFireStore;
    ProgressBar progressBarRequests;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_request);
        requestsRecycleView=findViewById(R.id.rv_requests);
        progressBarRequests=findViewById(R.id.progress_requests);
        mFireStore=FirebaseFirestore.getInstance();


        //get all requests in store
        getAllRequests();

    }

    private void getAllRequests() {
        List<Request> requestList=new ArrayList<Request>();
        Query query=mFireStore.collection("requests");
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String,Object> data=document.getData();
                                Timestamp requestTimeStamp= (Timestamp) data.get("request_date");
                                Date requestDate = requestTimeStamp.toDate();
                                Request request=new Request();
                                request.setId(document.getId());
                                request.setRequest_date(requestDate);
                                request.setRequest_data((Map<String, String>) data.get("request_data"));
                                request.setRequest_type(data.get("request_type").toString());
                                request.setRequest_user(data.get("request_user").toString());

                                requestList.add(request);
                            }

                            //initiate adapter
                            RequestsScrollAdapter adapter=new RequestsScrollAdapter(AllRequestActivity.this,requestList);
                            requestsRecycleView.setAdapter(adapter);


                        }
                        else{
                            Toast.makeText(AllRequestActivity.this,"Failed to retrieve requests  :"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                        //hide progress bar
                        progressBarRequests.setVisibility(View.GONE);

                    }
                });
    }
}