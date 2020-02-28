package com.phenjuly.security.api.config;

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(KeycloakSpringBootConfigResolver.class)
@Configuration
public class KeycloakConfiguration {
}
