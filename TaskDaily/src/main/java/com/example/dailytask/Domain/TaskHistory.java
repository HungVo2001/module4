package com.example.dailytask.Domain;

import com.example.dailytask.Domain.Enumration.TaskStatus;
import com.example.dailytask.Domain.Enumration.TaskType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "task_history")
@Where(clause = "deleted = 0")
@SQLDelete(sql = "UPDATE task_history SET `deleted` = 1 WHERE (`id` = ?);")
public class TaskHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;
    private LocalDate created;

    private LocalDateTime start;

    private LocalDateTime end;
    private boolean deleted = false;

    @Enumerated(value = EnumType.STRING)
    private TaskStatus status;

    @Enumerated(value = EnumType.STRING)
    private TaskType type;
    private LocalDate renewaDate = LocalDate.now().plusDays(1);

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

//    @PrePersist
//    public void setupBeforeInsert(){
//        status = TaskStatus.TODO;
//    }
    @PrePersist
    public void setupBeforeInsert() {
        created = LocalDate.now();
    }

}
