package com.kcdeveloperss.wallpapers.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.kcdeveloperss.wallpapers.R;
import com.kcdeveloperss.wallpapers.beans.TrendingBean;

import java.util.ArrayList;

public class TrendingAdapter extends RecyclerView.Adapter<TrendingAdapter.RecyclerVH> {

    Context context;
    ArrayList<TrendingBean> trendingList = new ArrayList<>();
    OnCategorySelectedListner onCategorySelectedListner;
    TrendingBean trendingBean;

    public TrendingAdapter(Context context, ArrayList<TrendingBean> trendingList) {
        this.context = context;
        this.trendingList= trendingList;
    }

    public void setOnCategorySelectedListner(OnCategorySelectedListner onCategorySelectedListner) {

        this.onCategorySelectedListner = onCategorySelectedListner;
    }

    public interface OnCategorySelectedListner {
        void setOnCategorySelatedListner(int position, TrendingBean trendingBean);

    }

    @NonNull
    @Override
    public RecyclerVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_trendingcat, parent, false);
        return new RecyclerVH(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull RecyclerVH holder, int position) {
        holder.tvTrendingName.setText(Html.fromHtml(trendingList.get(position).getTitle()));
        if (trendingList.get(position).isSelected()) {
            holder.catview.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorBlack)));
            holder.tvTrendingName.setTextColor(context.getResources().getColor(R.color.colorBlack));
        } else {
            holder.catview.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colortrendingLine)));
            holder.tvTrendingName.setTextColor(context.getResources().getColor(R.color.colortrendingLine));
        }
        holder.itemView.setOnClickListener(v -> onCategorySelectedListner.setOnCategorySelatedListner(position, trendingList.get(position)));
    }

    @Override
    public int getItemCount() {
        return trendingList.size();
    }


    public class RecyclerVH extends RecyclerView.ViewHolder {
        TextView tvTrendingName;

        View catview;
        public RecyclerVH(View itemView) {
            super(itemView);
            tvTrendingName = (TextView) itemView.findViewById(R.id.tvTrendingName);
            catview = (View) itemView.findViewById(R.id.catview);
        }
    }
}
