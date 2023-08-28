package com.example.taskdaily.service.task.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TaskEditRequest {
    private String id;
    private String title;

    private String description;

    private String start;

    private String end;

    private String type;
}
