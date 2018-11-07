package com.example.lldong0.webot.note;

import android.util.Log;

import com.example.lldong0.webot.model.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TaskReader{
    private String userUid = FirebaseAuth.getInstance().getUid();
    private DatabaseReference postReference = FirebaseDatabase.getInstance().getReference("users/" + userUid);

    private List<Task> tasksList;
    private List<String> tasksUidList;

    public void postTasks(final TaskListener taskListener) {

        postReference.child("tasks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                tasksList = new ArrayList<>();
                tasksUidList = new ArrayList<>();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    // get data from FireBase
                    Task getTask = data.getValue(Task.class);
                    // init
                    Task newTask = null;
                    // add data
                    if (getTask != null) {
                        newTask = new Task(getTask.getAssignment()
                                , getTask.getCognitiveError()
                                , getTask.getAutomaticThought()
                                , getTask.getChecked()
                                , getTask.getWrittenTime());
                    }
                    // add to ArrayList
                    tasksList.add(newTask);
                    tasksUidList.add(data.getKey());
                }
                taskListener.taskListener(tasksList, tasksUidList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("fail", databaseError.toException());
            }
        });
    }

}
