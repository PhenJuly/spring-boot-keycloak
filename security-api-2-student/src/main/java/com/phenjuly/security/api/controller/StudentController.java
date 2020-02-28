package com.phenjuly.security.api.controller;

import com.phenjuly.security.api.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

/**
 * @author PhenJuly
 */
@RequestMapping("/students")
@Controller
public class StudentController {
    @Autowired
    private StudentService studentService;

    @ResponseBody
    @GetMapping
    public List<String> list() {
        return studentService.list();
    }

    @ResponseBody
    @GetMapping(path = "/{name}")
    public String get(@PathVariable("name") String name) {
        Optional<String> first = studentService.list().stream().filter(student -> student.equals(name)).findFirst();
        return first.isPresent() ? first.get() : "";
    }

}
