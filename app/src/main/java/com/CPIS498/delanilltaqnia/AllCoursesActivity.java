package com.CPIS498.delanilltaqnia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.CPIS498.delanilltaqnia.adapters.CoursesScrollAdapter;
import com.CPIS498.delanilltaqnia.models.Course;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AllCoursesActivity extends AppCompatActivity {
    ProgressBar progressBarCoureses;
    FirebaseFirestore mFireStore;
    RecyclerView coursesRecycleView;
    ImageButton btnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_courses);
        //define components
        mFireStore=FirebaseFirestore.getInstance();
        coursesRecycleView=findViewById(R.id.rv_courses);
        progressBarCoureses=findViewById(R.id.progress_courses);
        btnBack=findViewById(R.id.btn_back);

        //set onclick back arrow
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //Get all cf from firebase
        getAllCourses();
    }

    public  void getAllCourses()  {
        //show loading
        progressBarCoureses.setVisibility(View.VISIBLE);
        //clear current results
        coursesRecycleView.setAdapter(null);
        List<Course> courses = new ArrayList<Course>();
        Query query=mFireStore.collection("courses");
       query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                Course course = new Course();
                                course.setId(document.getId());
                                course.setTitle(data.get("title").toString());
                                course.setLogo(data.get("logo").toString());
                                course.setLink(data.get("link").toString());
                                courses.add(course);

                            }
                            if(courses.size()==0)
                                Toast.makeText(AllCoursesActivity.this,"No Course was found",Toast.LENGTH_LONG).show();


                            // Set adapter on recycler view
                            CoursesScrollAdapter adapter=new CoursesScrollAdapter(AllCoursesActivity.this, courses);
                            coursesRecycleView.setAdapter(adapter);

                        }
                        else{
                            Toast.makeText(AllCoursesActivity.this,"Failed to retrieve Cources list :"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                        //hide progress bar
                        progressBarCoureses.setVisibility(View.GONE);

                    }
                });



    }
}