package com.phenjuly.security.api.service;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class StudentService {
    public List<String> list() {
        return Arrays.asList("puppte","Bakugou Katsuki","轰焦冻");
    }
}
