package com.example.logintypes.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.logintypes.MainActivity;
import com.example.logintypes.R;
import com.example.logintypes.helpers.FragmentHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashFragment extends Fragment {

    @BindView(R.id.f_splash_iv_animated_gif)
    ImageView ivAnimatedGif;

    private View view;

    //firebase
    FirebaseAuth firebaseAuth;

    //helper
    FragmentHelper fragmentHelper;

    private static SplashFragment splashFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_splash, container, false);
        ButterKnife.bind(this, view);
        Glide.with(view)
                .load(R.drawable.login_gif)
                .into(ivAnimatedGif);
        splashFragment = this;
        return view;
    }

    private void initViews() {
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            ((MainActivity) getActivity()).gotoLoginFragment(FragmentHelper.REPLACE_FRAGMENT, false,
                    null);
        } else if (firebaseUser != null) {
            ((MainActivity) getActivity()).gotoHomeFragment(FragmentHelper.REPLACE_FRAGMENT, false,
                    null);
        }
    }
}
