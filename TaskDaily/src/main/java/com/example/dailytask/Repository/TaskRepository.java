package com.example.dailytask.Repository;

import com.example.dailytask.Domain.Task;
import com.example.dailytask.Domain.TaskHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Task findByIdAndDeleted(Long taskId, boolean deleted);

    List<Task> findByRenewalDate(LocalDate currentDate);

    Task findTopByOrderByRenewalDateAsc();

}
