package com.example.lldong0.webot.note;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CheckingTask {
    private String userUid = FirebaseAuth.getInstance().getUid();
    private DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users/" + userUid);

    public void setChecking(String homeworkUid, Boolean bool) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String userUid = FirebaseAuth.getInstance().getUid();
            if (userUid != null && !userUid.equals(""))
                myRef.child("tasks").child(homeworkUid).child("checked").setValue(bool);
        } else {
            System.out.println("error");
        }
    }
}
