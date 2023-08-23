package com.example.taskdaily.service;

import com.example.taskdaily.model.Task;
import com.example.taskdaily.model.enumration.TaskStatus;
import com.example.taskdaily.model.enumration.TaskType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaskService {
    private final static List<Task> tasks;



    static {
        tasks = new ArrayList<>();
        var task1 = Task.builder().id(1L).title("Hoc Spring Boot")
                .description("bbbbbbbbb")
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusHours(2L))
                .type(TaskType.DAILY)
                .status(TaskStatus.IN_PROGRESS).build();
        var task2 = Task.builder().id(2L).title("Hoc Ky nang viet CV")
                .description("aaaaaaaaa")
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusHours(2L))
                .type(TaskType.DAILY)
                .status(TaskStatus.IN_PROGRESS).build();
        tasks.add(task1);
        tasks.add(task2);

    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void createTask(Task task) {
        task.setId(getNextTaskId());
        tasks.add(task);
    }

    private Long getNextTaskId() {
        if (tasks.isEmpty()) {
            return 1L;
        }
        Task lastTask = tasks.get(tasks.size() - 1);
        return lastTask.getId() + 1;
    }
    public void editTask(Task editedTask) {
        tasks.stream()
                .filter(task -> task.getId().equals(editedTask.getId()))
                .findFirst()
                .ifPresent(task -> {
                    task.setTitle(editedTask.getTitle());
                    task.setDescription(editedTask.getDescription());
                    task.setStart(editedTask.getStart());
                    task.setEnd(editedTask.getEnd());
                    task.setType(editedTask.getType());
                    task.setStatus(editedTask.getStatus());
                });
    }
    public Task getTaskById(Long taskId) {
        return tasks.stream()
                .filter(task -> task.getId().equals(taskId))
                .findFirst()
                .orElse(null);
    }
    public void deleteTask(Long taskId) {
        tasks.removeIf(task -> task.getId().equals(taskId));
    }
}
