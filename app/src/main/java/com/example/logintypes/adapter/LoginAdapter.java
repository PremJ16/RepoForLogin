package com.example.logintypes.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.logintypes.fragments.EmailLoginFragment;
import com.example.logintypes.fragments.SignUpFragment;

public class LoginAdapter extends FragmentPagerAdapter {

    private final Context context;
    private final int totalTabs;

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                EmailLoginFragment emailLoginFragment = new EmailLoginFragment();
                return emailLoginFragment;
            case 1:
                SignUpFragment signUpFragment = new SignUpFragment();
                return signUpFragment;
            default:
                return null;
        }
    }

    public LoginAdapter(FragmentManager fragmentManager, Context context, int totalTabs) {
        super(fragmentManager);
        this.context = context;
        this.totalTabs = totalTabs;
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
