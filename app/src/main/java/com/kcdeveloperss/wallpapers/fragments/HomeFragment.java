package com.kcdeveloperss.wallpapers.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.JsonElement;
import com.kcdeveloperss.wallpapers.MainActivity;
import com.kcdeveloperss.wallpapers.R;
import com.kcdeveloperss.wallpapers.adapters.NewPhotosAdapter;
import com.kcdeveloperss.wallpapers.adapters.TrendingAdapter;
import com.kcdeveloperss.wallpapers.adapters.TrendingPhotoByIdAdapter;
import com.kcdeveloperss.wallpapers.beans.PhotosBean;
import com.kcdeveloperss.wallpapers.beans.TrendingBean;
import com.kcdeveloperss.wallpapers.network.Config;
import com.kcdeveloperss.wallpapers.network.RestClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment implements NewPhotosAdapter.OnPhotoSelectedListner {

    @BindView(R.id.edtSearch)
    EditText edtSearch;
    @BindView(R.id.cv_Share)
    ImageView cvShare;
    @BindView(R.id.cv_like)
    CheckBox cvLike;
    @BindView(R.id.cv_download)
    ImageView cvDownload;
    @BindView(R.id.ivUserProfile)
    CircleImageView ivUserProfile;
    Unbinder unbinder;
    @BindView(R.id.rvNewPhotos)
    RecyclerView rvNewPhotos;
//    @BindView(R.id.rvTrendingphotosbyId)
//    RecyclerView rvTrendingphotosbyId;
//    @BindView(R.id.rvTrending)
//    RecyclerView rvTrending;
    @BindView(R.id.ivRandom)
    ImageView ivRandom;
    @BindView(R.id.ivDownlod)
    ImageView ivDownlod;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.tvDesc)
    TextView tvDesc;
    String trendId;
    String sharlink;
    private NewPhotosAdapter newPhotosAdapter;
    private ArrayList<PhotosBean> newPhotoslist = new ArrayList<>();

//    private TrendingAdapter trendingAdapter;
//    private ArrayList<TrendingBean> trendingList = new ArrayList<>();
//
//    private TrendingPhotoByIdAdapter trendingPhotoByIdAdapter;
//    private ArrayList<TrendingBean> trendingPhotosByIdList = new ArrayList<>();

    private ArrayList<PhotosBean> randomList = new ArrayList<>();
    private ProgressDialog progressDialog;

    Bitmap anImage;
    String randomPhotoId;
    String  alt_description;
    String  exploretitle;
    private ProgressDialog mProgressDialog;
    private static final int PERMISSION_REQUEST_CODE = 1;
    String wantPermission = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        // Initialize the progress dialog

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        // Progress dialog horizontal style
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // Progress dialog title
        mProgressDialog.setTitle("Loading.....");
        // Progress dialog message
        mProgressDialog.setMessage("Please wait...");
        if (checkPermission(wantPermission)) {
            Toast.makeText(getActivity(), "Permission already granted.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), "Please grant permission.", Toast.LENGTH_LONG).show();
        }
        if (!checkPermission(wantPermission)) {
            requestPermission(wantPermission);
        } else {
            Toast.makeText(getActivity(),"Permission already granted.", Toast.LENGTH_LONG).show();
        }

        ProgressDialogSetup();
        getRandom();
        getNewPhotos();
        edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                exploretitle = edtSearch.getText().toString();

                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                SearchFragment searchFragment = SearchFragment.newInstance(exploretitle);
                loadFragment(searchFragment);
                edtSearch.setText("");
                return true;
            }
            return false;
        });
        return view;
    }


    public void ProgressDialogSetup() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void loadFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        boolean fragmentPopped = fragmentManager.popBackStackImmediate(backStateName, 0);
        if (!fragmentPopped) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fl_container, fragment, null);
            fragmentTransaction.hide(HomeFragment.this);
            fragmentTransaction.addToBackStack(backStateName);
            fragmentTransaction.commit();
        }
    }

    @OnClick({R.id.edtSearch, R.id.cv_Share, R.id.cv_like, R.id.cv_download,R.id.ivRandom})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.edtSearch:
            case R.id.cv_like:
                break;
            case R.id.cv_Share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, sharlink);
                startActivity(Intent.createChooser(shareIntent, alt_description));
                break;
            case R.id.ivRandom:
                DetailFragment newDetailsFragment = DetailFragment.newInstance(randomPhotoId);
                loadFragment(newDetailsFragment);
                break;
            case R.id.cv_download:
                anImage = ((BitmapDrawable) ivRandom.getDrawable()).getBitmap();
                saveImageToExternalStorage(anImage);
                Toast.makeText(getActivity(), "Download successfully", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void saveImageToExternalStorage(Bitmap finalBitmap) {
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        File myDir = new File(root + "/WallKing");
        myDir.mkdirs();
        Random random = new Random();
        int n = 10000;
        n = random.nextInt(n);
        String fname = "Image-WallKing-" + n + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Tell the media scanner about the new file so that it is
        // immediately available to the user.
        //MediaStore.Images.Media.insertImage(getContentResolver(), yourBitmap, yourTitle , yourDescription)

        MediaScannerConnection.scanFile(getActivity(), new String[]{file.toString()}, null,
                (path, uri) -> {
                    Log.i("ExternalStorage", "Scanned " + path + ":");
                    Log.i("ExternalStorage", "-> uri=" + uri);
                });
    }

    private boolean checkPermission(String permission){
        if (Build.VERSION.SDK_INT >= 23) {
            int result = ContextCompat.checkSelfPermission(getActivity(), permission);
            return result == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    private void requestPermission(String permission){
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)){
            Toast.makeText(getActivity(), "Write external storage permission allows us to write data. \n" +
                    "Please allow in App Settings for additional functionality",Toast.LENGTH_LONG).show();
        }
        ActivityCompat.requestPermissions(getActivity(), new String[]{permission},PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Permission Granted.",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "Permission Denied.",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private void getRandom() {
        progressDialog.show();
        Call<JsonElement> call1 = RestClient.post().getRandom(1, 1, Config.pexel_api_key);
        call1.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                progressDialog.dismiss();
                randomList.clear();
                Log.e("random", response.body().toString());
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());

                        JSONArray jsonArray= jsonObject.getJSONArray("photos");

                        int length = jsonArray.length();


                        if (length > 0) {
                            for (int i = 0; i < length; i++) {

                                JSONObject object = jsonArray.getJSONObject(i);

                                int id = object.getInt("id");

                                JSONObject objectImages = object.getJSONObject("src");

                                String originalUrl = objectImages.getString("original");
                                String mediumUrl = objectImages.getString("medium");

                                randomList.add(new PhotosBean(id, originalUrl, mediumUrl));
                                Glide.with(getActivity()).load(mediumUrl)
                                        .thumbnail(0.5f)
                                        .placeholder(R.drawable.ic_place_holder)
                                        .error(R.drawable.ic_place_holder)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(ivRandom);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void getNewPhotos() {
        Call<JsonElement> call1 = RestClient.post().getNewPhotos(1, 80, Config.pexel_api_key);
        call1.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                newPhotoslist.clear();
                Log.e("FeatureNews", response.body().toString());
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());

                        JSONArray jsonArray= jsonObject.getJSONArray("photos");

                        int length = jsonArray.length();
                        if (length > 0) {
                            for (int i = 0; i < length; i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                int id = object.getInt("id");

                                JSONObject objectImages = object.getJSONObject("src");

                                String originalUrl = objectImages.getString("original");
                                String mediumUrl = objectImages.getString("medium");

                                newPhotoslist.add(new PhotosBean(id,originalUrl,mediumUrl));

                            }

                            bindTrendData();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {

            }
        });
    }

    private void bindTrendData() {
        if (newPhotoslist.size() > 0) {
            newPhotosAdapter = new NewPhotosAdapter(getActivity(), newPhotoslist);
            newPhotosAdapter.setOnCategorySelectedListner(this);
            rvNewPhotos.setAdapter(newPhotosAdapter);
        }
    }

    @Override
    public void setOnPhotoSelatedListner(int position, PhotosBean dataBean) {
        DetailFragment newsDetailsFragment = DetailFragment.newInstance(dataBean.getMediumUrl());
        loadFragment(newsDetailsFragment);
    }

//    @Override
//    public void setOnCategorySelatedListner(int position, TrendingBean trendingBean) {
////        for (int i = 0; i < trendingList.size(); i++) {
////            trendingList.get(i).setSelected(false);
////        }
////        if (trendingList.size() > 0) {
////            trendingAdapter.notifyDataSetChanged();
////            trendId = trendingBean.getId();
////            trendingBean.setSelected(true);
////            getTrendPhotosById();
////        }
//    }
//
//    @Override
//    public void setOnCategorybyidSelatedListner(int position, PhotosBean photosBean) {
//        DetailFragment newsDetailsFragment = DetailFragment.newInstance(photosBean.getMediumUrl());
//        ((MainActivity) getActivity()).loadFragment(newsDetailsFragment);
//    }
}