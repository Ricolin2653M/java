package mx.edu.uteq.HolaMundo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() throws Exception {

        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User
                .withUsername("user")
                .password(passwordEncoder().encode("123456"))
                .roles("USER")
                .build());

        manager.createUser(User
                .withUsername("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("ADMIN", "USER")
                .build());

        return manager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authz) -> {
                    authz.requestMatchers("/").permitAll()
                            .requestMatchers("/ofertaeducativa/**").hasRole("USER")
                            .requestMatchers("/agregarOferta/**").hasRole("ADMIN")
                            .requestMatchers("/api/**").hasRole("ADMIN")
                            .requestMatchers("/admisiones-borrar/**").hasRole("ADMIN")
                            .requestMatchers("/guardar-admision/**").hasRole("ADMIN")
                            .requestMatchers("/modificarOferta/**")
                            .hasAnyRole("ADMIN", "COORDINADOR")
                            .requestMatchers("/eliminarOferta/**").hasRole("ADMIN")
                            .anyRequest().authenticated();
                }
                )
                .formLogin((form) -> form
                .loginPage("/login")
                .permitAll()
                )
                .logout((logout) -> logout.permitAll());

        return http.build();
    }

}