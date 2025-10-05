package com.openclassrooms.mddapi.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** Point d'entrée 401 pour requêtes non authentifiées (API REST). */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Écrit une réponse JSON 401 "Unauthorized" quand aucune authentification valide n'est présente.
     * @param request  requête HTTP
     * @param response réponse HTTP
     * @param authException exception d'authentification levée par Spring Security
     * @throws IOException sur erreur d'écriture de la réponse
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"code\":\"401\",\"message\":\"Unauthorized\"}");
    }
}
