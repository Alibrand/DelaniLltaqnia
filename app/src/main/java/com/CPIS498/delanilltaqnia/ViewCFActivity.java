package com.CPIS498.delanilltaqnia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.CPIS498.delanilltaqnia.adapters.BooksScrollAdapter;
import com.CPIS498.delanilltaqnia.adapters.CertificatesScrollAdapter;
import com.CPIS498.delanilltaqnia.adapters.CoursesScrollAdapter;
import com.CPIS498.delanilltaqnia.adapters.ExpertsScrollAdapter;
import com.CPIS498.delanilltaqnia.models.Book;
import com.CPIS498.delanilltaqnia.models.Certificate;
import com.CPIS498.delanilltaqnia.models.ComputingField;
import com.CPIS498.delanilltaqnia.models.Course;
import com.CPIS498.delanilltaqnia.models.Expert;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewCFActivity extends AppCompatActivity {
    TextView txtCFWelcome, txtCFtitle, txtCFbrief, txtCourcesHeader;
    TextView txtBooksHeader, txtCertificatesHeader, txtExpertsHeader;
    ProgressBar progressBarCf, progressBarCourses;
    ProgressBar progressBarCertificates, progressBarBooks, progressBarExperts;
    FirebaseFirestore mFireStore;
    LinearLayoutCompat lyCFInformation;
    RecyclerView courcesRecycleView, booksRecycleView;
    RecyclerView certificatesRecycleView, expertsRecycleView;
    ImageButton btnBack;

    //lists for recycleviews
    List<Course> fieldCources = new ArrayList<Course>();
    List<Certificate> fieldCertificates = new ArrayList<Certificate>();
    List<Book> fieldBooks = new ArrayList<Book>();
    List<Expert> fieldExperts = new ArrayList<Expert>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cfactivity);
        //initialize Variables
        txtCFWelcome = findViewById(R.id.txt_cf_welcome);
        txtCFtitle = findViewById(R.id.txt_cf_title);
        txtCFbrief = findViewById(R.id.txt_cf_brief);
        txtCourcesHeader = findViewById(R.id.txt_courses_header);
        txtBooksHeader = findViewById(R.id.txt_books_header);
        txtExpertsHeader = findViewById(R.id.txt_experts_header);
        txtCertificatesHeader = findViewById(R.id.txt_certificates_header);

        progressBarCf = findViewById(R.id.progress_cf);
        progressBarCourses = findViewById(R.id.progress_courses);
        progressBarBooks = findViewById(R.id.progress_books);
        progressBarCertificates = findViewById(R.id.progress_certificates);
        progressBarExperts = findViewById(R.id.progress_experts);

        lyCFInformation = findViewById(R.id.ly_cf_info);

        courcesRecycleView = findViewById(R.id.rv_courses);
        certificatesRecycleView = findViewById(R.id.rv_certificates);
        booksRecycleView = findViewById(R.id.rv_books);
        expertsRecycleView = findViewById(R.id.rv_experts);
        btnBack=findViewById(R.id.btn_back);

        mFireStore = FirebaseFirestore.getInstance();

        //Get cf id sent with intent
        Intent intent = getIntent();
        String cf_id = intent.getStringExtra("cf_id");

        //get CF info from firebase
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
        progressBarCf.setVisibility(View.VISIBLE);
        DocumentReference document = mFireStore.collection("computing_fields").document(cf_id);
        document.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            Map<String, Object> data = document.getData();
                            //add uid to map
                            data.put("id", document.getId());
                            ComputingField field = new ComputingField(data);
                            //fill texts with info
                            txtCFtitle.setText(field.getTitle());
                            txtCFbrief.setText(field.getBrief());
                            txtCFWelcome.setText("Hello , \nYou are in " + field.getTitle());
                            //hide progress
                            progressBarCf.setVisibility(View.GONE);
                            //show hidden info layout
                            lyCFInformation.setVisibility(View.VISIBLE);

                            //Getting list of cources of this computing field
                            getCFCoursesList(field.getTitle());
                            //Getting list of certificates of this computing field
                            getCFCertificatesList(field.getTitle());
                            //Getting list of Books of this computing field
                            getCFBooksList(field.getTitle());
                            //Getting list of Experts of this computing field
                            getCFExpertsList(field.getTitle());


                        } else {
                            Toast.makeText(ViewCFActivity.this, "Failed to retrieve CF info :" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }

    private void getCFCertificatesList(String cf_title) {
        Query courseQuery = mFireStore.collection("certificates")
                .whereEqualTo("computing_field", cf_title);
        courseQuery.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()
                            ) {
                                //Get Data from document
                                Map<String, Object> data = document.getData();
                                data.put("id",document.getId());
                                Certificate certificate = new Certificate(data);

                                fieldCertificates.add(certificate);


                            }
                            //set scroll adapter to recycle view
                            CertificatesScrollAdapter adapter = new CertificatesScrollAdapter(ViewCFActivity.this, fieldCertificates);
                            certificatesRecycleView.setAdapter(adapter);
                            //if no certificate was found
                            if (fieldCertificates.size() == 0)
                                txtCertificatesHeader.append(": No Course was found");


                        } else {
                            Toast.makeText(ViewCFActivity.this, "Failed to retrieve certificates :" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            txtCertificatesHeader.append(":Error");
                        }
                        //hide progress
                        progressBarCertificates.setVisibility(View.GONE);

                    }
                });
    }

    private void getCFCoursesList(String cf_title) {
        Query courseQuery = mFireStore.collection("courses")
                .whereEqualTo("computing_field", cf_title);
        courseQuery.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()
                            ) {
                                //Get Data from document
                                Map<String, Object> data = document.getData();
                                Course course = new Course();
                                course.setId(document.getId());
                                course.setTitle(data.get("title").toString());
                                course.setLink(data.get("link").toString());
                                course.setLogo(data.get("logo").toString());
                                fieldCources.add(course);


                            }
                            //set scroll adapter to recycle view
                            CoursesScrollAdapter adapter = new CoursesScrollAdapter(ViewCFActivity.this, fieldCources);
                            courcesRecycleView.setAdapter(adapter);
                            //if no course was found
                            if (fieldCources.size() == 0)
                                txtCourcesHeader.append(": No Course was found");


                        } else {
                            Toast.makeText(ViewCFActivity.this, "Failed to retrieve courses :" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            txtCourcesHeader.append(":Error");
                        }
                        //hide progress
                        progressBarCourses.setVisibility(View.GONE);

                    }
                });
    }

    private void getCFBooksList(String cf_title) {
        Query courseQuery = mFireStore.collection("books")
                .whereEqualTo("computing_field", cf_title);
        courseQuery.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()
                            ) {
                                //Get Data from document
                                Map<String, Object> data = document.getData();
                                Book book = new Book();
                                book.setId(document.getId());
                                book.setTitle(data.get("title").toString());
                                book.setLink(data.get("link").toString());
                                book.setCover_image(data.get("cover_image").toString());
                                fieldBooks.add(book);


                            }
                            //set scroll adapter to recycle view
                            BooksScrollAdapter adapter = new BooksScrollAdapter(ViewCFActivity.this, fieldBooks);
                            booksRecycleView.setAdapter(adapter);
                            //if no book was found
                            if (fieldBooks.size() == 0)
                                txtBooksHeader.append(": No Book was found");


                        } else {
                            Toast.makeText(ViewCFActivity.this, "Failed to retrieve books :" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            txtBooksHeader.append(":Error");
                        }
                        //hide progress
                        progressBarBooks.setVisibility(View.GONE);

                    }
                });
    }

    private void getCFExpertsList(String cf_title) {
        Query courseQuery = mFireStore.collection("experts")
                .whereEqualTo("computing_field", cf_title);
        courseQuery.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()
                            ) {
                                //Get Data from document
                                Map<String, Object> data = document.getData();
                                data.put("id",document.getId());
                                Expert expert=new Expert(data);
                                fieldExperts.add(expert);


                            }
                            //set scroll adapter to recycle view
                            ExpertsScrollAdapter adapter = new ExpertsScrollAdapter(ViewCFActivity.this, fieldExperts);
                            expertsRecycleView.setAdapter(adapter);
                            //if no book was found
                            if (fieldExperts.size() == 0)
                                txtExpertsHeader.append(": No Expert was found");


                        } else {
                            Toast.makeText(ViewCFActivity.this, "Failed to retrieve experts :" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            txtExpertsHeader.append(":Error");
                        }
                        //hide progress
                        progressBarExperts.setVisibility(View.GONE);

                    }
                });
    }


}