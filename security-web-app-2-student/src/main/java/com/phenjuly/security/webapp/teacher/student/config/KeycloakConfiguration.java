package com.phenjuly.security.webapp.teacher.student.config;

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author PhenJuly
 */
@Import(KeycloakSpringBootConfigResolver.class)
@Configuration
public class KeycloakConfiguration {
}
