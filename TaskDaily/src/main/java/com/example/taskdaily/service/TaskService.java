package com.example.taskdaily.service;

import com.example.taskdaily.model.Task;
import com.example.taskdaily.model.TaskHistory;
import com.example.taskdaily.model.enumration.TaskStatus;
import com.example.taskdaily.model.enumration.TaskType;
import com.example.taskdaily.repository.TaskHistoryRepository;
import com.example.taskdaily.repository.TaskRepository;
import com.example.taskdaily.service.task.request.TaskSaveRequest;
import com.example.taskdaily.service.task.response.TaskListResponse;
import com.example.taskdaily.until.AppUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskService {
//    private final static List<Task> tasks;

    private final TaskRepository taskRepository;
    private final TaskHistoryService taskHistoryService;
    private final TaskHistoryRepository taskHistoryRepository;




//    static {
//        tasks = new ArrayList<>();
//        var task1 = Task.builder().id(1L).title("Hoc Spring Boot")
//                .description("bbbbbbbbb")
//                .start(LocalTime.now())
//                .end(LocalTime.now().plusHours(2L)).build();
////                .type(TaskType.DAILY)
////                .status(TaskStatus.IN_PROGRESS).build();
//        var task2 = Task.builder().id(2L).title("Hoc Ky nang viet CV")
//                .description("aaaaaaaaa")
//                .start(LocalTime.now())
//                .end(LocalTime.now().plusHours(2L)).build();
////                .type(TaskType.DAILY)
////                .status(TaskStatus.IN_PROGRESS).build();
//        tasks.add(task1);
//        tasks.add(task2);
//
//    }
    public List<TaskListResponse> getTasks() {

        return taskHistoryRepository.findDemo()
                .stream()
                .map(e -> AppUtil.mapper.map(e, TaskListResponse.class))
                .collect(Collectors.toList());
    }
    public void create(TaskSaveRequest request) {
        var taskHistory = AppUtil.mapper.map(request, TaskHistory.class);
        if (Objects.equals(request.getType(), TaskType.DAILY.toString())){
            var task = AppUtil.mapper.map(request, Task.class);
            task = taskRepository.save(task);
            
            //tạo lịch sử tác vụ mới để hiển thị trên màn hình chính

            LocalDate now = LocalDateTime.now().toLocalDate();
            taskHistory.setTask(task);
            taskHistory.setStart(LocalDateTime.of(now, task.getStart()));
            taskHistory.setEnd(LocalDateTime.of(now, task.getEnd()));
        }
        taskHistoryRepository.save(taskHistory);
    }

//    public List<Task> getTasks() {
//        return tasks;
//    }

//    public void createTask(Task task) {
//        task.setId(getNextTaskId());
//        tasks.add(task);
//    }

//    private Long getNextTaskId() {  // Hàm này để tìm id tiếp theo cho task mới
//        if (tasks.isEmpty()) {
//            return 1L;
//        }
//        Task lastTask = tasks.get(tasks.size() - 1);
//        return lastTask.getId() + 1;
//    }
//    public void editTask(Task editedTask) {
//        tasks.stream()
//                .filter(task -> task.getId().equals(editedTask.getId()))
//                .findFirst()
//                .ifPresent(task -> {
//                    task.setTitle(editedTask.getTitle());
//                    task.setDescription(editedTask.getDescription());
//                    task.setStart(editedTask.getStart());
//                    task.setEnd(editedTask.getEnd());
////                    task.setType(editedTask.getType());
////                    task.setStatus(editedTask.getStatus());
//                });
//    }
//    public Task getTaskById(Long taskId) {
//        return tasks.stream()
//                .filter(task -> task.getId().equals(taskId))
//                .findFirst()
//                .orElse(null);
//    }
//    public void deleteTask(Long taskId) {
//        tasks.removeIf(task -> task.getId().equals(taskId));
//    }
    public void updateTaskStatus(Long id, TaskStatus status) {
        TaskHistory taskHistory = taskHistoryService.getTaskHistoryById(id);
        if (taskHistory != null) {
            taskHistory.setStatus(status);
            taskHistoryService.save(taskHistory);

        }
    }
    public void createTaskHistory(Task task, TaskStatus oldStatus, TaskStatus newStatus) {
        TaskHistory taskHistory = new TaskHistory();
        taskHistory.setTask(task);
        taskHistory.setStatus(oldStatus);
        taskHistory.setStatus(newStatus);
        taskHistory.setType(TaskType.DAILY);
        // Thực hiện lưu bản ghi TaskHistory vào cơ sở dữ liệu
        taskHistoryService.save(taskHistory);
    }
//    public void updateTaskStatus(Long id, TaskStatus status) {
//        Task taskToUpdate = getTaskById(id);
//        if (taskToUpdate != null) {
//            taskToUpdate.setStatus(status);
//        }
//    }



}
