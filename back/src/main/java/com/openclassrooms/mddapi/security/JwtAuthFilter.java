package com.openclassrooms.mddapi.security;

import com.openclassrooms.mddapi.service.JwtService;
import com.openclassrooms.mddapi.service.AppUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/** Filtre qui extrait, vérifie le JWT, et peuple le SecurityContext. */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final AppUserDetailsService userDetailsService;

    /** @param jwtService service JWT (parse/validation) ; @param userDetailsService chargement utilisateur */
    public JwtAuthFilter(JwtService jwtService, AppUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Lit le header Authorization, valide le token et authentifie l'utilisateur.
     * @param request requête HTTP entrante
     * @param response réponse HTTP
     * @param chain chaîne de filtres
     * @throws ServletException en cas d'erreur servlet
     * @throws IOException en cas d'erreur I/O
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                String email = jwtService.getSubject(token);
                var userDetails = userDetailsService.loadUserByUsername(email);

                var auth = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception e) {

            }
        }

        chain.doFilter(request, response);
    }


    /**
     * Exclut les routes d'auth ({@code /api/auth/**}) du filtre JWT.
     * @param request requête HTTP
     * @return true si le filtre ne doit pas s'appliquer
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/api/auth/");
    }
}
