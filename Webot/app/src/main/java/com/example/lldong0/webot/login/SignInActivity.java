package com.example.lldong0.webot.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lldong0.webot.MainActivity;
import com.example.lldong0.webot.R;
import com.example.lldong0.webot.chat.ChatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInActivity extends AppCompatActivity {
    @BindView(R.id.edit_text_pw)
    EditText etPw;
    @BindView(R.id.edit_text_email)
    EditText etEmail;
    @BindView(R.id.sign_up)
    TextView tvSignUp;
    @BindView(R.id.btn_login)
    Button btnLogin;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getWindow().getDecorView().setBackgroundColor(Color.WHITE);
        ButterKnife.bind(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();

        // login interface listener
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("authState", "onAuthStateChanged:signed_in:" + user.getUid());
                    Intent intent = new Intent(SignInActivity.this, ChatActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // User is signed out
                    Log.d("authState", "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @OnClick(R.id.sign_up)
    public void signUp() {
        startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
    }

    @OnClick(R.id.btn_login)
    public void buttonLogin() {
        loginEvent();
    }

    public void loginEvent() {
        firebaseAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etPw.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // 로그인 판단
                if (!task.isSuccessful()) {
                    Log.d("sign", "Sign In Fail");
                } else {
                    startActivity(new Intent(SignInActivity.this, MainActivity.class));
                    Log.d("sign", "Sign In Success");
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 액티비티 실행시 리스너 연결
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 액티비티 종료시 리스너 제거
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}
