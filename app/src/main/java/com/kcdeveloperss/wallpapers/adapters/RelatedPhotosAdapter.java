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
import com.kcdeveloperss.wallpapers.beans.RelatedBean;

import java.util.ArrayList;

public class RelatedPhotosAdapter extends RecyclerView.Adapter<RelatedPhotosAdapter.RecyclerVH> {

    Context context;
    ArrayList<RelatedBean> relatedList = new ArrayList<>();
    OnPhotoSelectedListner onPhotoSelectedListner;
    PhotosBean catbean;

    public RelatedPhotosAdapter(Context context, ArrayList<RelatedBean> relatedBeans) {
        this.context = context;
        this.relatedList = relatedBeans;
    }


    public void setOnCategorySelectedListner(OnPhotoSelectedListner onPhotoSelectedListner) {

        this.onPhotoSelectedListner = onPhotoSelectedListner;
    }

    public interface OnPhotoSelectedListner {
        void setOnPhotoSelatedListner(int position, RelatedBean relatedBean);

    }

    @NonNull
    @Override
    public RecyclerVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_related, parent, false);
        return new RecyclerVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerVH holder, int position) {
        Glide.with(context).load(relatedList.get(position).getRegular())
                .placeholder(R.drawable.ic_placeholder_photos)
                .error(R.drawable.ic_placeholder_photos)
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imgview);
        holder.itemView.setOnClickListener(view -> onPhotoSelectedListner.setOnPhotoSelatedListner(position, relatedList.get(position)));
    }

    @Override
    public int getItemCount() {
        return relatedList.size();
    }


    public static class RecyclerVH extends RecyclerView.ViewHolder {
        ImageView imgview;

        public RecyclerVH(View itemView) {
            super(itemView);
            imgview = itemView.findViewById(R.id.imgview);

        }
    }
}
