package com.CPIS498.delanilltaqnia;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.CPIS498.delanilltaqnia.models.FireUser;
import com.CPIS498.delanilltaqnia.models.Request;
import com.CPIS498.delanilltaqnia.services.FirebaseHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RequestBookActivity extends AppCompatActivity {
    final int PICK_IMAGE_REQUEST = 22;
    AppCompatButton btnSend, btnSelectImage;
    ImageView ivBookLogo;
    EditText txtBookTitle,txtBookUrl;
    Uri imagePath;
    String bookTitle, imageName,bookUrl;
    ProgressDialog progressDialog;
    FirebaseHelper mFirehelper;
    FirebaseFirestore mFirestore;
    FirebaseStorage mFireStorage;
    FireUser loggedUser;
    int sizeLimit=300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_book);
        //define variables;
        txtBookTitle = findViewById(R.id.txt_book_title);
        txtBookUrl = findViewById(R.id.txt_book_url);
        ivBookLogo = findViewById(R.id.iv_book_cover);
        btnSend = findViewById(R.id.btn_send);
        btnSelectImage = findViewById(R.id.btn_select_image);
        progressDialog = new ProgressDialog(this);

        mFirehelper=new FirebaseHelper(this);
        mFirestore=FirebaseFirestore.getInstance();
        mFireStorage=FirebaseStorage.getInstance();
        loggedUser=mFirehelper.getLoggedUser();


        //on tapping select image listener
        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

        //on tapping send button
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if no user has logged
                if(loggedUser==null)
                {
                    Toast.makeText(RequestBookActivity.this,"This operation requires user log in",Toast.LENGTH_LONG).show();
                }
                else {

                    uploadRequest();
                }
            }
        });
    }
    private void SelectImage() {
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Logo Image ..."),
                PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            imagePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                imagePath);
                //check uploaded image size
                int imgize=0;
                try {
                    InputStream fileInputStream=getApplicationContext().getContentResolver().openInputStream(imagePath);
                    imgize = fileInputStream.available();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(imgize>sizeLimit*1024)
                {
                    imagePath=null;
                    ivBookLogo.setImageBitmap(null);
                    Toast.makeText(this,"Maximum file size allowed is "+sizeLimit+"KB",Toast.LENGTH_LONG).show();

                }
                else
                ivBookLogo.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    // UploadRequest method
    private void uploadRequest() {

        //fill variables
        bookTitle=txtBookTitle.getText().toString();
        bookUrl=txtBookUrl.getText().toString();

        //check empty fields
        if(bookTitle.trim().isEmpty()||imagePath==null)
        {
            Toast.makeText(RequestBookActivity.this,"You left empty fields or no cover was chosen",Toast.LENGTH_LONG).show();
            return;//stop operation
        }

        //Upload the cover image first
        // Code for showing progressDialog while uploading
        progressDialog.setTitle("Uploading Image...");
        progressDialog.show();

        //define unique image name usinf UUID generator
        imageName = UUID.randomUUID().toString();

        // Defining the child of storageReference
        StorageReference storageReference =mFireStorage.getReference();
        StorageReference ref = storageReference.child("images/covers/"+imageName);

        // adding listeners on upload
        // or failure of image
        ref.putFile(imagePath)
                .addOnSuccessListener(
                        new OnSuccessListener<UploadTask.TaskSnapshot>() {

                            @Override
                            public void onSuccess(
                                    UploadTask.TaskSnapshot taskSnapshot) {


                                // Image uploaded successfully
                                progressDialog.setTitle("Uploading Data...");
                                //get image download url
                               taskSnapshot.getMetadata().getReference().getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        imageName=uri.toString();
                                        //then upload book request
                                        uploadRequestData();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                   @Override
                                   public void onFailure(@NonNull Exception e) {
                                       progressDialog.dismiss();
                                       Toast.makeText(RequestBookActivity.this, "Failed to upload due to :" + e.getMessage(), Toast.LENGTH_SHORT).show();

                                   }
                               });



                            }
                        })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        // Error, Image not uploaded
                        progressDialog.dismiss();
                        Toast.makeText(RequestBookActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void uploadRequestData(){
        //get now date
        Date now= Calendar.getInstance().getTime() ;
        //prepare request
        Request request=new Request();
        request.setRequest_user(loggedUser.getName());
        request.setRequest_date(now);
        request.setRequest_type("books");
        //request data
        Map<String,String> bookData=new HashMap<String, String>() ;
        bookData.put("title",bookTitle);
        bookData.put("link",bookUrl);
        bookData.put("cover_image",imageName);
        request.setRequest_data(bookData);
        //save request data to firestore
        mFirestore.collection("requests").add(request)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        progressDialog.dismiss();
                        //go to thank page
                        Intent intent=new Intent(RequestBookActivity.this,ThanksActivity.class);
                        startActivity(intent);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(RequestBookActivity.this,"Failure :"+e.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });







    }
}