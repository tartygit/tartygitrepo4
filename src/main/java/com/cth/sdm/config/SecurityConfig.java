package com.cth.sdm.config;

import com.cth.sdm.entity.User;
import com.cth.sdm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${app.ldap.enabled:false}")
    private boolean ldapEnabled;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    user.isEnabled(),
                    true,
                    true,
                    !user.isAccountLocked(),
                    List.of(new SimpleGrantedAuthority(user.getRole()))
            );
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(frame -> frame.disable())) // Enable frames for previews & H2 console
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/index.html", "/login", "/css/**", "/js/**", "/swagger-ui/**", "/v3/api-docs/**", "/api/config/**", "/api/auth/**", "/api/documents/stream", "/h2-console/**").permitAll()
                .requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/api/checker/**").hasAnyAuthority("ROLE_CHECKER", "ROLE_ADMIN")
                .anyRequest().permitAll()
            )
            .formLogin(form -> form
                .loginPage("/index.html")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(logout -> logout.permitAll());

        return http.build();
    }
}
