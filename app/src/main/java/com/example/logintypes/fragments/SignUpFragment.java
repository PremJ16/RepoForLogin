package com.example.logintypes.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.logintypes.MainActivity;
import com.example.logintypes.R;
import com.example.logintypes.helpers.FragmentHelper;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.logintypes.helpers.ConstantHelper.CONFIRM_PASSWORD;
import static com.example.logintypes.helpers.ConstantHelper.EMAIL_ID;
import static com.example.logintypes.helpers.ConstantHelper.PASSWORD;

public class SignUpFragment extends Fragment {

    @BindView(R.id.f_sign_up_et_email)
    EditText etEmail;

    @BindView(R.id.f_sign_up_et_password)
    EditText etPassword;

    @BindView(R.id.f_sign_up_et_confirm_password)
    EditText etConfirmPassword;

    @BindView(R.id.f_sign_up_bt_sign_up)
    Button btSignUp;

    @BindView(R.id.f_sign_up_tv_error_msg)
    TextView tvErrorMsg;

    private View view;

    //firebase
    FirebaseAuth firebaseAuth;

    //helper
    FragmentHelper fragmentHelper;

    private static SignUpFragment signUpFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        ButterKnife.bind(this, view);
        signUpFragment = this;
        initViews();
        return view;
    }

    private void initViews() {
        initModels();
        firebaseAuth = FirebaseAuth.getInstance();
        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String pwd = etPassword.getText().toString();
                String confirmPwd = etConfirmPassword.getText().toString();

                if (!confirmPwd.equals(pwd)) {
                    tvErrorMsg.setText(R.string.password_does_not_match);
                } else {
                    tvErrorMsg.setText(R.string.empty_text);
                }


                if (email.isEmpty()) {
                    etEmail.setError(EMAIL_ID);
                    etEmail.requestFocus();
                } else if (pwd.isEmpty()) {
                    etPassword.setError(PASSWORD);
                    etPassword.requestFocus();
                } else if (confirmPwd.isEmpty()) {
                    etConfirmPassword.setError(CONFIRM_PASSWORD);
                    etConfirmPassword.requestFocus();
                } else if (email.isEmpty() && pwd.isEmpty()) {
                    Toast.makeText(getContext(), R.string.Fields_are_empty, Toast.LENGTH_SHORT).show();
                } else if (!(email.isEmpty() && pwd.isEmpty())) {
                    firebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(getActivity(), task -> {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getContext(), R.string.sign_up_unsuccessful, Toast.LENGTH_SHORT).show();
                        } else {
                            ((MainActivity) getActivity()).gotoHomeFragment(FragmentHelper.REPLACE_FRAGMENT,
                                    false, null);
                        }
                    });
                } else {
                    Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initModels() {
        fragmentHelper = new FragmentHelper();
    }
}
