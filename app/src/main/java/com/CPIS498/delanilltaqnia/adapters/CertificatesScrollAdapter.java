package com.CPIS498.delanilltaqnia.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.CPIS498.delanilltaqnia.R;
import com.CPIS498.delanilltaqnia.ViewCertificateActivity;
import com.CPIS498.delanilltaqnia.models.Certificate;
import com.CPIS498.delanilltaqnia.models.Course;

import java.util.List;

public class CertificatesScrollAdapter extends RecyclerView.Adapter<CertificateListItem> {
    List<Certificate> certificateList;
    Context mContext;

    public CertificatesScrollAdapter(Context mContext, List<Certificate> certificateList) {
        this.certificateList = certificateList;
        this.mContext=mContext;

    }

    @NonNull
    @Override
    public CertificateListItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate item.xml using LayoutInflator
        View itemView
                = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_list_certificate,
                        parent,
                        false);
        return new CertificateListItem(itemView) ;
    }

    @Override
    public void onBindViewHolder(@NonNull CertificateListItem holder, int position) {
        // Set the text of each item of
        // Recycler view with the list items
        holder.txtTitle.setText(certificateList.get(position).getTitle());
        String certificate_id= certificateList.get(position).getId();
        String url=certificateList.get(position).getUrl();
        holder.txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, ViewCertificateActivity.class);
                intent.putExtra("certificate_id",certificate_id);
                mContext.startActivity(intent);


            }
        });

                holder.txtDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, ViewCertificateActivity.class);
                intent.putExtra("certificate_id",certificate_id);
                mContext.startActivity(intent);
            }
        });

    }



    @Override
    public int getItemCount() {
        return certificateList.size();
    }
}

class  CertificateListItem extends  RecyclerView.ViewHolder{

    TextView txtTitle;
    TextView txtDetails;
    CardView itemCardView;


    public CertificateListItem(@NonNull View itemView) {
        super(itemView);
        txtTitle=itemView.findViewById(R.id.txt_title);
        txtDetails=itemView.findViewById(R.id.txt_details);
        itemCardView=itemView.findViewById(R.id.card_course_item);
    }
}
