package com.example.lldong0.webot.note;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.lldong0.webot.R;
import com.example.lldong0.webot.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

public class TaskFragment extends Fragment {
    @Nullable
    @BindView(R.id.homework_recyclerView)
    RecyclerView taskRecyclerView;
    @Nullable
    @BindView(R.id.empty_task)
    TextView emptyTask;
    @Nullable
    @BindView(R.id.require_id_button)
    Button btnGoLogin;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private TaskAdapter taskAdapter;
    private boolean isExistUser = false;

    public static TaskFragment newInstance() {
        return new TaskFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v;

        if (firebaseAuth.getCurrentUser() != null) {
            v = inflater.inflate(R.layout.fragment_task_list, container, false);
            isExistUser = true;
        } else {
            v = inflater.inflate(R.layout.fragment_not_login, container, false);
            isExistUser = false;
        }

        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TaskReader taskReader = new TaskReader();
        if (isExistUser) {
            taskReader.postTasks((taskList, taskUidList) -> {

                if (taskRecyclerView != null) {

                    taskAdapter = new TaskAdapter(taskList, taskUidList);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    linearLayoutManager.setReverseLayout(true);
                    linearLayoutManager.setStackFromEnd(true);
                    taskRecyclerView.setLayoutManager(linearLayoutManager);
                    taskRecyclerView.setAdapter(taskAdapter);

                    if (taskList.size() == 0 && emptyTask != null) {
                        emptyTask.setVisibility(View.VISIBLE);
                        taskRecyclerView.setVisibility(View.INVISIBLE);
                    } else if (emptyTask != null) {
                        emptyTask.setVisibility(View.INVISIBLE);
                        taskRecyclerView.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    @Optional
    @OnClick(R.id.require_id_button)
    public void require(View v) {
        startActivity(new Intent(v.getContext(), LoginActivity.class));
        if (getActivity() != null)
            getActivity().finish();
    }
}