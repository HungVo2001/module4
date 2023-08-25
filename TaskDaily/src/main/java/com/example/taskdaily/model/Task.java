package com.example.taskdaily.model;

import com.example.taskdaily.model.enumration.TaskStatus;
import com.example.taskdaily.model.enumration.TaskType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
@Entity
@Table(name ="tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Id và @Gene: dùng để create table trên database gồm những field ở dưới
    private Long id;

    private String title;

    private String description;

    private LocalTime start;

    private LocalTime end;

//    private LocalDateTime start;
//
//    private LocalDateTime end;
//
//    private TaskStatus status;
//
//    private TaskType type;
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public LocalDateTime getStart(){
//        return start;
//    }
//    public LocalDateTime getEnd(){
//        return end;
//    }
//
//    public void setStart(LocalDateTime start) {
//        this.start = start;
//    }
//
//
//
//    public void setEnd(LocalDateTime end) {
//        this.end = end;
//    }
//
//    public TaskStatus getStatus() {
//        return status;
//    }
//
//    public void setStatus(TaskStatus status) {
//        this.status = status;
//    }
//
//    public TaskType getType() {
//        return type;
//    }
//
//    public void setType(TaskType type) {
//        this.type = type;
//    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // để xác định được OneToMany hay ManyToOne thì phải câu hỏi Task History thằng 1 nào thằng nhiều
    // chữ đầu tiên đại diện cho class đang đứng
    // chữ thứ 2 đại diện cho field bên dưới
    @OneToMany(mappedBy = "task") // task ở đây được lấy name field task của TaskHistory
    private Set<TaskHistory> taskHistories;

}
