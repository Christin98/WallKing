package com.kcdeveloperss.wallpapers;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;

import com.google.android.material.tabs.TabLayout;
import com.kcdeveloperss.wallpapers.adapter.TabsPageAdapter;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;
    TabsPageAdapter tabsPageAdapter;

    private final String[] tabTitle = {"Home", "Live Wallpaper", "Exclusive Wallpaper", "Trending", "Category"};

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpWindowAnimation();

        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);
        setupViewPager(viewPager);
        invalidateOptionsMenu();
    }

    private void setupViewPager(ViewPager viewPager) {
        viewPager.setOffscreenPageLimit(5);
        tabsPageAdapter = new TabsPageAdapter(this, getSupportFragmentManager());
        Bundle bundle = new Bundle();
        WallFragment wallFragment = new WallFragment();
        bundle.putString("Category", "Home");
        tabsPageAdapter.addFragment(wallFragment, "Home");
        Bundle bundle1 = new Bundle();
        WallFragment wallFragment1 = new WallFragment();
        bundle1.putString("Category", "Home");
        tabsPageAdapter.addFragment(wallFragment1, "Home");
        Bundle bundle2 = new Bundle();
        WallFragment wallFragment2 = new WallFragment();
        bundle2.putString("Category", "Home");
        tabsPageAdapter.addFragment(wallFragment2, "Home");
        Bundle bundle3 = new Bundle();
        WallFragment wallFragment3 = new WallFragment();
        bundle3.putString("Category", "Home");
        tabsPageAdapter.addFragment(wallFragment3, "Home");
        Bundle bundle4 = new Bundle();
        WallFragment wallFragment4 = new WallFragment();
        bundle4.putString("Category", "Home");
        tabsPageAdapter.addFragment(wallFragment4, "Home");
        Bundle bundle5 = new Bundle();
        WallFragment wallFragment5 = new WallFragment();
        bundle5.putString("Category", "Home");
        tabsPageAdapter.addFragment(wallFragment5, "Home");
        Bundle bundle6 = new Bundle();
        WallFragment wallFragment6 = new WallFragment();
        bundle6.putString("Category", "Home");
        tabsPageAdapter.addFragment(wallFragment6, "Home");
        viewPager.setAdapter(tabsPageAdapter);
    }

    private void setUpTabs() {
        tabLayout.getTabAt(0).setText(tabTitle[0]);
        tabLayout.getTabAt(1).setText(tabTitle[1]);
        tabLayout.getTabAt(2).setText(tabTitle[2]);
        tabLayout.getTabAt(3).setText(tabTitle[3]);
        tabLayout.getTabAt(4).setText(tabTitle[4]);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setUpWindowAnimation() {
        Fade fade = new Fade();
        fade.setDuration(1000);
        getWindow().setExitTransition(fade);
    }
}