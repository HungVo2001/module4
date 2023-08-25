package com.example.taskdaily.controller;

import com.example.taskdaily.model.Task;
import com.example.taskdaily.model.enumration.TaskStatus;
import com.example.taskdaily.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(value = "/task")
@AllArgsConstructor
public class HomeController {
    private final TaskService taskService;

//    @GetMapping("/home")
//    public ModelAndView homeTask() {
//        ModelAndView view = new ModelAndView("/task/home");
//        List<Task> allTasks = taskService.getTasks();
//        view.addObject("tasks", allTasks);
//        view.addObject("taskStatuses", TaskStatus.values());
//        return view;
//    }
//
//    @PostMapping("/update-status/{id}")
//    public ModelAndView updateStatus(@PathVariable Long id, @RequestParam TaskStatus status) {
//        taskService.updateTaskStatus(id, status);
//        return new ModelAndView("redirect:/task/home");
//    }
}
