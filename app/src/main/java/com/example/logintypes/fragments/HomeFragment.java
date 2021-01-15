package com.example.logintypes.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.logintypes.MainActivity;
import com.example.logintypes.R;
import com.example.logintypes.helpers.FragmentHelper;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.twitter.sdk.android.core.SessionManager;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.util.Log.e;
import static com.example.logintypes.helpers.ConstantHelper.GOOGLE_PROVIDER;
import static com.example.logintypes.helpers.ConstantHelper.PROVIDER;

public class HomeFragment extends Fragment {

    @BindView(R.id.f_home_iv_welcome)
    ImageView ivWelcomeGif;

    @BindView(R.id.f_home_iv_profile_image)
    ImageView ivProfileImage;

    @BindView(R.id.f_home_tv_name)
    TextView tvUserName;

    @BindView(R.id.f_home_bt_logout)
    Button btLogOut;

    private View view;

    private static HomeFragment homeFragment;
    //firebase
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    //google
    GoogleSignInClient googleSignInClient;

    //helper
    FragmentHelper fragmentHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        homeFragment = this;
        initViews();
        return view;
    }

    private void initViews() {
        Glide.with(view)
                .load(R.drawable.welcome)
                .into(ivWelcomeGif);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            Glide.with(this)
                    .load(firebaseUser.getPhotoUrl())
                    .into(ivProfileImage);
            tvUserName.setText(firebaseUser.getDisplayName());
        }

        googleSignInClient = GoogleSignIn.getClient(getActivity(), GoogleSignInOptions.DEFAULT_SIGN_IN);

        btLogOut.setOnClickListener(view -> {

            String provider = null;
            if (firebaseUser.getProviderData().size() > 0) {
                //Prints Out google.com for Google Sign In, prints facebook.com for Facebook
                Toast.makeText(getContext(), PROVIDER +" "+firebaseUser.getProviderData().get(firebaseUser.getProviderData().size() - 1).getProviderId(), Toast.LENGTH_SHORT).show();
                provider=firebaseUser.getProviderData().get(firebaseUser.getProviderData().size() - 1).getProviderId();
            }

           /* SessionManager<TwitterSession> sessionManager= TwitterCore.getInstance().getSessionManager();
            if(sessionManager.getActiveSession()!=null){
                sessionManager.clearActiveSession();
                firebaseAuth.signOut();
                ((MainActivity) getActivity()).gotoLoginFragment(FragmentHelper.REPLACE_FRAGMENT,false,null);
            }*/
            if (provider.equals(GOOGLE_PROVIDER)) {
                googleSignInClient.signOut().addOnCompleteListener(task ->  {
                    firebaseAuth.signOut();
                    Toast.makeText(getContext(), R.string.log_out_successful, Toast.LENGTH_SHORT);
                    ((MainActivity) getActivity()).gotoSplashFragment(FragmentHelper.REPLACE_FRAGMENT, false,
                            null);
                });
            }
            else{
                firebaseAuth.signOut();
                ((MainActivity) getActivity()).gotoSplashFragment(FragmentHelper.REPLACE_FRAGMENT, false,
                        null);
            }

        });
        initModels();
    }

    private void initModels() {
        fragmentHelper = new FragmentHelper();
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            ((MainActivity) getActivity()).gotoLoginFragment(FragmentHelper.REPLACE_FRAGMENT,false,null);
        }
        else{
            Glide.with(this)
                    .load(currentUser.getPhotoUrl())
                    .into(ivProfileImage);
            tvUserName.setText(currentUser.getDisplayName());
        }
    }
}