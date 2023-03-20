package com.CPIS498.delanilltaqnia.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.CPIS498.delanilltaqnia.R;
import com.CPIS498.delanilltaqnia.ViewCFActivity;
import com.CPIS498.delanilltaqnia.models.ComputingField;

import java.util.List;

public class ComputingFieldsScrollAdapter extends RecyclerView.Adapter<ComputingFieldListItem> {
    List<ComputingField> computingFieldList;
    Context mContext;

    public ComputingFieldsScrollAdapter(Context mContext, List<ComputingField> computingFieldList) {
        this.computingFieldList = computingFieldList;
        this.mContext=mContext;

    }

    @NonNull
    @Override
    public ComputingFieldListItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate item.xml using LayoutInflator
        View itemView
                = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_list_computing_field,
                        parent,
                        false);
        return new ComputingFieldListItem(itemView) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ComputingFieldListItem holder, int position) {
        // Set the text of each item of
        // Recycler view with the list items
        holder.txtTitle.setText(computingFieldList.get(position).getTitle());
        String cf_id=computingFieldList.get(position).getId();
        String url=computingFieldList.get(position).getUrl();
        holder.txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, ViewCFActivity.class);
                intent.putExtra("cf_id",cf_id);
                mContext.startActivity(intent);

            }
        });
//        holder.txtDetails.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(url==null)
//                    return;
//                Uri uri = Uri.parse(url ); // missing 'http://' will cause crashed
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                mContext.startActivity(intent);
//            }
//        });
    }



    @Override
    public int getItemCount() {
        return computingFieldList.size();
    }
}

class  ComputingFieldListItem extends  RecyclerView.ViewHolder{

    TextView txtTitle;
    TextView txtDetails;
    CardView itemCardView;


    public ComputingFieldListItem(@NonNull View itemView) {
        super(itemView);
        txtTitle=itemView.findViewById(R.id.txt_title);
        txtDetails=itemView.findViewById(R.id.txt_details);
        itemCardView=itemView.findViewById(R.id.card_cf_item);
    }
}
