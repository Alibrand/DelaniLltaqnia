package com.CPIS498.delanilltaqnia.adapters;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.CPIS498.delanilltaqnia.R;
import com.CPIS498.delanilltaqnia.RequestBookActivity;
import com.CPIS498.delanilltaqnia.ThanksActivity;
import com.CPIS498.delanilltaqnia.models.Request;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class RequestsScrollAdapter extends RecyclerView.Adapter<RequestListItem> {
    List<Request> requestList;
    Context mContext;
    String[] allCFTypes={"General System Analyst","Junior Technical Support",
            "Security Specialist","Project Manager",
            "Junior Software Developer","Network Engineer"};
    FirebaseFirestore mFireStore;
    ProgressDialog progressDialog;

    public RequestsScrollAdapter(Context mContext, List<Request> requestList) {
        this.requestList = requestList;
        this.mContext=mContext;
        this.mFireStore=FirebaseFirestore.getInstance();
        this.progressDialog=new ProgressDialog(mContext);


    }

    @NonNull
    @Override
    public RequestListItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate item.xml using LayoutInflator
        View itemView
                = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_list_request,
                        parent,
                        false);
        return new RequestListItem(itemView) ;
    }

    @Override
    public void onBindViewHolder(@NonNull RequestListItem holder, int position) {
        //load cf list in dropdownlist
        ArrayAdapter allcfAdapter=new ArrayAdapter(mContext,R.layout.item_spinner,allCFTypes);
        holder.spinnerAllCf.setAdapter( allcfAdapter);

        final int itemPostition=position;
        // get request from list
        Request request=requestList.get(itemPostition);
        //get request data
        String requestCollection=request.getRequest_type();
        Map<String,String> requestData=request.getRequest_data();
        String requestId=request.getId();
        String requestUser=request.getRequest_user();
        String dataTitle=requestData.get("title").toString();
        //set request number
         int requestnum=itemPostition+1;
         holder.txtReqNum.setText(String.valueOf(requestnum));
         holder.txtReqUser.setText(requestUser);
         holder.txtReqDatatitle.setText(dataTitle);
         //set request type
         holder.txtReqName.setText("Upload "+request.getRequest_type());
         //format date to dispaly
        SimpleDateFormat formatDate =new SimpleDateFormat("YYYY-MM-dd  HH:mm:ss");
        String formattedDate=formatDate.format(request.getRequest_date());
        holder.txtReqDate.setText(formattedDate);



        //set on click upload
        holder.btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectdCF=holder.spinnerAllCf.getSelectedItem().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setCancelable(true);
                builder.setTitle("Upload Confirmation");
                builder.setMessage("Confirm uploading request to "+selectdCF+"?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //add computing field to request data
                                requestData.put("computing_field",selectdCF);
                                //show loading
                                progressDialog.show();
                                progressDialog.setMessage("Uploading Data...");
                                //add request data to the collection
                                mFireStore.collection(requestCollection).add(requestData)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                //delete request from store
                                                progressDialog.setMessage("Deleting Request...");
                                                Toast.makeText(mContext,"Request data has been uploaded Successfully to "+requestCollection,Toast.LENGTH_LONG).show();
                                                //delete request from store
                                                mFireStore.collection("requests")
                                                        .document(requestId).delete()
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                //dismiss dialog
                                                                progressDialog.dismiss();
                                                                //delete request from list
                                                                requestList.remove(request);
                                                                notifyItemRemoved(itemPostition);
                                                                notifyItemRangeChanged(itemPostition, requestList.size());

                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(mContext,"Failted to delete request "+e.getMessage().toString(),Toast.LENGTH_LONG).show();

                                                    }
                                                });



                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(mContext,"Failure :"+e.getMessage(),Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();





            }
        });

        //set delete listener
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show confirm dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setCancelable(true);
                builder.setTitle("Delete Confirmation");
                builder.setMessage("Are you sure to delete this request?");
                builder.setPositiveButton("Sure",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //show loading
                                progressDialog.show();
                                progressDialog.setMessage("Deleting Request..");
                                //delete request from store
                                mFireStore.collection("requests")
                                        .document(requestId).delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                //dismiss dialog
                                                progressDialog.dismiss();
                                                //delete request from list
                                                requestList.remove(request);
                                                notifyItemRemoved(itemPostition);
                                                notifyItemRangeChanged(itemPostition, requestList.size());

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(mContext,"Failed to delete request "+e.getMessage().toString(),Toast.LENGTH_LONG).show();
                                        //dismiss dialog
                                        progressDialog.dismiss();
                                    }
                                });
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        //set on click view
        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info="User:"+requestUser+"\n\n";
                info+="Title:" +requestData.get("title").toString()+"\n\n";
                info+="Category:"+request.getRequest_type()+"\n\n";

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Request Data")
                        .setMessage(info)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                builder.create();
                builder.show();
            }
        });



    }



    @Override
    public int getItemCount() {
        return requestList.size();
    }
}

class  RequestListItem extends  RecyclerView.ViewHolder{

    Spinner spinnerAllCf;
    TextView btnView,btnDelete,btnUpload;
    TextView txtReqNum,txtReqName,txtReqDate,txtReqUser,txtReqDatatitle;




    public RequestListItem(@NonNull View itemView) {
        super(itemView);
        spinnerAllCf=itemView.findViewById(R.id.spinner_all_cf);
        btnView=itemView.findViewById(R.id.btn_request_view);
        btnDelete=itemView.findViewById(R.id.btn_request_delete);
        btnUpload=itemView.findViewById(R.id.btn_request_upload);
        txtReqNum=itemView.findViewById(R.id.txt_request_num);
        txtReqDate=itemView.findViewById(R.id.txt_request_date);
        txtReqName=itemView.findViewById(R.id.txt_request_name);
        txtReqUser=itemView.findViewById(R.id.txt_request_user);
        txtReqDatatitle=itemView.findViewById(R.id.txt_request_data_title);
    }
}
