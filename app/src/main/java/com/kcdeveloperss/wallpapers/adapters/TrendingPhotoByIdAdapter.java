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
import com.kcdeveloperss.wallpapers.beans.TrendingBean;

import java.util.ArrayList;

public class TrendingPhotoByIdAdapter extends RecyclerView.Adapter<TrendingPhotoByIdAdapter.RecyclerVH> {

    Context context;
    ArrayList<TrendingBean> trendingList = new ArrayList<>();
    OnCategorybyidSelectedListner onCategorybyidSelectedListner;

    public TrendingPhotoByIdAdapter(Context context, ArrayList<TrendingBean> trendingList) {
        this.context = context;
        this.trendingList = trendingList;
    }

    public void setOnCategorybyidSelectedListner(OnCategorybyidSelectedListner onCategorybyidSelectedListner) {

        this.onCategorybyidSelectedListner = onCategorybyidSelectedListner;
    }

    public interface OnCategorybyidSelectedListner {
        void setOnCategorybyidSelatedListner(int position, TrendingBean trendingBean);

    }

    @NonNull
    @Override
    public RecyclerVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_trendingphotosbyid, parent, false);
        return new RecyclerVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerVH holder, int position) {
        Glide.with(context).load(trendingList.get(position).getRegular())
                .thumbnail(0.5f)
                .placeholder(R.drawable.ic_placeholder_photos)
                .error(R.drawable.ic_placeholder_photos)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imgview);
        holder.itemView.setOnClickListener(v -> onCategorybyidSelectedListner.setOnCategorybyidSelatedListner(position, trendingList.get(position)));
    }

    @Override
    public int getItemCount() {
        return trendingList.size();
    }

    public class RecyclerVH extends RecyclerView.ViewHolder {
        ImageView imgview;


        public RecyclerVH(View itemView) {
            super(itemView);
            imgview = (ImageView) itemView.findViewById(R.id.imgview);

        }
    }
}
