package com.phenjuly.security.webapp.teacher.student.controller;

import com.phenjuly.security.webapp.teacher.student.service.CoursesService;
import com.phenjuly.security.webapp.teacher.student.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author PhenJuly
 */
@RequestMapping("/students")
@Controller
public class StudentController {
    @Autowired
    private StudentService studentService;

    @Autowired
    private CoursesService coursesService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("results", studentService.list());
        return "students";
    }

    @GetMapping(path = "/{name}")
    public String get(@PathVariable("name") String name, Model model) {
        model.addAttribute("result", name);
        return "student";
    }

    @GetMapping(path = "/courses")
    public String listCourses(Model model) {
        model.addAttribute("results", coursesService.list());
        return "courses";
    }

    @GetMapping(path = "/courses/{name}")
    public String getCourses(@PathVariable("name") String name, Model model) {
        model.addAttribute("result", coursesService.get(name));
        return "course";
    }

}
