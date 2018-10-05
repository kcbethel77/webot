package com.example.lldong0.webot;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignInActivity extends AppCompatActivity {
    protected @BindView(R.id.edit_text_pw)
    EditText etPw;
    protected @BindView(R.id.edit_text_email)
    EditText etEmail;
    protected @BindView(R.id.sign_up)
    TextView tvSignUp;
    protected @BindView(R.id.btn_login)
    Button btnLogin;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginEvent();
            }
        });

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

    public void loginEvent() {
        firebaseAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etPw.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // 로그인 판단
                if (!task.isSuccessful()) {
                    Toast.makeText(SignInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("sign", "Sign In Fail");
                } else {
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
