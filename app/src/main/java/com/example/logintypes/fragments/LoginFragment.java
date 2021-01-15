package com.example.logintypes.fragments;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.logintypes.MainActivity;
import com.example.logintypes.R;
import com.example.logintypes.adapter.LoginAdapter;
import com.example.logintypes.helpers.FragmentHelper;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.util.concurrent.Executor;

import javax.xml.transform.sax.TemplatesHandler;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.logintypes.helpers.ConstantHelper.EMAIL;
import static com.example.logintypes.helpers.ConstantHelper.EMAIL_SMALL;
import static com.example.logintypes.helpers.ConstantHelper.FLOAT_ONE;
import static com.example.logintypes.helpers.ConstantHelper.FLOAT_ZERO;
import static com.example.logintypes.helpers.ConstantHelper.INT_THREE_HUNDRED;
import static com.example.logintypes.helpers.ConstantHelper.LONG_EIGHT_HUNDRED;
import static com.example.logintypes.helpers.ConstantHelper.LONG_FOUR_HUNDRED;
import static com.example.logintypes.helpers.ConstantHelper.LONG_SIX_HUNDRED;
import static com.example.logintypes.helpers.ConstantHelper.LONG_THOUSAND;
import static com.example.logintypes.helpers.ConstantHelper.PUBLIC_PROFILE;
import static com.example.logintypes.helpers.ConstantHelper.REGISTRATION;

public class LoginFragment extends Fragment
        implements EmailLoginFragment.OnEmailClicked, MobileVerificationFragment.OnMobileClicked {

    @BindView(R.id.f_login_tab_layout)
    TabLayout tlLogin;

    @BindView(R.id.f_login_bt_google)
    SignInButton btGoogle;

    @BindView(R.id.f_login_bt_facebook)
    LoginButton btFacebook;

    @BindView(R.id.f_login_bt_twitter)
    TwitterLoginButton btTwitter;

    @BindView(R.id.f_login_iv_login_gif)
    ImageView ivLoginAnimatedGif;

    private View view;

    private static LoginFragment loginFragment;

    GoogleSignInClient googleSignInClient;

    //helper
    FragmentHelper fragmentHelper;

    //firebase
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    //Facebook
    CallbackManager callbackManager;

    public static final int RC_SIGN_IN = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //This code must be entering before the setContentView to make the twitter login work...
        TwitterConfig config = new TwitterConfig.Builder(getContext())
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(getString(R.string.api_key), getString(R.string.api_secret_key)))
                .debug(true)
                .build();
        Twitter.initialize(config);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        loginFragment = this;
        initViews();
        return view;
    }

    private void initViews() {
        Glide.with(view)
                .load(R.drawable.splash_screen)
                .into(ivLoginAnimatedGif);
        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    ((MainActivity) getActivity()).gotoHomeFragment(FragmentHelper.REPLACE_FRAGMENT,
                            false, null);
                }
            }
        };

        tlLogin.addTab(tlLogin.newTab().setText(EMAIL));
        tlLogin.addTab(tlLogin.newTab().setText(REGISTRATION));
        ViewPager vpLogin = (ViewPager) view.findViewById(R.id.f_login_view_pager);
        LoginAdapter loginAdapter = new LoginAdapter(getFragmentManager(), getContext(), tlLogin.getTabCount());
        vpLogin.setAdapter(loginAdapter);
        tlLogin.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpLogin.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0 || tab.getPosition() == 1) {
                    loginAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        tlLogin.setTabGravity(TabLayout.GRAVITY_FILL);
        vpLogin.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tlLogin));

        btFacebook.setTranslationY(INT_THREE_HUNDRED);
        btGoogle.setTranslationY(INT_THREE_HUNDRED);
        btTwitter.setTranslationY(INT_THREE_HUNDRED);

        btFacebook.setAlpha(FLOAT_ZERO);
        btGoogle.setAlpha(FLOAT_ZERO);
        btTwitter.setAlpha(FLOAT_ZERO);
        tlLogin.setAlpha(FLOAT_ZERO);

        btFacebook.animate().translationY(FLOAT_ZERO).alpha(FLOAT_ONE).setDuration(LONG_THOUSAND).setStartDelay(LONG_FOUR_HUNDRED).start();
        btGoogle.animate().translationY(FLOAT_ZERO).alpha(FLOAT_ONE).setDuration(LONG_THOUSAND).setStartDelay(LONG_SIX_HUNDRED).start();
        btTwitter.animate().translationY(FLOAT_ZERO).alpha(FLOAT_ONE).setDuration(LONG_THOUSAND).setStartDelay(LONG_EIGHT_HUNDRED).start();
        tlLogin.animate().translationY(FLOAT_ZERO).alpha(FLOAT_ONE).setDuration(LONG_THOUSAND).setStartDelay(LONG_THOUSAND).start();
        initModels();


        //google
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(getActivity(), googleSignInOptions);
        btGoogle.setOnClickListener(view -> {
            Intent intent = googleSignInClient.getSignInIntent();
            startActivityForResult(intent, RC_SIGN_IN);
        });

        //facebook
        callbackManager=CallbackManager.Factory.create();
        btFacebook.setReadPermissions(EMAIL_SMALL,PUBLIC_PROFILE);
        btFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getContext(), getString(R.string.Facebook_signin_success), Toast.LENGTH_SHORT).show();
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {

            }
        });



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            if (signInAccountTask.isSuccessful()) {
                Toast.makeText(getContext(), R.string.google_sign_in_successful, Toast.LENGTH_SHORT);
                try {
                    GoogleSignInAccount googleSignInAccount = signInAccountTask.getResult(ApiException.class);
                    if (googleSignInAccount != null) {
                        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
                        firebaseAuth.signInWithCredential(authCredential)
                                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            ((MainActivity) getActivity()).gotoHomeFragment(FragmentHelper.REPLACE_FRAGMENT, false,
                                                    null);
                                        } else {
                                            Toast.makeText(getContext(), R.string.authentication_failed + task.getException().
                                                    getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void onClicked() {
        ((MainActivity) getActivity()).gotoEmailLoginFragment(FragmentHelper.REPLACE_FRAGMENT,
                false, null);
    }

    @Override
    public void onMobileTabClicked() {
        ((MainActivity) getActivity()).gotoMobileVerificationFragment(FragmentHelper.REPLACE_FRAGMENT,
                false, null);
    }

    private void initModels() {
        fragmentHelper = new FragmentHelper();
    }



    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            ((MainActivity) getActivity()).gotoHomeFragment(FragmentHelper.REPLACE_FRAGMENT, false,
                    null);
        }
       
    }

    @Override
    public void onPause() {
        super.onPause();
        Toast.makeText(getContext(), "OnPause", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop() {
        super.onStop();
        Toast.makeText(getContext(), "onStop", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onResume() {
        super.onResume();

        Task<AuthResult> pendingResultTask = firebaseAuth.getPendingAuthResult();
        if (pendingResultTask != null) {
            // There's something already here! Finish the sign-in for your user.
            pendingResultTask
                    .addOnSuccessListener(
                            new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {

                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getContext(), "OnResume", Toast.LENGTH_SHORT).show();
                                }
                            });
        } else {
            // There's no pending result so you need to start the sign-in flow.
            // See below.
        }


    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getContext(), getString(R.string.credential_success), Toast.LENGTH_SHORT).show();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            ((MainActivity) getActivity()).gotoHomeFragment(FragmentHelper.REPLACE_FRAGMENT,
                                    false, null);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getContext(), getString(R.string.authentication_failed),
                                    Toast.LENGTH_SHORT).show();
                            ((MainActivity) getActivity()).gotoSplashFragment(FragmentHelper.REPLACE_FRAGMENT,
                                    false, null);
                        }
                    }

    });
    }



}



