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
import java.time.LocalTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "task")
@Where(clause = "deleted = 0")
@SQLDelete(sql= "UPDATE task SET `deleted` = 1 WHERE (`id` = ?) ")

public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private LocalTime start;

    private LocalTime end;

    private boolean deleted = false;

    @Enumerated(value = EnumType.STRING)
    private TaskType type;

    private LocalDate renewalDate = LocalDate.now().plusDays(1);

//    private boolean deleted = false;

//    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
//    @JoinColumn(name = "task_id")
//    private TaskHistory taskHistory;
//
//    @OneToMany(mappedBy = "task")
//    private Set<Task> tasks;

//    @OneToMany(mappedBy = "task") // task ở đây được lấy name field task của TaskHistory
//    private Set<TaskHistory> taskHistories;
}
