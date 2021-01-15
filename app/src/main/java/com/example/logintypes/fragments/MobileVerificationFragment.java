package com.example.logintypes.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.logintypes.R;

import butterknife.ButterKnife;

public class MobileVerificationFragment extends Fragment {
    private View view;

    private static MobileVerificationFragment mobileVerificationFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mobile_verification, container, false);
        ButterKnife.bind(this, view);
        mobileVerificationFragment = this;
        initViews();
        return view;
    }

    private void initViews() {

    }

    public interface OnMobileClicked {
        void onMobileTabClicked();
    }
}
