package com.example.dailytask.Controller;
import com.example.dailytask.Domain.Enumration.TaskStatus;
import com.example.dailytask.Domain.Enumration.TaskType;
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

import java.time.LocalDate;
import java.util.List;

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

}
