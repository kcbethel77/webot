package com.example.lldong0.webot.note;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lldong0.webot.R;
import com.example.lldong0.webot.helper.CreateDialogHelper;
import com.example.lldong0.webot.model.Task;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private List<String> taskUidList;

    public TaskAdapter(List<Task> taskList, List<String> taskUidList) {
        this.taskList = taskList;
        this.taskUidList = taskUidList;
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    @NonNull
    @Override
    public TaskAdapter.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskAdapter.TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task newHomework = taskList.get(position);
        String homeworkUid = taskUidList.get(position);
        holder.setData(newHomework, homeworkUid);
    }


    public class TaskViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.homework_cognitive_error)
        TextView hwCognitiveError;
        @BindView(R.id.homework_date)
        TextView hwDate;
        @BindView(R.id.homework_automatic_thought)
        TextView hwAutoThought;
        @BindView(R.id.homework_assignment)
        TextView hwAssignment;
        @BindView(R.id.homework_finished_button)
        CheckBox homework_finished_button;
        @BindView(R.id.remove_button)
        Button removeButton;
        @BindView(R.id.homework_form)
        LinearLayout homework_form;
        @BindView(R.id.homework_uid)
        TextView homework_uid;

        private TaskViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        private void setData(Task newHomework, String uid) {
            hwCognitiveError.setText(newHomework.getCognitiveError());
            hwDate.setText(newHomework.getWrittenTime());
            hwAutoThought.setText(newHomework.getAutomaticThought());
            hwAssignment.setText(newHomework.getAssignment());
            homework_uid.setText(uid);

            if (newHomework.getChecked()) {
                homework_finished_button.setChecked(true);
                visRemoveButton();
            } else {
                homework_finished_button.setChecked(false);
                noRemoveButton();
            }

        }

        @OnCheckedChanged(R.id.homework_finished_button)
        public void isChecking(boolean isChecked) {
            CheckingTask checkingHomework = new CheckingTask();
            if (isChecked) {
                visRemoveButton();

                checkingHomework.setChecking(homework_uid.getText().toString(), true);
            } else {
                noRemoveButton();
                checkingHomework.setChecking(homework_uid.getText().toString(), false);
            }
        }

        private void visRemoveButton() {
            homework_form.setAlpha((float) 0.5);
            removeButton.setVisibility(View.VISIBLE);
        }

        @OnClick(R.id.remove_button)
        public void toRemove(View v) {
            CreateDialogHelper dialog = new CreateDialogHelper(homework_uid.getText().toString());
            dialog.showDialog(v.getContext());
        }

        private void noRemoveButton() {
            homework_form.setAlpha((float) 1.0);
            removeButton.setVisibility(View.INVISIBLE);
        }
    }
}
