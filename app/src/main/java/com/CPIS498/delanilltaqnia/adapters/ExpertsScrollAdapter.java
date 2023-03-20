package com.CPIS498.delanilltaqnia.adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.CPIS498.delanilltaqnia.R;
import com.CPIS498.delanilltaqnia.models.Expert;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.Map;

public class ExpertsScrollAdapter extends RecyclerView.Adapter<ExpertListItem> {
    List<Expert> expertList;
    Context mContext;

    public ExpertsScrollAdapter(Context mContext, List<Expert> expertList) {
        this.expertList = expertList;
        this.mContext=mContext;

    }

    @NonNull
    @Override
    public ExpertListItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate item.xml using LayoutInflator
        View itemView
                = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_list_expert,
                        parent,
                        false);
        return new ExpertListItem(itemView) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ExpertListItem holder, int position) {
        // Set the text of each item of
        // Recycler view with the list items
        String experttitle=expertList.get(position).getJob_title()+"-"+expertList.get(position).getName();
        holder.txtTitle.setText(experttitle);
        holder.txtBrief.setText(expertList.get(position).getBrief());
        //get social media accounts
        Map<String,String> socialAccounts=expertList.get(position).getSocial_links();
        String instaAccount=socialAccounts.get("instagram");
        String twitterAccount=socialAccounts.get("twitter");
        String snapchatAccount=socialAccounts.get("snapchat");
        //set onclick insta button
        holder.btnInsta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dont procced if the  expert hasn't got an account
                if(instaAccount.isEmpty()||instaAccount==null)
                    return;
                //combine with instagram url
                //e.g http://instagram.com/myaccount
                String url="http://instagram.com/"+instaAccount;
                Uri uri = Uri.parse(url);
                Intent intent= new Intent(Intent.ACTION_VIEW,uri);
                //tell intent to open the app if it's already installed
                intent.setPackage("com.instagram.android");
                try {
                  mContext.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    //if app is not installed open in web browser
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(url)));
                }
            }
        });
        //set onclick twitter button
        holder.btnTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dont procced if the  expert hasn't got an account
                if(twitterAccount.isEmpty()||twitterAccount==null)
                    return;
                //combine with twitter url
                //e.g http://twitter.com/myaccount
                String url="http://twitter.com/"+twitterAccount;
                Uri uri = Uri.parse(url);
                Intent intent= new Intent(Intent.ACTION_VIEW,uri);
                //tell intent to open the app if it's already installed
                intent.setPackage("com.twitter.android");
                try {
                    mContext.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    //if app is not installed open in web browser
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(url)));
                }
            }
        });
        //set onclick snap button
        holder.btnSnapchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dont procced if the  expert hasn't got an account
                if(snapchatAccount.isEmpty()||snapchatAccount==null)
                    return;
                //combine with snapchat url
                //e.g http://snapchat.com/myaccount
                String url="http://snapchat.com/"+snapchatAccount;
                Uri uri = Uri.parse(url);
                Intent intent= new Intent(Intent.ACTION_VIEW,uri);
                //tell intent to open the app if it's already installed
                intent.setPackage("com.snapchat.android");
                try {
                    mContext.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    //if app is not installed open in web browser
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(url)));
                }
            }
        });



    }



    @Override
    public int getItemCount() {
        return expertList.size();
    }
}

class  ExpertListItem extends  RecyclerView.ViewHolder{

    TextView txtTitle;
    TextView txtBrief;
    ImageButton btnInsta,btnTwitter,btnSnapchat,btnEmail;




    public ExpertListItem(@NonNull View itemView) {
        super(itemView);
        txtTitle=itemView.findViewById(R.id.txt_title);
        txtBrief=itemView.findViewById(R.id.txt_brief);
        btnInsta=itemView.findViewById(R.id.btn_insta);
        btnTwitter=itemView.findViewById(R.id.btn_twitter);
        btnSnapchat=itemView.findViewById(R.id.btn_snapchat);
    }
}
