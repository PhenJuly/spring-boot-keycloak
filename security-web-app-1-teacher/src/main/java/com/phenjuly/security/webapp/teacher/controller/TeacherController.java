package com.phenjuly.security.webapp.teacher.controller;

import com.phenjuly.security.webapp.teacher.service.CourseService;
import com.phenjuly.security.webapp.teacher.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

/**
 * @author PhenJuly
 */
@RequestMapping("teachers")
@Controller
public class TeacherController {
    @Autowired
    private TeacherService teacherService;

    @Autowired
    private CourseService courseService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("results", teacherService.list());
        return "teachers";
    }

    @GetMapping(path = "/{name}")
    public String get(@PathVariable("name") String name, Model model) {
        Optional<String> first = teacherService.list().stream().filter(teacher -> teacher.equals(name)).findFirst();
        model.addAttribute("result", first.isPresent() ? first.get() : "");
        return "teacher";
    }

    @GetMapping(path = "/courses")
    public String listCourses(Model model) {
        model.addAttribute("results", courseService.list());
        return "courses";
    }

    @GetMapping(path = "/courses/{name}")
    public String getCourses(@PathVariable("name") String name, Model model) {
        model.addAttribute("result", courseService.get(name));
        return "course";
    }


}
