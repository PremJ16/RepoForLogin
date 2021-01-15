package com.example.logintypes;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.logintypes.fragments.EmailLoginFragment;
import com.example.logintypes.fragments.HomeFragment;
import com.example.logintypes.fragments.LoginFragment;
import com.example.logintypes.fragments.MobileVerificationFragment;
import com.example.logintypes.fragments.SignUpFragment;
import com.example.logintypes.fragments.SplashFragment;
import com.example.logintypes.helpers.FragmentHelper;

public class MainActivity extends AppCompatActivity {

    //Fragment
    SplashFragment splashFragment;
    LoginFragment loginFragment;
    MobileVerificationFragment mobileVerificationFragment;
    EmailLoginFragment emailLoginFragment;
    HomeFragment homeFragment;
    SignUpFragment signUpFragment;

    //helper
    FragmentHelper fragmentHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        gotoSplashFragment(FragmentHelper.REPLACE_FRAGMENT,
                false, null);
    }

    private void initView() {
        initModels();
    }

    private void initModels() {
        fragmentHelper = new FragmentHelper();
    }

    public void gotoSplashFragment(int action, boolean addToBackStack, String tag) {
        splashFragment = new SplashFragment();
        try {
            fragmentHelper.changeFragment(getSupportFragmentManager(), R.id.a_main_cl_container,
                    splashFragment, action, addToBackStack, tag);
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
        }
    }

    public void gotoLoginFragment(int action, boolean addToBackStack, String tag) {
        loginFragment = new LoginFragment();
        try {
            fragmentHelper.changeFragment(getSupportFragmentManager(), R.id.a_main_cl_container,
                    loginFragment, action, addToBackStack, tag);
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
        }
    }

    public void gotoEmailLoginFragment(int action, boolean addToBackStack, String tag) {
        emailLoginFragment = new EmailLoginFragment();
        try {
            fragmentHelper.changeFragment(getSupportFragmentManager(), R.id.f_login_view_pager,
                    emailLoginFragment, action, addToBackStack, tag);
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
        }
    }

    public void gotoMobileVerificationFragment(int action, boolean addToBackStack, String tag) {
        mobileVerificationFragment = new MobileVerificationFragment();
        try {
            fragmentHelper.changeFragment(getSupportFragmentManager(), R.id.f_login_view_pager,
                    mobileVerificationFragment, action, addToBackStack, tag);
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
        }
    }

    public void gotoHomeFragment(int action, boolean addToBackStack, String tag) {
        homeFragment = new HomeFragment();
        try {
            fragmentHelper.changeFragment(getSupportFragmentManager(), R.id.a_main_cl_container,
                    homeFragment, action, addToBackStack, tag);
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
        }
    }

    public void gotoSignUpFragment(int action, boolean addToBackStack, String tag) {
        signUpFragment = new SignUpFragment();
        try {
            fragmentHelper.changeFragment(getSupportFragmentManager(), R.id.f_login_view_pager,
                    signUpFragment, action, addToBackStack, tag);
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
        }
    }


}