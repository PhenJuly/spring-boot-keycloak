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
        name = "security-api-3-teacher-service",
        url = "http://phenjuly:8086/api/t/teachers/",
        configuration = FeignAuthRequestInterceptor.class
)
public interface TeacherService {
    @GetMapping
    List<String> list();

    @GetMapping(path = "/{name}")
    String get(@PathVariable("name") String name);
}
