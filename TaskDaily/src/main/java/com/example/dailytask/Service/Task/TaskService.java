package com.example.dailytask.Service.Task;
import com.example.dailytask.Domain.Enumration.TaskStatus;
import com.example.dailytask.Domain.Enumration.TaskType;
import com.example.dailytask.Domain.Task;
import com.example.dailytask.Domain.TaskHistory;
import com.example.dailytask.Exception.ResourceNotFoundException;
import com.example.dailytask.Exception.TaskNotFoundException;
import com.example.dailytask.Repository.TaskHistoryRepository;
import com.example.dailytask.Repository.TaskRepository;
import com.example.dailytask.Service.Task.Request.TaskEditRequest;
import com.example.dailytask.Service.Task.Request.TaskSaveRequest;
import com.example.dailytask.Service.Task.Response.TaskListResponse;
import com.example.dailytask.Service.taskHistory.TaskHistoryService;
import com.example.dailytask.Util.AppMessage;
import com.example.dailytask.Util.AppUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskHistoryService taskHistoryService;
    private final TaskHistoryRepository taskHistoryRepository;

    public List<TaskListResponse> getTasks() {
        return taskRepository.findAll()
                .stream()
                .map(e -> AppUtil.mapper.map(e, TaskListResponse.class))
                .collect(Collectors.toList());

    }

    public List<TaskListResponse> findAll() {
        return taskRepository.findAll().stream().map(task -> {
            return AppUtil.mapper.map(task, TaskListResponse.class);
        }).toList();
    }


    public void create(TaskSaveRequest request){
        var taskHistory = AppUtil.mapper.map(request, TaskHistory.class);
        if (Objects.equals(request.getType(), TaskType.DAILY.toString())){
            var task = AppUtil.mapper.map(request, Task.class);
            task = taskRepository.save(task);

            LocalDate now = LocalDateTime.now().toLocalDate();
            taskHistory.setTask(task);
            taskHistory.setStart(LocalDateTime.of(now, task.getStart()));
            taskHistory.setEnd(LocalDateTime.of(now, task.getEnd()));
        }
        taskHistoryRepository.save(taskHistory);
    }

    public Task findById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Id not found"));
    }

    public TaskHistory findByID(Long id){
        return taskHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(AppMessage.ID_NOT_FOUND, "Task", id)));
    }


    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }
    public Task updateTask(Long taskId, Task updatedTask) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);

        if (taskOptional.isPresent()) {
            Task existingTask = taskOptional.get();
            existingTask.setTitle(updatedTask.getTitle());
            existingTask.setDescription(updatedTask.getDescription());
            existingTask.setStart(updatedTask.getStart());
            existingTask.setEnd(updatedTask.getEnd());

            return taskRepository.save(existingTask);

        } else {
            return null;
        }
    }
    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElse(null);

    }

    public TaskEditRequest showEditByIdTask(Long id){
        Task task = getTaskById(id);
        return taskToTaskEdit(task);
    }
    public void edit(TaskEditRequest request, Long id) throws Exception{
        var taskInDb = getTaskById(id);
        taskInDb.setStart(AppUtil.mapper.map(request.getStart(), LocalTime.class));
        taskInDb.setEnd(AppUtil.mapper.map(request.getEnd(), LocalTime.class));
        taskInDb.setType(TaskType.valueOf(request.getType()));
        taskInDb.setTitle(request.getTitle());
        taskInDb.setDescription(request.getDescription());
        request.setId(id.toString());
        taskRepository.save(taskInDb);
    }
    private TaskEditRequest taskToTaskEdit(Task task){
        var result = new TaskEditRequest();
        result.setTitle(String.valueOf(task.getTitle()));
        result.setDescription(String.valueOf(task.getDescription()));
        result.setStart(String.valueOf(task.getStart()));
        result.setEnd(String.valueOf(task.getEnd()));
        result.setType(String.valueOf(task.getType()));
        result.setId(String.valueOf(task.getId()));
        return result;

    }

    private String parseTime(String timeStr) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("HH:mm");

        LocalTime localTime = LocalTime.parse(timeStr, inputFormatter);
        String formattedTime = localTime.format(outputFormatter);

        return formattedTime; // Kết quả: "09:00"

    }
}
