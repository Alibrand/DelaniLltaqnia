package com.CPIS498.delanilltaqnia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.CPIS498.delanilltaqnia.adapters.BooksScrollAdapter;
import com.CPIS498.delanilltaqnia.adapters.CertificatesScrollAdapter;
import com.CPIS498.delanilltaqnia.models.Book;
import com.CPIS498.delanilltaqnia.models.Certificate;
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

public class AllBooksActivity extends AppCompatActivity {
    ProgressBar progressBarBooks;
    FirebaseFirestore mFireStore;
    FirebaseHelper mFireHelper;
    RecyclerView booksRecycleView;
    AppCompatButton btnRequestBook;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_books);

        //define components
        mFireStore=FirebaseFirestore.getInstance();
        mFireHelper=new FirebaseHelper(AllBooksActivity.this);
        booksRecycleView =findViewById(R.id.rv_books);
        progressBarBooks =findViewById(R.id.progress_books);
        btnBack=findViewById(R.id.btn_back);
        btnRequestBook =findViewById(R.id.btn_request_book);

        //set onclick back arrow
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //set on click request to certificate button
        btnRequestBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               FireUser loggedUser= mFireHelper.getLoggedUser();
               // if no user has logged to system
                //inform user that this operation require login
               if(loggedUser==null){
                   Snackbar.make(btnRequestBook,"This Operation requires user login",Snackbar.LENGTH_LONG).show();
               }
               else{
                   //go to request page
                   Intent intent=new Intent(AllBooksActivity.this,RequestBookActivity.class);
                   startActivity(intent);

               }
            }
        });


        //Get all cf from firebase
        getAllCertificates();
    }

    public  void getAllCertificates()  {
        List<Book> books = new ArrayList<Book>();
        Query query=mFireStore.collection("books");
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
                            BooksScrollAdapter adapter=new BooksScrollAdapter(AllBooksActivity.this, books);
                            booksRecycleView.setAdapter(adapter);

                        }
                        else{
                            Toast.makeText(AllBooksActivity.this,"Failed to retrieve Books list :"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                        progressBarBooks.setVisibility(View.GONE);

                    }
                });



    }
}