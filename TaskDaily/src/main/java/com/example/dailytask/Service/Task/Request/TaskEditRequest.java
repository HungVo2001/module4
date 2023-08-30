package com.example.dailytask.Service.Task.Request;
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
