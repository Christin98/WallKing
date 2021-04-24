package com.kcdeveloperss.wallpapers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kcdeveloperss.wallpapers.R;
import com.kcdeveloperss.wallpapers.beans.ExploreBean;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    Context context;
    private LayoutInflater inflater;
    ArrayList<ExploreBean> exploreList = new ArrayList<>();
    OnCategorybyidSelectedListner onCategorybyidSelectedListner;
    ExploreBean exploreBean;
    RecyclerView mRecyclerView;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener mOnLoadMoreListener;
    public boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public SearchAdapter(final Context context, ArrayList<ExploreBean> exploreList, RecyclerView recyclerView) {
        this.context = context;
        this.exploreList = exploreList;
        this.mRecyclerView = recyclerView;
        inflater = LayoutInflater.from(context);
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return exploreList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void setOnCategorybyidSelectedListner(OnCategorybyidSelectedListner onCategorybyidSelectedListner) {
        this.onCategorybyidSelectedListner = onCategorybyidSelectedListner;
    }

    public interface OnCategorybyidSelectedListner {
        void setOnCategorybyidSelatedListner(int position, ExploreBean exploreBean);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_explorephotosbyid, parent, false);
            return new MyViewholder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loading_item, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewholder) {
            final MyViewholder myHolder = (MyViewholder) holder;
            Glide.with(context).load(exploreList.get(position).getRegular())
                    .thumbnail(0.5f)
                    .placeholder(R.drawable.ic_placeholder_photos)
                    .error(R.drawable.ic_placeholder_photos)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(myHolder.imgView);
            holder.itemView.setOnClickListener(v -> onCategorybyidSelectedListner.setOnCategorybyidSelatedListner(position, exploreList.get(position)));
        } else {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);

            if (!isLoading) {
                loadingViewHolder.progressBar.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return exploreList.size();
    }

    public class MyViewholder extends RecyclerView.ViewHolder {
        ImageView imgView;

        public MyViewholder(@NonNull View itemView) {
            super(itemView);
            imgView = itemView.findViewById(R.id.imgview);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar1);
        }
    }

    public void setLoaded() {
        isLoading = false;
    }
}
