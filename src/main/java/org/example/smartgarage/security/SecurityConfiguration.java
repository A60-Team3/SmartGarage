package org.example.smartgarage.security;

import org.example.smartgarage.security.jwt.JwtAuthenticationEntryPoint;
import org.example.smartgarage.security.jwt.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
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

    private static final String[] SWAGGER_WHITELIST = {
            // -- swagger ui
            "/v2/api-docs", "/v3/api-docs", "/v3/api-docs/**",
            "/swagger-resources", "/swagger-resources/**",
            "/configuration/ui", "/configuration/security",
            "/swagger-ui/**", "/swagger-ui.html",
            "/webjars/**"
    };

    private static final String[] MVC_WHITELIST = {
            "/garage/login", "/", "/garage", "/garage/", "/garage/home",
            "/garage/reviews", "/garage/team", "/garage/services",
            "/garage/contacts", "/garage/booking", "/garage/about",
            "/garage/password", "/garage/password/**"
    };

    private static final String[] RESOURCE_WHITELIST = {
            "/resources/**", "/static/**", "/static/templates/**",
            "/css/**", "/img/**", "/js/**", "/lib/**", "/scss/**"
    };

    private final JwtFilter jwtFilter;
    private final JwtAuthenticationEntryPoint jwtEntryPoint;

    public SecurityConfiguration(JwtFilter jwtFilter, JwtAuthenticationEntryPoint jwtEntryPoint) {
        this.jwtFilter = jwtFilter;
        this.jwtEntryPoint = jwtEntryPoint;
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
    @Order(1)
    public SecurityFilterChain restFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**")
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth
                            .requestMatchers("/api/garage/login").permitAll()
                            .requestMatchers(SWAGGER_WHITELIST).permitAll()
                            .requestMatchers("/api/garage/clerks/visits").permitAll();
                    auth.anyRequest().authenticated();
                })
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtEntryPoint))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain mvcFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf ->
                        csrf.ignoringRequestMatchers("/api/**")
                                .ignoringRequestMatchers(SWAGGER_WHITELIST))
                .authorizeHttpRequests(auth -> {
                    auth
                            .requestMatchers(SWAGGER_WHITELIST).permitAll()
                            .requestMatchers(MVC_WHITELIST).permitAll()
                            .requestMatchers(RESOURCE_WHITELIST).permitAll()
                            .requestMatchers("/api/garage/clerks/visits").permitAll();
                    auth.anyRequest().authenticated();
                })
                .formLogin(formLogin -> formLogin
                        .loginPage("/garage/login")
                        .defaultSuccessUrl("/garage/home?success", true)
                        .failureUrl("/garage/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/garage/logout")
                        .addLogoutHandler(new SecurityContextLogoutHandler())
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("/garage/home?logout")
                        .permitAll()
                );

        return http.build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**", "/img/**", "/js/**", "/lib/**", "/scss/**")
                .addResourceLocations("classpath:/static/css/",
                        "classpath:/static/img/", "classpath:/static/js/",
                        "classpath:/static/lib/", "classpath:/static/scss/");
    }
}
