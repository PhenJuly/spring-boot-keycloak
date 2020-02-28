package com.phenjuly.security.webapp.teacher.service;

import com.phenjuly.security.webapp.teacher.infrastructure.FeignAuthRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author PhenJuly
 */
@FeignClient(
        name = "security-api-1-course-service",
        url = "http://phenjuly:8084/api/c/courses/",
        configuration = FeignAuthRequestInterceptor.class
)
public interface CourseService {
    @GetMapping(path = "/teacher")
    List<String> list();

    @GetMapping(path = "/teacher/{name}")
    String get(@PathVariable("name") String name);
}
