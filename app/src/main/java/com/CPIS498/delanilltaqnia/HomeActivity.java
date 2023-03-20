package com.CPIS498.delanilltaqnia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.CPIS498.delanilltaqnia.adapters.BooksScrollAdapter;
import com.CPIS498.delanilltaqnia.adapters.CertificatesScrollAdapter;
import com.CPIS498.delanilltaqnia.adapters.ComputingFieldsScrollAdapter;
import com.CPIS498.delanilltaqnia.adapters.CoursesScrollAdapter;
import com.CPIS498.delanilltaqnia.adapters.ExpertsScrollAdapter;
import com.CPIS498.delanilltaqnia.models.Book;
import com.CPIS498.delanilltaqnia.models.Certificate;
import com.CPIS498.delanilltaqnia.models.ComputingField;
import com.CPIS498.delanilltaqnia.models.Course;
import com.CPIS498.delanilltaqnia.models.Expert;
import com.CPIS498.delanilltaqnia.models.FireUser;
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

public class HomeActivity extends AppCompatActivity {
    TextView txtWelcome,txtSeeAllCF,txtSeeAllCources,txtSeeAllCertificates,txtSeeAllBooks,txtSeeAllExpets;
    FirebaseHelper firebaseHelper;
    ImageView btnLogOut;
    ProgressBar progressBarCf,progressBarCoureses,progressBarCertificates,progressBarBooks,progressBarExperts;
    FirebaseFirestore mFireStore;
    RecyclerView computingFieldsRecycleView,courcesRecycleView,certificatesRecylcleView,
            booksRecycleView,expertsRecycleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //define components
        txtWelcome=findViewById(R.id.txt_welcome);
        txtSeeAllCF=findViewById(R.id.txt_cf_see_all);
        txtSeeAllCources=findViewById(R.id.txt_cources_see_all);
        txtSeeAllCertificates=findViewById(R.id.txt_certificates_see_all);
        txtSeeAllBooks=findViewById(R.id.txt_books_see_all);
        txtSeeAllExpets=findViewById(R.id.txt_experts_see_all);
        btnLogOut=findViewById(R.id.btn_logout);
        firebaseHelper=new FirebaseHelper(this);
        mFireStore=FirebaseFirestore.getInstance();
        computingFieldsRecycleView=findViewById(R.id.rv_computing_fields);
        courcesRecycleView=findViewById(R.id.rv_courses);
        certificatesRecylcleView=findViewById(R.id.rv_certificates);
        booksRecycleView =findViewById(R.id.rv_books);
        expertsRecycleView=findViewById(R.id.rv_experts);
        progressBarCf=findViewById(R.id.progress_cf);
        progressBarCoureses=findViewById(R.id.progress_courses);
        progressBarCertificates=findViewById(R.id.progress_certificates);
        progressBarBooks=findViewById(R.id.progress_books);
        progressBarExperts=findViewById(R.id.progress_experts);
        //SET WELCOME TO USER
        setWelcomeText();
        //get computing fields to list - get only 5 fields to show
        //limit can be edited any time
        getComputingFields(5);
        //get courses to list - get only 5 courses to show
        //limit can be edited any time
        getCourses(5);
        //get certificates to list - get only 5 certificates to show
        //limit can be edited any time
        getCertificates(5);
        //get Books to list - get only 5 Books to show
        //limit can be edited any time
        getBooks(5);
        //get Books to list - get only 5 Books to show
        //limit can be edited any time
        getExperts(5);

        //set see all click listener for all cf
        txtSeeAllCF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //jump to all cf activity
                Intent intent=new Intent(HomeActivity.this, AllCFActivity.class);
                startActivity(intent);

            }
        });
        //set see all click listener for all cources
        txtSeeAllCources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //jump to all cf activity
                Intent intent=new Intent(HomeActivity.this, AllCoursesActivity.class);
                startActivity(intent);

            }
        });

        //set see all click listener for all certifcates
        txtSeeAllCertificates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Intent
                //jump to all cf activity
                Intent intent=new Intent(HomeActivity.this, AllCertificatesActivity.class);
                startActivity(intent);

            }
        });

        //set see all click listener for all books
        txtSeeAllBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Intent
                //jump to all cf activity
                Intent intent=new Intent(HomeActivity.this, AllBooksActivity.class);
                startActivity(intent);

            }
        });

        //set see all click listener for all experts
        txtSeeAllExpets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Intent
                //jump to all cf activity
                Intent intent=new Intent(HomeActivity.this, AllExpertsActivity.class);
                startActivity(intent);

            }
        });




        //Logout button listener
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseHelper.logout();
                //go back to welcome screen
                Intent intent=new Intent(HomeActivity.this, WelcomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });








    }



    private void setWelcomeText(){
        FireUser currentUser= firebaseHelper.getLoggedUser();
          //if no user was logged in
        if(currentUser==null) {
            txtWelcome.setText("Welcome Guest");
            btnLogOut.setVisibility(View.INVISIBLE);
        }
        else {
            txtWelcome.setText("Welcome " + currentUser.getName());
            btnLogOut.setVisibility(View.VISIBLE);
        }
    }

    private  void getComputingFields(int limit)  {
        progressBarCf.setVisibility(View.VISIBLE);
        List<ComputingField> fields = new ArrayList<ComputingField>();
        Query query=mFireStore.collection("computing_fields").limit(limit);
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
                            ComputingFieldsScrollAdapter adapter=new ComputingFieldsScrollAdapter(HomeActivity.this, fields);
                            computingFieldsRecycleView.setAdapter(adapter);

                        }
                        else{
                            Toast.makeText(HomeActivity.this,"Failed to retrieve CF list :"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }

                    }
                });



    }

    private   void getCourses(int limit)  {
        progressBarCf.setVisibility(View.VISIBLE);
        List<Course> courses = new ArrayList<Course>();
        Query query=mFireStore.collection("courses").limit(limit);
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

                            // Set adapter on recycler view
                            CoursesScrollAdapter adapter=new CoursesScrollAdapter(HomeActivity.this, courses);
                            courcesRecycleView.setAdapter(adapter);

                        }
                        else{
                            Toast.makeText(HomeActivity.this,"Failed to retrieve Cources list :"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                        progressBarCoureses.setVisibility(View.GONE);

                    }
                });



    }

    private   void getCertificates(int limit)  {
        List<Certificate> certificates = new ArrayList<Certificate>();
        Query query=mFireStore.collection("certificates").limit(limit);
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
                            CertificatesScrollAdapter adapter=new CertificatesScrollAdapter(HomeActivity.this, certificates);
                            certificatesRecylcleView.setAdapter(adapter);

                        }
                        else{
                            Toast.makeText(HomeActivity.this,"Failed to retrieve Certificates list :"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                        progressBarCertificates.setVisibility(View.GONE);

                    }
                });



    }
    private void getBooks(int limit) {
        List<Book> books = new ArrayList<Book>();
        Query query=mFireStore.collection("books").limit(limit);
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                Book book=new Book();

                                book.setId(document.getId());
                                book.setTitle(data.get("title").toString());
                                book.setCover_image(data.get("cover_image").toString());
                                book.setLink(data.get("link").toString());
                                books.add(book);

                            }

                            // Set adapter on recycler view
                            BooksScrollAdapter adapter=new BooksScrollAdapter(HomeActivity.this, books);
                            booksRecycleView.setAdapter(adapter);

                        }
                        else{
                            Toast.makeText(HomeActivity.this,"Failed to retrieve Books list :"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                        progressBarBooks.setVisibility(View.GONE);

                    }
                });
    }

    private void getExperts(int limit) {
        List<Expert> experts = new ArrayList<Expert>();
        Query query=mFireStore.collection("experts").limit(limit);
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

                            // Set adapter on recycler view
                            ExpertsScrollAdapter adapter=new ExpertsScrollAdapter(HomeActivity.this, experts);
                            expertsRecycleView.setAdapter(adapter);

                        }
                        else{
                            Toast.makeText(HomeActivity.this,"Failed to retrieve Experts list :"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                        progressBarExperts.setVisibility(View.GONE);

                    }
                });
    }

}