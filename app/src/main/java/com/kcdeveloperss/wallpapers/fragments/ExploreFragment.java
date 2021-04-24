package com.kcdeveloperss.wallpapers.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonElement;
import com.kcdeveloperss.wallpapers.R;
import com.kcdeveloperss.wallpapers.adapters.ExploreAdapter;
import com.kcdeveloperss.wallpapers.beans.ExploreBean;
import com.kcdeveloperss.wallpapers.network.Config;
import com.kcdeveloperss.wallpapers.network.RestClient;
import com.kcdeveloperss.wallpapers.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ExploreFragment extends Fragment implements ExploreAdapter.OnExploreSelectedListner, ExploreAdapter.OnLoadMoreListener {

    @BindView(R.id.rvExplore)
    RecyclerView rvExplore;
    Unbinder unbinder;
    ProgressDialog progressDialog;
    private ExploreAdapter exploreAdapter;
    private ArrayList<ExploreBean> explorelist = new ArrayList<>();
    private int per_page = 1;

    public ExploreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        unbinder = ButterKnife.bind(this, view);

        ProgressDialogSetup();
        exploreAdapter = new ExploreAdapter(getContext(), explorelist, rvExplore);
        exploreAdapter.setOnExploreSelectedListner(this);
        exploreAdapter.setOnLoadMoreListener(this);
        rvExplore.setAdapter(exploreAdapter);

        getExplore(per_page);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void ProgressDialogSetup() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
    }

    private void getExplore(final int per_page) {
        if (per_page == 1) {
            progressDialog.show();
        }
        Call<JsonElement> call1 = RestClient.post().getExplore(per_page,20, Config.unsplash_access_key);
        call1.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (per_page == 1) {
                    progressDialog.dismiss();
                }
                if (response.body() == null) {
                    exploreAdapter.setLoaded();
                    exploreAdapter.notifyDataSetChanged();
                }

                Log.e("Explore", response.body().toString());
                if (response.isSuccessful()) {

                    JSONArray jsonArr = null;
                    try {
                        jsonArr = new JSONArray(response.body().toString());
                        if (per_page == 1) {

                            explorelist.clear();
                            if (jsonArr.length() > 0) {
                                for (int i = 0; i < jsonArr.length(); i++) {
                                    JSONObject json2 = jsonArr.getJSONObject(i);
                                    String id = json2.getString("id");
                                    String title = json2.getString("title");
                                    explorelist.add(new ExploreBean(id,title));

                                }
                                rvExplore.setAdapter(exploreAdapter);
                            }else {
                                exploreAdapter.setLoaded();
                                exploreAdapter.notifyDataSetChanged();
                            }

                        }else {
                            if (jsonArr.length() > 0) {
                                explorelist.remove(explorelist.size() - 1);
                                exploreAdapter.notifyItemRemoved(explorelist.size());
                                for (int i = 0; i < jsonArr.length(); i++) {
                                    JSONObject json2 = jsonArr.getJSONObject(i);
                                    String id = json2.getString("id");
                                    String title = json2.getString("title");
                                    explorelist.add(new ExploreBean(id,title));
                                    exploreAdapter.notifyDataSetChanged();
                                    exploreAdapter.setLoaded();
                                }

                            }else {
                                exploreAdapter.setLoaded();
                                exploreAdapter.notifyDataSetChanged();
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }else {
                    if (per_page == 1) {
                        progressDialog.dismiss();
                    }

                    explorelist.remove(explorelist.size() - 1);
                    exploreAdapter.notifyItemRemoved(explorelist.size());
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });

    }


    @Override
    public void onLoadMore() {
        Log.e("haint", "Load More");
        if (!exploreAdapter.isLoading) {
            explorelist.add(null);
            exploreAdapter.notifyDataSetChanged();
            per_page++;
            getExplore(per_page);
        }
    }

    @Override
    public void setOnExploreSelatedListner(int position, ExploreBean dataBean) {
        AppUtils.shortToast(getContext(), "Working On It.");
    }
}