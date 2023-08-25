package com.example.taskdaily.service.task;


import com.example.taskdaily.model.TaskHistory;
import com.example.taskdaily.repository.TaskHistoryRepository;
import com.example.taskdaily.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TaskHistoryService {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskHistoryRepository taskHistoryRepository;

    public TaskHistory getTaskHistoryById(Long id) {
        return taskHistoryRepository.findById(id).orElse(null);
    }
    public void save(TaskHistory taskHistory) {
        taskHistoryRepository.save(taskHistory);
    }

}
