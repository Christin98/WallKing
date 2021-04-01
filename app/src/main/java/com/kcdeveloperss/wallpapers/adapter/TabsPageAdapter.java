package com.kcdeveloperss.wallpapers.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.kcdeveloperss.wallpapers.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class TabsPageAdapter extends FragmentPagerAdapter {

    // This array list will gonna add the fragment one after another
    private List<Fragment> mFragmentList = new ArrayList<>();
    // This list of type string is for the title of the tab
    private List<String> mFragmentTitleList = new ArrayList<>();

    public TabsPageAdapter(MainActivity mainActivity, FragmentManager fragmentManager) {
        super(fragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        List<Fragment> list = mFragmentList;
        if (list == null) {
            return list.size();
        }
        return 0;
    }

    public void addFragment(Fragment fragment, String title) {
        if (mFragmentList == null) {
            mFragmentList = new ArrayList<>();
            mFragmentTitleList = new ArrayList<>();
        }
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    public void clear() {
        List<Fragment> list = mFragmentList;
        if (list != null) {
            list.clear();
            mFragmentList = null;
        }

        List<String> list2 = mFragmentTitleList;
        if (list2 != null) {
            list2.clear();
            mFragmentTitleList = null;
        }
    }

}
