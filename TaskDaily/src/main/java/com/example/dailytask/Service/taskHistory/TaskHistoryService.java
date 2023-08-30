package com.example.dailytask.Service.taskHistory;
import com.example.dailytask.Domain.Enumration.TaskStatus;
import com.example.dailytask.Domain.Enumration.TaskType;
import com.example.dailytask.Domain.TaskHistory;
import com.example.dailytask.Exception.ResourceNotFoundException;
import com.example.dailytask.Repository.TaskHistoryRepository;
import com.example.dailytask.Repository.TaskRepository;
import com.example.dailytask.Service.Task.Request.TaskEditRequest;
import com.example.dailytask.Service.Task.Response.TaskListResponse;
import com.example.dailytask.Util.AppMessage;
import com.example.dailytask.Util.AppUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskHistoryService {

    private TaskRepository taskRepository;
    private TaskHistoryRepository taskHistoryRepository;

    public List<TaskListResponse> getHistoryTasks() {
        return taskHistoryRepository.findAllTaskToDay() // hoặc sài getTask()
                .stream()
                .map(e -> AppUtil.mapper.map(e, TaskListResponse.class))
                .collect(Collectors.toList());
    }

    public void changeStatus(Long id, TaskStatus status){
        var task = getTaskHistoryById(id);
        task.setStatus(status);
        taskHistoryRepository.save(task);
    }

    public TaskHistory getTaskHistoryById(Long id) {
        return taskHistoryRepository.findById(id).orElse(null);
    }
    public void save(TaskHistory taskHistory) {
        taskHistoryRepository.save(taskHistory);
    }

    public void deleteByID(Long id){
        TaskHistory taskHistory = getTaskHistoryById(id);
        taskHistoryRepository.delete(taskHistory);
    }

    public TaskEditRequest showEditById(Long id){
        TaskHistory taskHistory = getTaskHistoryById(id);
        return taskToTaskEditRequest(taskHistory);
    }
    public void edit(TaskEditRequest request, Long id) throws Exception{
        var taskInDb = getTaskHistoryById(id);

        taskInDb.setStart(AppUtil.mapper.map(request.getStart(), LocalDateTime.class));
        taskInDb.setEnd(AppUtil.mapper.map(request.getEnd(), LocalDateTime.class));
        taskInDb.setType(TaskType.valueOf(request.getType()));
        taskInDb.setTitle(request.getTitle());
        taskInDb.setDescription(request.getDescription());
        request.setId(id.toString());
        taskHistoryRepository.save(taskInDb);
    }

    private TaskEditRequest taskToTaskEditRequest(TaskHistory taskHistory){
        var result = new TaskEditRequest();
        result.setTitle(String.valueOf(taskHistory.getTitle()));
        result.setDescription(String.valueOf(taskHistory.getDescription()));
        result.setStart(String.valueOf(taskHistory.getStart()));
        result.setEnd(String.valueOf(taskHistory.getEnd()));
        result.setType(String.valueOf(taskHistory.getType()));
        result.setId(String.valueOf(taskHistory.getId()));
        return result;
    }
    public TaskHistory findById(Long id) {
        return taskHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException
                        (String.format(AppMessage.ID_NOT_FOUND, "Task", id)));
    }

    public List<TaskListResponse> findByStartBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return  taskHistoryRepository.findByStartBetween(startDate, endDate)
                .stream()
                .map(e -> AppUtil.mapper.map(e, TaskListResponse.class))
                .collect(Collectors.toList());

    }


}
