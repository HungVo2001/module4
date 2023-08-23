package com.example.demospring.Controller;

import com.example.demospring.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value = "/api")
public class ProductController {
    private final UserService userService;

    public ProductController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{message}")
    @ResponseStatus(HttpStatus.CREATED)
    public ModelAndView helloWord(@PathVariable String message){
        ModelAndView view = new ModelAndView("index");
        view.addObject("message", message);
        return view;
    }
    @PostMapping("/hello")
    public String demo(){
        return "hello";
    }
}
