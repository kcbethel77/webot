package com.example.lldong0.webot.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.lldong0.webot.R;
import com.example.lldong0.webot.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class CreateDialogHelper {
    private String taskUid;
    private String userUid = FirebaseAuth.getInstance().getUid();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public CreateDialogHelper() {
    }

    public CreateDialogHelper(String taskUid) {
        this.taskUid = taskUid;
    }

    // 삭제 다이얼로그 설정.
    public void showDialog(Context context) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        dialog.setTitle("과제 삭제").setMessage("정말로 삭제 하시겠습니까?").setIcon(R.drawable.ic_face_scanner).setCancelable(true)
                .setNegativeButton("취소", (dialogInterface, which) -> dialogInterface.cancel())
                .setPositiveButton("삭제", (dialogInterface, which) -> {
                    try {
                        FirebaseDatabase.getInstance()
                                .getReference("users/" + userUid)
                                .child("tasks")
                                .child(taskUid)
                                .removeValue((databaseError, databaseReference) -> Toast.makeText(context, "심리 노트가 삭제되었습니다.", Toast.LENGTH_SHORT).show());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

        dialog.show();
    }

    public Boolean loginDialog(Context context) {

        if (firebaseAuth.getCurrentUser() != null) {
            return true;
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);

            dialog.setTitle("로그인 안내").setMessage("로그인이 필요한 기능입니다." + "\n" + "로그인 하시겠습니까?").setIcon(R.drawable.ic_error_black_24dp).setCancelable(true)
                    .setNegativeButton("취소", (dialogInterface, which) -> dialogInterface.cancel())
                    .setPositiveButton("확인", (dialogInterface, which) -> context.startActivity(new Intent(context, LoginActivity.class))).create();

            dialog.show();

            return false;
        }
    }
}
