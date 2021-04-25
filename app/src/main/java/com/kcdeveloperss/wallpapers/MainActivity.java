package com.kcdeveloperss.wallpapers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kcdeveloperss.wallpapers.fragments.ExploreFragment;
import com.kcdeveloperss.wallpapers.fragments.HomeFragment;
import com.kcdeveloperss.wallpapers.utils.AppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.ivDrawer)
    ImageView ivDrawer;
    @BindView(R.id.txtX)
    TextView txtX;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.AppBar)
    LinearLayout AppBar;
    @BindView(R.id.fl_container)
    FrameLayout flContainer;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigation;

    boolean clickAgainToExit = false;
    private boolean activityStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        if (activityStarted &&
//                getIntent() != null &&
//                (getIntent().getFlags() & Intent.FLAG_ACTIVITY_REORDER_TO_FRONT) != 0) {
//            finish();
//            return;
//        }

        activityStarted = true;

        loadFragment(new HomeFragment());

        bottomNavigation.setOnNavigationItemSelectedListener(this);

    }

    @OnClick({R.id.ivDrawer, R.id.bottom_navigation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivDrawer:
            case R.id.bottom_navigation:

                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // Home
            case R.id.navigation_home:
                loadFragment(new HomeFragment());
            case R.id.navigation_explore:
                loadFragment(new ExploreFragment());
            case R.id.navigation_favourite:
                AppUtils.shortToast(getApplicationContext(), "Working On It.");
//                loadFragment();
        }
        return false;
    }

    public void loadFragment(Fragment fragment) {
        // Insert the fragment by replacing any existing fragment
        String backStateName = fragment.getClass().getName();

        FragmentManager fragmentManager = getSupportFragmentManager();
        boolean fragmentPopped  = fragmentManager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped) {
            //fragment not in back stack, create it.
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fl_container, fragment, backStateName);
            fragmentTransaction.addToBackStack(backStateName);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStackImmediate();
        if (fragmentManager.getBackStackEntryCount() == 1) {
            if (clickAgainToExit) {
                super.onBackPressed();
                finish();
                return;
            }
            clickAgainToExit = true;
            AppUtils.shortToast(MainActivity.this, getResources().getString(R.string.app_backpress));
            new Handler().postDelayed(() -> clickAgainToExit = false, 2000);
        } else {
            super.onBackPressed();
        }
    }
}