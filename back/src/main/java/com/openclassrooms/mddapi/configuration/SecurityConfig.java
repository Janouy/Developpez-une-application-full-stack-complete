package com.openclassrooms.mddapi.configuration;

import com.openclassrooms.mddapi.security.JwtAuthFilter;
import com.openclassrooms.mddapi.security.RestAuthenticationEntryPoint;
import com.openclassrooms.mddapi.service.AppUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/** Configuration Spring Security. */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AppUserDetailsService uds;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    /** @param jwtAuthFilter filtre JWT ; @param uds chargeur d'utilisateurs ; @param restAuthenticationEntryPoint 401 JSON */
    public SecurityConfig(JwtAuthFilter jwtAuthFilter, AppUserDetailsService uds, RestAuthenticationEntryPoint restAuthenticationEntryPoint) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.uds = uds;
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
    }

    /**
     * Chaîne de filtres HTTP (CORS, CSRF off, stateless, JWT, règles d'accès).
     * @param http builder HttpSecurity
     * @return la SecurityFilterChain configurée
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(restAuthenticationEntryPoint)
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(daoAuthProvider())
                .build();
    }

    /**
     * Provider d'authentification basé sur UserDetailsService + BCrypt.
     * @return AuthenticationProvider DAO
     */
    @Bean
    public AuthenticationProvider daoAuthProvider() {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setUserDetailsService(uds);
        p.setPasswordEncoder(passwordEncoder());
        return p;
    }

    /** @return encodeur de mots de passe BCrypt */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Manager d'authentification exposé par Spring.
     * @param cfg configuration d'authentification
     * @return AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }
}
