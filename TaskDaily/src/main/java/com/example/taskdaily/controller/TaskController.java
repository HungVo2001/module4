package com.example.taskdaily.controller;

import com.example.taskdaily.model.Task;
import com.example.taskdaily.model.enumration.TaskStatus;
import com.example.taskdaily.model.enumration.TaskType;
import com.example.taskdaily.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/task")
@AllArgsConstructor
public class TaskController {
    private final TaskService taskService;


    @GetMapping
    public ModelAndView showListTasks(){
        ModelAndView view = new ModelAndView("/task/index");
        view.addObject("tasks", taskService.getTasks());
        return view;
    }

    @GetMapping("/create")
    public ModelAndView showCreate(){
        ModelAndView view = new ModelAndView("/task/create");
        view.addObject("task", new Task());
        view.addObject("taskTypes", TaskType.values());
        view.addObject("taskStatuses", TaskStatus.values());
        return view;
    }

    @PostMapping("/create")
    public ModelAndView showCreate(@ModelAttribute Task task){
        ModelAndView view = new ModelAndView("/task/create");
        view.addObject("task", new Task());
        view.addObject("taskTypes", TaskType.values());
        view.addObject("taskStatuses", TaskStatus.values());
        taskService.createTask(task);
        return new ModelAndView("redirect:/task");

    }
    @GetMapping("/edit/{id}")
    public ModelAndView showEdit(@PathVariable Long id){
        ModelAndView view = new ModelAndView("/task/edit");
        Task taskToEdit = taskService.getTaskById(id);
        view.addObject("task", taskToEdit);
        view.addObject("taskTypes", TaskType.values());
        view.addObject("taskStatuses", TaskStatus.values());
        return view;
    }

    @PostMapping("/edit")
    public ModelAndView editTask(@ModelAttribute Task task){
        ModelAndView view = new ModelAndView("/task/edit");
        view.addObject("taskTypes", TaskType.values());
        view.addObject("taskStatuses", TaskStatus.values());
        taskService.editTask(task);
        return new ModelAndView("redirect:/task");

    }
    @GetMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable Long id){
        ModelAndView view = new ModelAndView("/task/delete");
        Task taskToDelete = taskService.getTaskById(id);
        view.addObject("task", taskToDelete);
        view.addObject("taskTypes", TaskType.values());
        view.addObject("taskStatuses", TaskStatus.values());
        return view;
    }

    @PostMapping("/delete")
    public ModelAndView deleteTask(@ModelAttribute Task task){
        taskService.deleteTask(task.getId());
        return new ModelAndView("redirect:/task");
    }

}
