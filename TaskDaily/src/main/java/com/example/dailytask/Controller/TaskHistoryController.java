package com.example.dailytask.Controller;
import com.example.dailytask.Domain.Enumration.TaskStatus;
import com.example.dailytask.Domain.Enumration.TaskType;
import com.example.dailytask.Domain.TaskHistory;
import com.example.dailytask.Service.Task.Request.TaskEditRequest;
import com.example.dailytask.Service.Task.Request.TaskSaveRequest;
import com.example.dailytask.Service.Task.Response.TaskListResponse;
import com.example.dailytask.Service.taskHistory.TaskHistoryService;
import com.example.dailytask.Service.Task.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.YearMonth;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/history")
@AllArgsConstructor
public class TaskHistoryController {
    private final TaskHistoryService taskHistoryService;
    private final TaskService taskService;


    @GetMapping
    public ModelAndView showHistoryTasks(@RequestParam(required = false) String message) {
        ModelAndView view = new ModelAndView("task/history");
        view.addObject("historytasks", taskHistoryService.getHistoryTasks());
        view.addObject("statuses", TaskStatus.values());
        view.addObject("message", message);
        return view;
    }

    @GetMapping("/search")
    public ModelAndView searchTasks(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                    @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<TaskListResponse> tasks = taskHistoryService.findByStartBetween(startDate.atStartOfDay(), endDate.atStartOfDay());
        ModelAndView view = new ModelAndView("task/history");
        view.addObject("historytasks", tasks);
        view.addObject("statuses", TaskStatus.values());
        view.addObject("message", "Found " + tasks.size() + " tasks!");
        return view;
    }
//    @GetMapping("/create")
//    public ModelAndView showCreate() {
//        ModelAndView view = new ModelAndView("task/create");
//        view.addObject("task", new TaskSaveRequest());
//        view.addObject("taskTypes", TaskType.values());
//        view.addObject("taskStatuses", TaskStatus.values());
//        return view;
//    }

    @PostMapping("/create")
    public ModelAndView showCreate(@ModelAttribute TaskSaveRequest task) {
        ModelAndView view = new ModelAndView("task/create");
        taskService.create(task);
        view.addObject("message", "Created");
        view.addObject("task", new TaskSaveRequest());
        view.addObject("taskTypes", TaskType.values());
        view.addObject("taskStatuses", TaskStatus.values());
        return view;
    }
    @GetMapping("/{id}/{status}")
    public String changeStatus(@PathVariable Long id, @PathVariable TaskStatus status){
        taskHistoryService.changeStatus(id, status);
        return "redirect:/history?message=Change Success";
    }

//    @GetMapping("delete")
//    public String delete(@RequestParam("id") Long id){
//        taskHistoryService.deleteByID(id);
//        return "redirect:/history?message=Deleted";
//    }
    @GetMapping("/delete")
    public ModelAndView delete(@RequestParam("id") Long id){
    taskHistoryService.findById(id);
    taskHistoryService.deleteByID(id);
    return new ModelAndView("redirect:/history?message=Deleted successfully");
    }

    @GetMapping("/edit")
    public ModelAndView showEdit(@RequestParam("id") Long id){
        ModelAndView view = new ModelAndView("/task/edit");
        view.addObject("task", taskHistoryService.showEditById(id));
        view.addObject("taskTypes", TaskType.values());
        return view;
    }

    @PostMapping("/edit/{id}")
    public ModelAndView editTask(@ModelAttribute TaskEditRequest task, @PathVariable Long id){
        ModelAndView view = new ModelAndView("/task/edit");
        try{
            taskHistoryService.edit(task, id);
            view.setViewName("redirect:/history");
        }catch (Exception e){
            view.setViewName("/task/edit");
            view.addObject("task", task);
            view.addObject("taskTypes", TaskType.values());
            view.addObject("errorMessage", "An error occurred while editing the task.");
            return view;
        }
        view.addObject("task", task);
        view.addObject("taskTypes", TaskType.values());
        return new ModelAndView("redirect:/history");
    }

//    @GetMapping("/task-history/day")
//    public String viewTaskHistoryByDay(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, Model model) {
//        List<TaskHistory> taskHistories = taskHistoryService.getTaskHistoriesByDay(date);
//        model.addAttribute("taskHistories", taskHistories);
//        return "task-history";
//    }
//
//    @GetMapping("/task-history/week")
//    public String viewTaskHistoryByWeek(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, Model model) {
//        List<TaskHistory> taskHistories = taskHistoryService.getTaskHistoriesByWeek(date);
//        model.addAttribute("taskHistories", taskHistories);
//        return "task-history";
//    }
//
//    @GetMapping("/task-history/month")
//    public String viewTaskHistoryByMonth(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, Model model) {
//        List<TaskHistory> taskHistories = taskHistoryService.getTaskHistoriesByMonth(date);
//        model.addAttribute("taskHistories", taskHistories);
//        return "task-history";
//    }

    @GetMapping("/date")
    public ModelAndView showListDate(@RequestParam(name = "localDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate localDate) {
        ModelAndView view = new ModelAndView("/history/list");
        view.addObject("view", taskHistoryService.getListDate(localDate));
        return view;
    }
    @GetMapping("/thongke")
    public ModelAndView show(
            @RequestParam(value = "inputDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inputDate,
            @RequestParam(value = "inputWeek", required = false) String inputWeek,
            @RequestParam(value = "inputMonth", required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth inputMonth) {
        ModelAndView view = new ModelAndView("/history/thongke");
        if (inputDate == null && inputWeek == null && inputMonth == null) {
            LocalDate currentDate = LocalDate.now();
            inputDate = currentDate;
            inputWeek = currentDate.getYear() + "-W" + currentDate.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
            inputMonth = YearMonth.from(currentDate);
        }
        List<Object[]> statusCountsDay = taskHistoryService.getStatusCounts(inputDate);
        Map<String, Long> statusTotalsDay = taskHistoryService.calculateStatusTotals(statusCountsDay);
        view.addObject("statusTotalsDay", statusTotalsDay);
        view.addObject("inputDate", inputDate);
        String[] parts = inputWeek.split("-W");
        int year = Integer.parseInt(parts[0]);
        int weekNumber = Integer.parseInt(parts[1]);
        LocalDate startOfWeek = LocalDate.ofYearDay(year, 1)
                .with(TemporalAdjusters.firstDayOfYear())
                .plus(Period.ofWeeks(weekNumber))
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        List<Object[]> statusCountsWeek = taskHistoryService.getStatusCountsByWeek(startOfWeek, endOfWeek);
        Map<String, Long> statusTotalsWeek = taskHistoryService.calculateStatusTotals(statusCountsWeek);
        view.addObject("statusTotalsWeek", statusTotalsWeek);
        view.addObject("inputWeek", inputWeek);
        LocalDate startOfMonth = inputMonth.atDay(1);
        LocalDate endOfMonth = inputMonth.atEndOfMonth();
        List<Object[]> statusCountsMonth = taskHistoryService.getStatusCountsByWeek(startOfMonth, endOfMonth);
        Map<String, Long> statusTotalsMonth = taskHistoryService.calculateStatusTotals(statusCountsMonth);
        view.addObject("statusTotalsMonth", statusTotalsMonth);
        view.addObject("inputMonth", inputMonth);
        return view;
    }

    @GetMapping("/thongke-day")
    public ModelAndView showStatusTotals(@RequestParam("inputDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inputDate,
                                         @RequestParam(value = "inputWeek", required = false) String inputWeek,
                                         @RequestParam(value = "inputMonth", required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth inputMonth) {
        ModelAndView view = new ModelAndView("/history/thongke");
        if (inputWeek == null && inputMonth == null) {
            LocalDate currentDate = LocalDate.now();
//            inputDate = currentDate;
            inputWeek = currentDate.getYear() + "-W" + currentDate.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
            inputMonth = YearMonth.from(currentDate);
        }
        List<Object[]> statusCounts = taskHistoryService.getStatusCounts(inputDate);
        Map<String, Long> statusTotals = taskHistoryService.calculateStatusTotals(statusCounts);
        view.addObject("statusTotalsDay", statusTotals);
        view.addObject("inputDate", inputDate);
        String[] parts = inputWeek.split("-W");
        int year = Integer.parseInt(parts[0]);
        int weekNumber = Integer.parseInt(parts[1]);
        LocalDate startOfWeek = LocalDate.ofYearDay(year, 1)
                .with(TemporalAdjusters.firstDayOfYear())
                .plus(Period.ofWeeks(weekNumber))
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        List<Object[]> statusCountsWeek = taskHistoryService.getStatusCountsByWeek(startOfWeek, endOfWeek);
        Map<String, Long> statusTotalsWeek = taskHistoryService.calculateStatusTotals(statusCountsWeek);
        view.addObject("statusTotalsWeek", statusTotalsWeek);
        view.addObject("inputWeek", inputWeek);
        LocalDate startOfMonth = inputMonth.atDay(1);
        LocalDate endOfMonth = inputMonth.atEndOfMonth();
        List<Object[]> statusCountsMonth = taskHistoryService.getStatusCountsByWeek(startOfMonth, endOfMonth);
        Map<String, Long> statusTotalsMonth = taskHistoryService.calculateStatusTotals(statusCountsMonth);
        view.addObject("statusTotalsMonth", statusTotalsMonth);
        view.addObject("inputMonth", inputMonth);
        return view;
    }
    @GetMapping("/thongke-week")
    public ModelAndView showStatusTotalsByWeek(
            @RequestParam(value = "inputWeek", required = false) String inputWeek,
            @RequestParam(value = "inputDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inputDate,
            @RequestParam(value = "inputMonth", required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth inputMonth) {
        ModelAndView view = new ModelAndView("/history/thongke");
        if (inputDate == null &&  inputMonth == null) {
            LocalDate currentDate = LocalDate.now();
            inputDate = currentDate;
            inputMonth = YearMonth.from(currentDate);
        }
        String[] parts = inputWeek.split("-W");
        int year = Integer.parseInt(parts[0]);
        int weekNumber = Integer.parseInt(parts[1]);
        LocalDate startOfWeek = LocalDate.ofYearDay(year, 1)
                .with(TemporalAdjusters.firstDayOfYear())
                .plus(Period.ofWeeks(weekNumber))
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        List<Object[]> statusCounts = taskHistoryService.getStatusCountsByWeek(startOfWeek, endOfWeek);
        Map<String, Long> statusTotals = taskHistoryService.calculateStatusTotals(statusCounts);
        view.addObject("statusTotalsWeek", statusTotals);
        view.addObject("inputWeek", inputWeek);
        List<Object[]> statusCountsDay = taskHistoryService.getStatusCounts(inputDate);
        Map<String, Long> statusTotalsDay = taskHistoryService.calculateStatusTotals(statusCountsDay);
        view.addObject("statusTotalsDay", statusTotalsDay);
        view.addObject("inputDate", inputDate);
        List<Object[]> statusCountsWeek = taskHistoryService.getStatusCountsByWeek(startOfWeek, endOfWeek);
        Map<String, Long> statusTotalsWeek = taskHistoryService.calculateStatusTotals(statusCountsWeek);
        view.addObject("statusTotalsWeek", statusTotalsWeek);
        view.addObject("inputWeek", inputWeek);
        LocalDate startOfMonth = inputMonth.atDay(1);
        LocalDate endOfMonth = inputMonth.atEndOfMonth();
        List<Object[]> statusCountsMonth = taskHistoryService.getStatusCountsByWeek(startOfMonth, endOfMonth);
        Map<String, Long> statusTotalsMonth = taskHistoryService.calculateStatusTotals(statusCountsMonth);
        view.addObject("statusTotalsMonth", statusTotalsMonth);
        view.addObject("inputMonth", inputMonth);
        return view;
    }

    @GetMapping("/thongke-month")
    public ModelAndView showStatusTotalsByMonth(
            @RequestParam("inputMonth") @DateTimeFormat(pattern = "yyyy-MM") YearMonth inputMonth,
            @RequestParam(value = "inputWeek", required = false) String inputWeek,
            @RequestParam(value = "inputDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inputDate
    ) {
        ModelAndView view = new ModelAndView("history/thongke");
        if (inputDate == null && inputWeek == null ) {
            LocalDate currentDate = LocalDate.now();
            inputDate = currentDate;
            inputWeek = currentDate.getYear() + "-W" + currentDate.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        }
        LocalDate startOfMonth = inputMonth.atDay(1);
        LocalDate endOfMonth = inputMonth.atEndOfMonth();
        List<Object[]> statusCountsMonth = taskHistoryService.getStatusCountsByWeek(startOfMonth, endOfMonth);
        Map<String, Long> statusTotalsMonth = taskHistoryService.calculateStatusTotals(statusCountsMonth);
        view.addObject("statusTotalsMonth", statusTotalsMonth);
        view.addObject("inputMonth", inputMonth);
        List<Object[]> statusCounts = taskHistoryService.getStatusCounts(inputDate);
        Map<String, Long> statusTotals = taskHistoryService.calculateStatusTotals(statusCounts);
        view.addObject("statusTotalsDay", statusTotals);
        view.addObject("inputDate", inputDate);
        String[] parts = inputWeek.split("-W");
        int year = Integer.parseInt(parts[0]);
        int weekNumber = Integer.parseInt(parts[1]);
        LocalDate startOfWeek = LocalDate.ofYearDay(year, 1)
                .with(TemporalAdjusters.firstDayOfYear())
                .plus(Period.ofWeeks(weekNumber))
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        List<Object[]> statusCountsWeek = taskHistoryService.getStatusCountsByWeek(startOfWeek, endOfWeek);
        Map<String, Long> statusTotalsWeek = taskHistoryService.calculateStatusTotals(statusCountsWeek);
        view.addObject("statusTotalsWeek", statusTotalsWeek);
        view.addObject("inputWeek", inputWeek);
        return view;
    }


}
