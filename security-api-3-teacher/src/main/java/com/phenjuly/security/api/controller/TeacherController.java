package com.phenjuly.security.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequestMapping("/teachers")
@Controller
public class TeacherController {
    @ResponseBody
    @GetMapping
    public List<String> list() {
        List<String> ectoplasm = listTeachers();
        return ectoplasm;
    }

    private List<String> listTeachers() {
        return Arrays.asList("Eraser-Head", "ONE-FOR-ALL  ", "Ectoplasm");
    }

    @ResponseBody
    @GetMapping(path = "/{name}")
    public String get(@PathVariable("name") String name) {
        Optional<String> first = listTeachers().stream().filter(teacher -> teacher.equals(name)).findFirst();
        return first.isPresent() ? first.get() : "";
    }

}
