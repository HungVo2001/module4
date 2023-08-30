package com.example.dailytask.Service.Task;
import com.example.dailytask.Domain.Enumration.TaskStatus;
import com.example.dailytask.Domain.Enumration.TaskType;
import com.example.dailytask.Domain.Task;
import com.example.dailytask.Domain.TaskHistory;
import com.example.dailytask.Repository.TaskHistoryRepository;
import com.example.dailytask.Repository.TaskRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskRenewalService {
    private final TaskRepository taskRepository;
    private final TaskHistoryRepository taskHistoryRepository;
    @Transactional
    @Scheduled(cron = "0 0 10 * * ?")
    public void renewTasks() {
        System.out.println("Renewal task started.");
        List<TaskHistory> renewedTasks = taskRepository.findAll().stream()
                .map(this::createTaskHistoryFromTask)
                .collect(Collectors.toList());

        taskHistoryRepository.saveAll(renewedTasks);

    }

//    @Transactional
//    @EventListener(ContextRefreshedEvent.class)
//    public void contextRefreshedEvent() {
//        renewTasks();
//    }


    private TaskHistory createTaskHistoryFromTask(Task task) {
        LocalDate currentDate = LocalDate.now();
        return TaskHistory.builder()
                .title(task.getTitle())
                .description(task.getDescription())
                .start(currentDate.atTime(task.getStart()))
                .end(currentDate.atTime(task.getEnd()))
                .status(TaskStatus.TODO)
                .type(TaskType.DAILY)
                .task(task)
                .created(LocalDate.now())
                .build();
    }
}
