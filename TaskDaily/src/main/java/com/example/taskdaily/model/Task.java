package com.example.taskdaily.model;

import com.example.taskdaily.model.enumration.TaskStatus;
import com.example.taskdaily.model.enumration.TaskType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {
    private Long id;

    private String title;

    private String description;

    private LocalDateTime start;

    private LocalDateTime end;

    private TaskStatus status;

    private TaskType type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStart(){
        return start;
    }
    public LocalDateTime getEnd(){
        return end;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }



    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }
}
