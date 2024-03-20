package mx.edu.uteq.HolaMundo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;
    
   @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    
    @Autowired
    public void configurerGlobal(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authz) -> {
                    authz.requestMatchers("/").permitAll()
                            .requestMatchers("/inicio").permitAll()
                            .requestMatchers("/ofertaeducativa").hasAnyRole("ADMIN","COORDINADOR")
                            .requestMatchers("/admisiones").hasAnyRole("ADMIN","COORDINADOR")
                            .requestMatchers("/listar/**").hasAnyRole("ADMIN","COORDINADOR")
                            .requestMatchers("/api/**").hasAnyRole("ADMIN","COORDINADOR")
                            .requestMatchers("/eliminar/**").hasRole("ADMIN")
                            .requestMatchers("/usuarios").hasAnyRole("ADMIN","COORDINADOR")
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