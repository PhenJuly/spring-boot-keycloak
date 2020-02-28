package com.phenjuly.security.webapp.teacher.student.service;

import com.phenjuly.security.webapp.teacher.student.infrastructure.FeignAuthRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author PhenJuly
 */
@FeignClient(
        name = "security-api-2-student-service",
        url = "http://phenjuly:8085/api/s/students/", configuration = FeignAuthRequestInterceptor.class
)
public interface StudentService {
    @GetMapping()
    List<String> list();

    @GetMapping(path = "/{name}")
    String get(@PathVariable("name") String name);
}
