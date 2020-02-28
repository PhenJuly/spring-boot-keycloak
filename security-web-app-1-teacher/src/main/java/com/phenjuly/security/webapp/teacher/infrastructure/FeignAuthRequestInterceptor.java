package com.phenjuly.security.webapp.teacher.infrastructure;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * @author PhenJuly
 * @create 2020/2/28
 * @since
 */
public class FeignAuthRequestInterceptor implements RequestInterceptor {
    private final String AUTHORIZATION_HEADER = "Authorization";
    private final String BEARER_TOKEN_TYPE = "Bearer ";

    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return;
        }
        HttpServletRequest request = requestAttributes.getRequest();
        Principal userPrincipal = request.getUserPrincipal();

        KeycloakSecurityContext keycloakSecurityContext = null;
        if (userPrincipal instanceof KeycloakAuthenticationToken) {
            KeycloakPrincipal principal = (KeycloakPrincipal) ((KeycloakAuthenticationToken) userPrincipal).getPrincipal();
            keycloakSecurityContext = principal.getKeycloakSecurityContext();
        } else if (userPrincipal instanceof KeycloakPrincipal) {
            keycloakSecurityContext = ((KeycloakPrincipal) userPrincipal).getKeycloakSecurityContext();
        }

        if (keycloakSecurityContext instanceof RefreshableKeycloakSecurityContext) {
            RefreshableKeycloakSecurityContext.class.cast(keycloakSecurityContext).refreshExpiredToken(true);
            requestTemplate.header(AUTHORIZATION_HEADER, BEARER_TOKEN_TYPE + keycloakSecurityContext.getTokenString());
        }

    }
}