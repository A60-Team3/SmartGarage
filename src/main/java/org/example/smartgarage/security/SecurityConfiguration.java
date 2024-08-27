package org.example.smartgarage.security;

import org.example.smartgarage.security.jwt.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration implements WebMvcConfigurer {

    private static final String[] AUTH_WHITELIST = {
            // -- swagger ui
            "/v2/api-docs", "/v3/api-docs", "/v3/api-docs/**",
            "/swagger-resources", "/swagger-resources/**",
            "/configuration/ui", "/configuration/security",
            "/swagger-ui/**", "/swagger-ui.html",
            "/webjars/**"
    };

    private final JwtFilter jwtFilter;

    public SecurityConfiguration(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(UserDetailsService userDetailsService, HttpSecurity httpSecurity) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity
                .getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
//                .sessionManagement(session ->
//                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    auth.
                            requestMatchers(AUTH_WHITELIST)
                            .permitAll()
                            .requestMatchers("/api/garage/login").permitAll()
                            .requestMatchers("/garage/register", "/garage/login",
                                    "/", "/garage", "/garage/main").permitAll()
                            .requestMatchers("/resources/**", "/static/**", "/static/templates/**",
                                    "/css/**", "/images/**", "/js/**")
                            .permitAll();//
                    auth.anyRequest().authenticated();
                })
                .formLogin(formLogin ->
                        formLogin.loginPage("/garage/login")
                                .defaultSuccessUrl("/garage/main?success", true)
                                .failureUrl("/garage/login?error")
                                .permitAll()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/garage/logout")
                        .addLogoutHandler(new SecurityContextLogoutHandler())
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("/garage/main?logout")
                );

        return http.build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**", "/images/**", "/js/**")
                .addResourceLocations("classpath:/static/css/", "classpath:/static/images/", "classpath:/static/js/");
    }
}
