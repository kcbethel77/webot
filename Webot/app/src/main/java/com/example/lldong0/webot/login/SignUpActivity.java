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
import android.widget.Toast;

import com.example.lldong0.webot.R;
import com.example.lldong0.webot.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity {
    @BindView(R.id.edit_text_sign_email)
    EditText signEmail;
    @BindView(R.id.edit_text_sign_pw)
    EditText signPw;
    @BindView(R.id.btn_sign)
    Button btnSign;
    @BindView(R.id.sign_in)
    TextView tvSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getWindow().getDecorView().setBackgroundColor(Color.WHITE);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_sign)
    public void signUp() {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(signEmail.getText().toString(), signPw.getText().toString())
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("entrv", "signInWithEmail", task.getException());
                            Toast.makeText(getBaseContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            return;
                        } else {
                            Toast.makeText(getBaseContext(), "회원 가입을 축하드립니다.", Toast.LENGTH_LONG).show();
                            User user = new User(signEmail.getText().toString(), signPw.getText().toString());
                            // firebase auth 이메일에 해당하는 uid
                            String uid = task.getResult().getUser().getUid();
                            // write
                            FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(user);
                        }
                    }
                });
    }

    @OnClick(R.id.sign_in)
    public void goSignIn() {
        startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
        finish();
    }
}
