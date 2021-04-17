package com.kcdeveloperss.wallpapers.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
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

import com.kcdeveloperss.wallpapers.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;


public class HomeFragment extends Fragment {

    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 1;
    private static final int REQUEST_PERMISSION_SETTING = 0;
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
    @BindView(R.id.rvTrendingphotosbyId)
    RecyclerView rvTrendingphotosbyId;
    @BindView(R.id.rvTrending)
    RecyclerView rvTrending;
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

    private TrendingAdapter trendingAdapter;
    private ArrayList<TrendingBean> trendingList = new ArrayList<>();

    private TrendingPhotoByIdAdapter trendingPhotoByIdAdapter;
    private ArrayList<TrendingBean> trendingPhotosByIdList = new ArrayList<>();

    private ArrayList<PhotosBean> randomList = new ArrayList<>();
    private ProgressDialog progressDialog;

    String url;
    Bitmap anImage;
    String randomPhotoId;
    String  alt_description;
    String  exploretitle;
    private AsyncTask mMyTask;
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

        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    exploretitle = edtSearch.getText().toString();

                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    edtSearch.setText("");
                    return true;
                }
                return false;
            }
        });
        return view;
    }
}