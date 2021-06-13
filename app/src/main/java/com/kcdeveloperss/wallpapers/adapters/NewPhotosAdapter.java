package com.kcdeveloperss.wallpapers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kcdeveloperss.wallpapers.R;
import com.kcdeveloperss.wallpapers.beans.PhotosBean;

import java.util.ArrayList;

public class NewPhotosAdapter extends RecyclerView.Adapter<NewPhotosAdapter.RecyclerVH> {
    Context context;
    ArrayList<PhotosBean> arrCateList;
    OnPhotoSelectedListner onPhotoSelectedListner;
    PhotosBean catbean;

    public NewPhotosAdapter(Context context, ArrayList<PhotosBean> photosBeans) {
        this.context = context;
        this.arrCateList = photosBeans;
    }

    public void setOnCategorySelectedListner(OnPhotoSelectedListner onPhotoSelectedListner) {
        this.onPhotoSelectedListner = onPhotoSelectedListner;
    }

    public interface OnPhotoSelectedListner {
         void setOnPhotoSelatedListner(int position, PhotosBean dataBean);
    }

    @NonNull
    @Override
    public RecyclerVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_newphotos, parent, false);
        return new RecyclerVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerVH holder, int position) {
        Glide.with(context).load(arrCateList.get(position).getMediumUrl())
                .thumbnail(0.5f)
                .placeholder(R.drawable.ic_placeholder_sq)
                .error(R.drawable.ic_placeholder_sq)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imgview);
        holder.itemView.setOnClickListener(v -> onPhotoSelectedListner.setOnPhotoSelatedListner(position, arrCateList.get(position)));
    }

    @Override
    public int getItemCount() {
        return arrCateList.size();
    }

    public class RecyclerVH extends RecyclerView.ViewHolder {
        ImageView imgview;
//        CircleImageView cv_user_image;
        // TextView tvTitle;

        public RecyclerVH(@NonNull View itemView) {
            super(itemView);
            imgview = (ImageView) itemView.findViewById(R.id.imgview);
//            cv_user_image = (CircleImageView) itemView.findViewById(R.id.cv_user_image);
            //  tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        }
    }
}
