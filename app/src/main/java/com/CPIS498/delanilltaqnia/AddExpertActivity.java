package com.CPIS498.delanilltaqnia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.CPIS498.delanilltaqnia.models.Expert;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddExpertActivity extends AppCompatActivity {
    EditText etxtName,etxtBrief,etxtJobtitle,etxtInstaAccount,
    etxtTwitterAccount,etxtSnapChatAccount;
    Spinner spinnerAllCf;
    AppCompatButton btnUpload;
    ProgressDialog dialog;
    String[] allCFTypes={"General System Analyst","Junior Technical Support",
            "Security Specialist","Project Manager",
            "Junior Software Developer","Network Engineer"};
    FirebaseFirestore mFireStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expert);
        //initialize
        etxtName=findViewById(R.id.txt_expert_name);
        etxtBrief=findViewById(R.id.txt_expert_brief);
        etxtJobtitle=findViewById(R.id.txt_expert_jobtitle);
        etxtInstaAccount=findViewById(R.id.txt_instagram_account);
        etxtTwitterAccount=findViewById(R.id.txt_twitter_account);
        etxtSnapChatAccount=findViewById(R.id.txt_snapchat_account);
        spinnerAllCf=findViewById(R.id.spinner_all_cf);
        btnUpload=findViewById(R.id.btn_upload);
        mFireStore=FirebaseFirestore.getInstance();

        //load cf list in dropdownlist
        ArrayAdapter allcfAdapter=new ArrayAdapter(this,R.layout.item_spinner,allCFTypes);
        spinnerAllCf.setAdapter( allcfAdapter);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadExpert();
            }
        });

       etxtBrief.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {

           }

           @Override
           public void afterTextChanged(Editable s) {
               //if brief exceeds the max length
                if(etxtBrief.getText().toString().length()>=90)
                {
                    Toast.makeText(AddExpertActivity.this,"Max allowed length is 90 character",Toast.LENGTH_LONG).show();

                }
           }
       });


    }

    private void uploadExpert() {
        //get input info into variables
        String expertName=etxtName.getText().toString();
        String expertJobtitle=etxtJobtitle.getText().toString();
        String expertBrief=etxtBrief.getText().toString();
        String instaAccount=etxtInstaAccount.getText().toString();
        String twitterAccount=etxtTwitterAccount.getText().toString();
        String snapchatAccount=etxtSnapChatAccount.getText().toString();
        String selectedCF=spinnerAllCf.getSelectedItem().toString();
        //check if empty
        if(expertName.trim().isEmpty()||expertJobtitle.trim().isEmpty()||expertBrief.trim().isEmpty())
        {
            Toast.makeText(AddExpertActivity.this,"A required field is empty",Toast.LENGTH_LONG).show();
            return;
        }
        //show loading
        dialog=new ProgressDialog(this);
        dialog.setMessage("Uploading...");
        dialog.setCancelable(false);
        dialog.show();


        //create expert object
        Expert expert=new Expert();
        expert.setName(expertName);
        expert.setBrief(expertBrief);
        expert.setJob_title(expertJobtitle);
        expert.setComputing_field(selectedCF);
        //create social links map
        Map<String,String> socialAccounts=new HashMap<String,String>();
        socialAccounts.put("instagram",instaAccount.trim());
        socialAccounts.put("twitter",twitterAccount.trim());
        socialAccounts.put("snapchat",snapchatAccount.trim());
        expert.setSocial_links(socialAccounts);

        //put expert object in firestore collection
        mFireStore.collection("experts")
                .add(expert)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(AddExpertActivity.this,"Expert has been uploaded successfully",Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        //close activity
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddExpertActivity.this,"Failed to upload Expert:"+e.getMessage().toString(),Toast.LENGTH_LONG).show();
                dialog.dismiss();

            }
        });


    }
}