package com.example.taskdaily.service.task;


import com.example.taskdaily.model.Task;
import com.example.taskdaily.model.TaskHistory;
import com.example.taskdaily.model.enumration.TaskType;
import com.example.taskdaily.repository.TaskHistoryRepository;
import com.example.taskdaily.repository.TaskRepository;
import com.example.taskdaily.service.task.request.TaskSaveRequest;
import com.example.taskdaily.service.task.response.TaskListResponse;
import com.example.taskdaily.until.AppUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskHistoryService {
//    private final TaskRepository taskRepository;
//    private final TaskHistoryService taskHistoryService;
//    private final TaskHistoryRepository taskHistoryRepository;
//
//    public List<TaskListResponse> getTasks() {
//        return taskHistoryRepository.findAllTaskToDay()
//                .stream()
//                .map(e -> AppUtil.mapper.map(e, TaskListResponse.class))
//                .collect(Collectors.toList());
//    }
//
//
//    public void create(TaskSaveRequest request) {
//        var taskHistory = AppUtil.mapper.map(request, TaskHistory.class);
//        if (Objects.equals(request.getType(), TaskType.DAILY.toString())){
//            var task = AppUtil.mapper.map(request, Task.class);
//            task = taskRepository.save(task);
//
//            //tạo lịch sử tác vụ mới để hiển thị trên màn hình chính
//
//            LocalDate now = LocalDateTime.now().toLocalDate();
//            taskHistory.setTask(task);
//            taskHistory.setStart(LocalDateTime.of(now, task.getStart()));
//            taskHistory.setEnd(LocalDateTime.of(now, task.getEnd()));
//        }
//        taskHistoryRepository.save(taskHistory);
//    }

}
