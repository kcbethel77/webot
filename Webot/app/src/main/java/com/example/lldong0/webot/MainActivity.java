package com.example.lldong0.webot;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.lldong0.webot.chat.ChatActivity;
import com.example.lldong0.webot.helper.BackPressCloseHelper;
import com.example.lldong0.webot.helper.BottomNavigationViewHelper;
import com.example.lldong0.webot.home.HomeFragment;
import com.example.lldong0.webot.note.TaskFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView mainToolbarTitle;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    private Fragment selectedFragment;
    private FragmentTransaction transaction;
    private BackPressCloseHelper backPressCloseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getWindow().getDecorView().setBackgroundColor(Color.WHITE);
        ButterKnife.bind(this);

        setUpToolbar();
        setUpFragment();
        setUpBottomNavigation();
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        mainToolbarTitle.setText(R.string.mainToolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setUpFragment() {
        selectedFragment = HomeFragment.newInstance();
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, selectedFragment);
        transaction.commit();
    }

    private void setUpBottomNavigation() {
        //back button push handler
        backPressCloseHelper = new BackPressCloseHelper(this);
        try {
            BottomNavigationViewHelper.disableShiftMode(bottomNavigationView); //이동 모드 해제
            bottomNavigationView.setOnNavigationItemSelectedListener((item) -> {
                selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.action_home:
                        mainToolbarTitle.setText(R.string.mainToolbar);
                        selectedFragment = HomeFragment.newInstance();
                        break;
                    case R.id.action_chatting:
                        Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.action_note:
                        mainToolbarTitle.setText("심리노트");
                        selectedFragment = TaskFragment.newInstance();
                        break;
                    default:
                        break;
                }
                if (selectedFragment != null) {
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_layout, selectedFragment);
                    transaction.commit();
                }
                return true;
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        backPressCloseHelper.onBackPressed();
    }
}