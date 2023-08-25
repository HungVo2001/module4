package com.example.taskdaily.controller;

import com.example.taskdaily.model.Task;
import com.example.taskdaily.model.enumration.TaskStatus;
import com.example.taskdaily.model.enumration.TaskType;
import com.example.taskdaily.service.task.TaskService;
//import com.example.taskdaily.service.task.request.TaskEditRequest;
import com.example.taskdaily.service.task.request.TaskSaveRequest;
import com.example.taskdaily.service.task.response.TaskListResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(value = "/task")
@AllArgsConstructor
public class TaskController {
    private final TaskService taskService;


    @GetMapping
    public ModelAndView showListTasks(@RequestParam(required = false) String message) {
        ModelAndView view = new ModelAndView("/task/index");
        view.addObject("tasks", taskService.getTasks());
        view.addObject("message", message);
        view.addObject("statuses", TaskStatus.values());
        return view;
    }

    @GetMapping("/create")
    public ModelAndView showCreate() {
        ModelAndView view = new ModelAndView("/task/create");
        view.addObject("task", new TaskSaveRequest());
        view.addObject("taskTypes", TaskType.values());
        view.addObject("taskStatuses", TaskStatus.values());
        return view;
    }

    @PostMapping("/create")
    public ModelAndView showCreate(@ModelAttribute TaskSaveRequest task) {
        ModelAndView view = new ModelAndView("/task/create");
        taskService.create(task);
        view.addObject("message", "Created");
        view.addObject("task", new TaskSaveRequest());
        view.addObject("taskTypes", TaskType.values());
        view.addObject("taskStatuses", TaskStatus.values());
        return new ModelAndView("redirect:/task");

    }
    @GetMapping("/{id}/{status}")
    public String changeStatus(@PathVariable Long id, @PathVariable TaskStatus status){
        taskService.changeStatus(id, status);
        return "redirect:/task?message=Change Success";
    }

    //    @GetMapping("/edit/{id}")
//    public ModelAndView showEdit(@PathVariable Long id){
//        ModelAndView view = new ModelAndView("/task/edit");
////        Task taskToEdit = taskService.getTaskById(id);
//        view.addObject("task", taskService.showEditById(id));
//        view.addObject("taskTypes", TaskType.values());
////        view.addObject("taskStatuses", TaskStatus.values());
//        return view;
//    }
//
//    @PostMapping("/edit/{id}")
//    public ModelAndView editTask(@ModelAttribute TaskEditRequest task, @PathVariable Long id){
//        ModelAndView view = new ModelAndView("/task/edit");
//        try{
//            taskService.edit(task, id);
//        }catch (Exception e){
//            view.addObject("task", task);
//            view.addObject("taskTypes", TaskType.values());
//            return view;
//        }
//        view.addObject("task", task);
//        view.addObject("taskTypes", TaskType.values());
//        return new ModelAndView("redirect:/task");
//
//    }
//    @GetMapping("/delete/{id}")
//    public ModelAndView delete(@PathVariable Long id){
//        ModelAndView view = new ModelAndView("/task/delete");
//        Task taskToDelete = taskService.getTaskById(id);
//        view.addObject("task", taskToDelete);
//        view.addObject("taskTypes", TaskType.values());
//        view.addObject("taskStatuses", TaskStatus.values());
//        return view;
//    }
//
//    @PostMapping("/delete")
//    public ModelAndView deleteTask(@ModelAttribute Task task){
//        taskService.deleteTask(task.getId());
//        return new ModelAndView("redirect:/task");
//    }
//
    @GetMapping("/home")
    public ModelAndView homeTask() {
        ModelAndView view = new ModelAndView("/task/home");
        List<TaskListResponse> allTasks = taskService.getTasks();
        view.addObject("tasks", allTasks);
        view.addObject("taskStatuses", TaskStatus.values());
        return view;
    }

    @PostMapping("/update-status/{id}")
    public ModelAndView updateStatus(@PathVariable Long id, @RequestParam TaskStatus status) {
        taskService.updateTaskStatus(id, status);
        return new ModelAndView("redirect:/task/home");
    }

    @PostMapping("/create-task")
    public ModelAndView createTaskHome(@ModelAttribute Task task, TaskStatus oldStatus, TaskStatus newStatus) {
        ModelAndView view = new ModelAndView("/task/create");
        view.addObject("task", new Task());
        view.addObject("taskStatuses", TaskStatus.values());
        taskService.createTaskHistory(task, oldStatus, newStatus);
        return new ModelAndView("redirect:/task/home");
    }

}
