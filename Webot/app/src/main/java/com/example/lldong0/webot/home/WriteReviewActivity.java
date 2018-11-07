package com.example.lldong0.webot.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lldong0.webot.MainActivity;
import com.example.lldong0.webot.R;
import com.example.lldong0.webot.login.LoginActivity;
import com.example.lldong0.webot.model.Review;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WriteReviewActivity extends AppCompatActivity {
    @BindView(R.id.btn_cancel_review)
    Button btnCancelReview;
    @BindView(R.id.btn_write_review)
    Button btnWriteReview;
    @BindView(R.id.toolbar)
    Toolbar reviewToolbar;
    @BindView(R.id.toolbar_title)
    TextView reviewToolbarTitle;
    @BindView(R.id.review_contents)
    EditText reviewContents;

    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private Map<String, Object> reviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);
        ButterKnife.bind(this);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        if (firebaseUser == null) {
            startActivity(new Intent(WriteReviewActivity.this, LoginActivity.class));
        }
        reviews = new HashMap<>();
        setupToolbar();
    }

    private void setupToolbar() {
        // toolbar
        setSupportActionBar(reviewToolbar);
        reviewToolbarTitle.setText("상담후기 작성하기");
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reviewToolbar.setNavigationOnClickListener((v) -> {
            startActivity(new Intent(WriteReviewActivity.this, MainActivity.class));
            finish();
        });
    }

    @OnClick(R.id.btn_cancel_review)
    public void cancelReview() {
        startActivity(new Intent(WriteReviewActivity.this, MainActivity.class));
    }

    @OnClick(R.id.btn_write_review)
    public void writeReview() {
        if (TextUtils.isEmpty(reviewContents.getText())) {
            Snackbar.make(reviewContents, "후기를 작성해주세요.", Snackbar.LENGTH_LONG).show();
        } else {
            Review review = new Review(firebaseUser.getUid(), firebaseUser.getEmail(), reviewContents.getText().toString(), getCurrentTime());

            firebaseDatabase.getReference()
                    .child("reviews")
                    .push()
                    .setValue(review)
                    .addOnSuccessListener(WriteReviewActivity.this
                            , (aVoid) -> Snackbar.make(reviewContents, "후기가 작성되었습니다.", Snackbar.LENGTH_LONG).show());
        }

    }

    public String getCurrentTime() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat currentTime = new SimpleDateFormat("yyyy/MM/dd E", Locale.KOREAN);

        return currentTime.format(date);
    }

}
