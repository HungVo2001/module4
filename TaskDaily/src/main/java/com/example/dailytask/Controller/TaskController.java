package com.example.dailytask.Controller;
import com.example.dailytask.Domain.Enumration.TaskStatus;
import com.example.dailytask.Domain.Enumration.TaskType;
import com.example.dailytask.Domain.Task;
import com.example.dailytask.Service.Task.Request.TaskEditRequest;
import com.example.dailytask.Service.Task.Request.TaskSaveRequest;
import com.example.dailytask.Service.Task.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/task")
@AllArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public ModelAndView showListTasks() {
        ModelAndView view = new ModelAndView("task/index");
        view.addObject("tasks", taskService.getTasks());
        return view;
    }

    @GetMapping("/create")
    public ModelAndView showCreate() {
        ModelAndView view = new ModelAndView("task/create");
        view.addObject("task", new Task());
        view.addObject("taskTypes", TaskType.values());
        view.addObject("taskStatuses", TaskStatus.values());
        return view;
    }

    @PostMapping("/create")
    public String create (@ModelAttribute TaskSaveRequest task) {
        taskService.create(task);
        return "redirect:/task";
    }

    @GetMapping("/editTask/{id}")
    public ModelAndView showEditTaskDaily(@PathVariable Long id){

        ModelAndView view = new ModelAndView("task/editTask");
        view.addObject("task", taskService.showEditByIdTask(id));
        view.addObject("taskTypes", TaskType.values());
//        view.addObject("taskStatuses", TaskStatus.values());
        return view;

    }

    @PostMapping("/editTask/{id}")
    public ModelAndView editTaskDaily(@ModelAttribute TaskEditRequest task, @PathVariable Long id){
        ModelAndView view = new ModelAndView("/task/editTask");
        try{
            taskService.edit(task, id);
            view.setViewName("redirect:/task");
        }catch (Exception e){
            view.setViewName("/task/editTask");
            view.addObject("task", task);
            view.addObject("taskTypes", TaskType.values());
            view.addObject("errorMessage", "An error occurred while editing the task.");
            return view;
        }
        view.addObject("task", task);
        view.addObject("taskTypes", TaskType.values());
        return new ModelAndView("redirect:/task");
    }



//    @GetMapping("/delete/{id}")
//    public ModelAndView delete(@PathVariable Long id){
//        taskService.findById(id);
//        taskService.deleteById(id);
//        ModelAndView view = new ModelAndView("task/index");
//        view.addObject("message", "Delete successfully");
//        view.addObject("tasks", taskService.getTasks());
//        return view;
//    }
    @GetMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable Long id){
    taskService.findById(id);
    taskService.deleteById(id);
    ModelAndView view = new ModelAndView("task/index");
    view.addObject("taskTypes", TaskType.values());
    view.addObject("message", "Deleted successfully");
    view.addObject("tasks", taskService.getTasks());
    return view;
    }

}
