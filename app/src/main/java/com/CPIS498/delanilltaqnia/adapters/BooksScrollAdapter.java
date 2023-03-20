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
import com.CPIS498.delanilltaqnia.ViewCertificateActivity;
import com.CPIS498.delanilltaqnia.models.Book;
import com.CPIS498.delanilltaqnia.models.Course;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class BooksScrollAdapter extends RecyclerView.Adapter<BookListItem> {
    List<Book> bookList;
    Context mContext;

    public BooksScrollAdapter(Context mContext, List<Book> bookList) {
        this.bookList = bookList;
        this.mContext=mContext;

    }

    @NonNull
    @Override
    public BookListItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate item.xml using LayoutInflator
        View itemView
                = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_list_book,
                        parent,
                        false);
        return new BookListItem(itemView) ;
    }

    @Override
    public void onBindViewHolder(@NonNull BookListItem holder, int position) {
        // Set the text of each item of
        // Recycler view with the list items
        holder.txtTitle.setText(bookList.get(position).getTitle());
        String link=bookList.get(position).getLink();
        ImageView cover=holder.ivBookCover;
        //tapping on book title will open the url in browser
        holder.txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(link==null)
                    return;
                Uri uri = Uri.parse(link ); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                mContext.startActivity(intent);
            }
        });

                        //get cover image
                        String imageUrl=bookList.get(position).getCover_image();
                        //LOAD cover image
                        Glide.with(mContext)
                                .load(imageUrl)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(R.drawable.ic_open_book_svgrepo_com)
                                .into(cover);

    }



    @Override
    public int getItemCount() {
        return bookList.size();
    }
}

class  BookListItem extends  RecyclerView.ViewHolder{

    TextView txtTitle;
    ImageView ivBookCover;



    public BookListItem(@NonNull View itemView) {
        super(itemView);
        txtTitle=itemView.findViewById(R.id.txt_title);
        ivBookCover=itemView.findViewById(R.id.iv_book_cover);

    }
}
