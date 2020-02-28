package com.phenjuly.security.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;


/**
 * @author PhenJuly
 */

@Slf4j
@RequestMapping("/courses")
@Controller
public class CourseController {

    @ResponseBody
    @GetMapping
    public Map<String, List<String>> list() {
        Map<String, List<String>> result = new HashMap<>();
        List<String> students = listStudentCourses();
        List<String> teachers = listTeacherCourses();
        result.put("student", students);
        result.put("teacher", teachers);
        return result;
    }

    @ResponseBody
    @GetMapping(path = "/{userType}")
    public List<String> list(@PathVariable("userType") String userType) {
        return list().get(userType);
    }

    @ResponseBody
    @GetMapping(path = "/{userType}/{name}")
    public String get(@PathVariable("userType") String userType, @PathVariable("name") String name) {
        Optional<String> first = list().get(userType).stream().filter(course -> course.equals(name)).findFirst();
        return first.isPresent() ? first.get() : "";
    }

    private List<String> listStudentCourses() {
        List<String> list = new ArrayList<>();
        list.add("English");
        list.add("Mathematics");
        list.add("Chemistry");
        return list;
    }

    private List<String> listTeacherCourses() {
        List<String> list = listStudentCourses();
        list.add("philosophy");
        list.add("political");
        return list;
    }

}
