package com.example.dailytask.Service.Task.Response;
import com.example.dailytask.Domain.Enumration.TaskStatus;
import com.example.dailytask.Domain.Enumration.TaskType;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class TaskListResponse {
    private Long id;

    private String title;

    private String description;

    private LocalTime start;

    private LocalTime end;

    private TaskStatus status;

    private TaskType type;


    public String getTime(){
        return start.toString() + " - " + end;
    }

}
