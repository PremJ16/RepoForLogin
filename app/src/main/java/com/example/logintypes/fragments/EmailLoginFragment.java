package com.example.logintypes.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.logintypes.MainActivity;
import com.example.logintypes.R;
import com.example.logintypes.helpers.FragmentHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.logintypes.helpers.ConstantHelper.EMAIL_ID;
import static com.example.logintypes.helpers.ConstantHelper.FLOAT_ONE;
import static com.example.logintypes.helpers.ConstantHelper.FLOAT_ZERO;
import static com.example.logintypes.helpers.ConstantHelper.LONG_EIGHT_HUNDRED;
import static com.example.logintypes.helpers.ConstantHelper.LONG_FOUR_HUNDRED;
import static com.example.logintypes.helpers.ConstantHelper.LONG_SIX_HUNDRED;
import static com.example.logintypes.helpers.ConstantHelper.PASSWORD;

public class EmailLoginFragment extends Fragment {

    @BindView(R.id.f_email_login_et_email)
    EditText etEmail;

    @BindView(R.id.f_email_login_et_password)
    EditText etPassword;

    @BindView(R.id.f_email_login_bt_login)
    Button btLogin;

    private View view;

    private static EmailLoginFragment emailLoginFragment;

    //helper
    FragmentHelper fragmentHelper;

    //firebase
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_email_login, container, false);
        ButterKnife.bind(this, view);
        emailLoginFragment = this;
        initViews();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    private void initViews() {
        initModels();
        firebaseAuth = FirebaseAuth.getInstance();
        etEmail.setTranslationX(LONG_EIGHT_HUNDRED);
        etPassword.setTranslationX(LONG_EIGHT_HUNDRED);
        btLogin.setTranslationX(LONG_EIGHT_HUNDRED);


        etEmail.setAlpha(FLOAT_ZERO);
        etPassword.setAlpha(FLOAT_ZERO);
        btLogin.setAlpha(FLOAT_ZERO);

        etEmail.animate().translationX(FLOAT_ZERO).alpha(FLOAT_ONE).setDuration(LONG_EIGHT_HUNDRED).
                setStartDelay(LONG_FOUR_HUNDRED).start();
        etPassword.animate().translationX(FLOAT_ZERO).alpha(FLOAT_ONE).setDuration(LONG_EIGHT_HUNDRED).
                setStartDelay(LONG_SIX_HUNDRED).start();
        btLogin.animate().translationX(FLOAT_ZERO).alpha(FLOAT_ONE).setDuration(LONG_EIGHT_HUNDRED).
                setStartDelay(LONG_EIGHT_HUNDRED).start();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    ((MainActivity) getActivity()).gotoHomeFragment(FragmentHelper.REPLACE_FRAGMENT,
                            false, null);
                } else {
                    Toast.makeText(getContext(), R.string.please_login, Toast.LENGTH_SHORT).show();
                }
            }
        };

        btLogin.setOnClickListener(view -> {
            String email = etEmail.getText().toString();
            String pwd = etPassword.getText().toString();
            if (email.isEmpty()) {
                etEmail.setError(EMAIL_ID);
                etPassword.requestFocus();
            } else if (pwd.isEmpty()) {
                etPassword.setError(PASSWORD);
                etEmail.requestFocus();
            } else if (email.isEmpty() && pwd.isEmpty()) {
                Toast.makeText(getContext(), R.string.Fields_are_empty, Toast.LENGTH_SHORT).show();
            } else if (!(email.isEmpty() && pwd.isEmpty())) {
                firebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(getActivity(), (task) -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(getContext(), R.string.login_error, Toast.LENGTH_SHORT).show();
                    } else {
                        ((MainActivity) getActivity()).gotoHomeFragment(FragmentHelper.REPLACE_FRAGMENT,
                                false, null);
                    }
                });
            } else {
                Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface OnEmailClicked {
        void onClicked();
    }

    private void initModels() {
        fragmentHelper = new FragmentHelper();
    }
}