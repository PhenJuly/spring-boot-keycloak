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
        name = "security-api-1-course-Service",
        url = "http://phenjuly:8084/api/c/courses", configuration = FeignAuthRequestInterceptor.class
)
public interface CoursesService {
    @GetMapping(path = "/student")
    List<String> list();

    @GetMapping(path = "/student/{name}")
    String get(@PathVariable("name") String name);
}
