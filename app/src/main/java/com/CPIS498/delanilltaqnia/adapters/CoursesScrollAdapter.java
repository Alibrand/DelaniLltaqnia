package com.CPIS498.delanilltaqnia.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.CPIS498.delanilltaqnia.R;
import com.CPIS498.delanilltaqnia.models.Course;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class CoursesScrollAdapter extends RecyclerView.Adapter<CourseListItem> {
    List<Course> courseList;
    Context mContext;

    public CoursesScrollAdapter(Context mContext, List<Course> courseList) {
        this.courseList = courseList;
        this.mContext=mContext;

    }

    @NonNull
    @Override
    public CourseListItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate item.xml using LayoutInflator
        View itemView
                = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_list_course,
                        parent,
                        false);
        return new CourseListItem(itemView) ;
    }

    @Override
    public void onBindViewHolder(@NonNull CourseListItem holder, int position) {
        // Set the text of each item of
        // Recycler view with the list items
        holder.txtTitle.setText(courseList.get(position).getTitle());
        String course_id= courseList.get(position).getId();
        String url=courseList.get(position).getLink();
        String logoUrl=courseList.get(position).getLogo();
         holder.txtDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(url==null)
                    return;
                Uri uri = Uri.parse(url ); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                mContext.startActivity(intent);
            }
        });

        //load course logo image if existed
        if(logoUrl!=null)
        {
                        //get logo
                        Glide.with(mContext)
                                .load(logoUrl)
                                .placeholder(R.drawable.ic_md_ondemand_video)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(holder.ivCourseLogo);

   }
    }



    @Override
    public int getItemCount() {
        return courseList.size();
    }
}

class  CourseListItem extends  RecyclerView.ViewHolder{

    TextView txtTitle;
    TextView txtDetails;
    ImageView ivCourseLogo;
    CardView itemCardView;


    public CourseListItem(@NonNull View itemView) {
        super(itemView);
        txtTitle=itemView.findViewById(R.id.txt_title);
        txtDetails=itemView.findViewById(R.id.txt_details);
        ivCourseLogo=itemView.findViewById(R.id.iv_course_logo);
        itemCardView=itemView.findViewById(R.id.card_course_item);
    }
}
