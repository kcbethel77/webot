package com.example.lldong0.webot;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class SplashActivity extends AppCompatActivity {
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.SplashTheme);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        // 인앱 기본값 설정
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
        // 서버값 fetch
        mFirebaseRemoteConfig.fetch(0)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SplashActivity.this, "Fetch Succeeded",
                                    Toast.LENGTH_SHORT).show();

                            mFirebaseRemoteConfig.activateFetched();
                        } else {
                            Toast.makeText(SplashActivity.this, "Fetch Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                        displayWelcomeMessage(); // welcome message 설정 가
                    }
                });


    }

    // splash 화면에서 welcome message 설정 함수
    // 확인차 작성
    private void displayWelcomeMessage() {
        boolean caps = mFirebaseRemoteConfig.getBoolean("splash_message_caps");
        String msg = mFirebaseRemoteConfig.getString("splash_message");
    }

    @Override
    public void onPause() {
        super.onPause();
        finish();
    }
}