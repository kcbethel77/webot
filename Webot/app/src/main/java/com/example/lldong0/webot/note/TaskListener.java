package com.example.lldong0.webot.note;


import com.example.lldong0.webot.model.Task;

import java.util.List;

public interface TaskListener {
    void taskListener(List<Task> tasksList, List<String> tasksUidList);
}
