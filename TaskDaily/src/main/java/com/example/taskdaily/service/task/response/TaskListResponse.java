package com.example.taskdaily.service.task.response;

import com.example.taskdaily.model.enumration.TaskStatus;
import com.example.taskdaily.model.enumration.TaskType;
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
