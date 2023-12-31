package com.example.dailytask.Repository;
import com.example.dailytask.Domain.TaskHistory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TaskHistoryRepository extends JpaRepository<TaskHistory, Long> {
    @Query(value = "SELECT t  FROM TaskHistory t WHERE  " +
            "(DATE_FORMAT(t.start, '%Y-%m-%d') <= DATE_FORMAT(CURRENT_DATE, '%Y-%m-%d') AND " +
            "DATE_FORMAT(t.end, '%Y-%m-%d') >= DATE_FORMAT(CURRENT_DATE, '%Y-%m-%d') AND t.type = 'NONE_DAILY')" +
            "OR (DATE_FORMAT(t.start, '%Y-%m-%d') = DATE_FORMAT(CURRENT_DATE, '%Y-%m-%d') AND t.type = 'DAILY')" )

    List<TaskHistory> findAllTaskToDay();

    @Query("SELECT t FROM TaskHistory t WHERE " +
            "((t.start <= :endDate AND t.end >= :startDate AND t.type = 'NONE_DAILY') " +
            "OR (t.start = :startDate AND t.type = 'DAILY'))")
    List<TaskHistory> findByStartBetween(@Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate);

    @Query("SELECT e.created FROM TaskHistory e ORDER BY e.created DESC limit 1")
    List<LocalDate> findCreatedTask(PageRequest pageRequest);

    @Query("SELECT t.status, COUNT(t.status) as status_count FROM TaskHistory t WHERE FUNCTION('DATE_FORMAT', t.start, '%Y-%m-%d') = FUNCTION('DATE_FORMAT', :startDate, '%Y-%m-%d') GROUP BY t.status")
    List<Object[]> getStatusCountsByStartDate(@Param("startDate") LocalDate startDate);
    @Query("SELECT t.status, COUNT(t.status) as status_count FROM TaskHistory t WHERE FUNCTION('DATE_FORMAT', t.start, '%Y-%m-%d') >= FUNCTION('DATE_FORMAT', :startDate, '%Y-%m-%d') AND FUNCTION('DATE_FORMAT', t.end, '%Y-%m-%d') <= FUNCTION('DATE_FORMAT', :endDate, '%Y-%m-%d') GROUP BY t.status")
    List<Object[]> getStatusCountsByWeek(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT t FROM TaskHistory t WHERE FUNCTION('DATE_FORMAT', t.start, '%Y-%m-%d') = FUNCTION('DATE_FORMAT', :localDate, '%Y-%m-%d')")
    List<TaskHistory> findByStartDateToday(@Param("localDate") LocalDate localDate);
}
