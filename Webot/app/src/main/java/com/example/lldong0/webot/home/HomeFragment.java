package com.example.lldong0.webot.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.lldong0.webot.R;
import com.example.lldong0.webot.chat.ChatActivity;
import com.example.lldong0.webot.helper.CreateDialogHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class HomeFragment extends Fragment {

    @BindView(R.id.btn_start_chat)
    TextView btnStartChat;
    @BindView(R.id.home_btn_write_review)
    Button writeReview;
    @BindView(R.id.review_recycler_view)
    RecyclerView reviewRecyclerView;

    private ReviewAdapter reviewAdapter;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // Run after activity onCreate method
        ReviewReader reviewReader = new ReviewReader();
        super.onActivityCreated(savedInstanceState);

        reviewReader.postReviews((reviewArrayList) -> {

            reviewAdapter = new ReviewAdapter(reviewArrayList);
            reviewRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            reviewRecyclerView.setAdapter(reviewAdapter);

        });
    }

    @OnClick(R.id.btn_start_chat)
    public void start() {
        startActivity(new Intent(getActivity(), ChatActivity.class));
    }

    @OnClick(R.id.home_btn_write_review)
    public void write() {
        CreateDialogHelper createDialogHelper = new CreateDialogHelper();
        if (createDialogHelper.loginDialog(getContext())) {
            startActivity(new Intent(getActivity(), WriteReviewActivity.class));
        }
    }


}
