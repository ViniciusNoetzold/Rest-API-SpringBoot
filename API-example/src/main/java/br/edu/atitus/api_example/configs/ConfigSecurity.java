package br.edu.atitus.api_example.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import br.edu.atitus.api_example.components.AuthTokenFilter;

@Configuration
public class ConfigSecurity {
	
	@Bean
	SecurityFilterChain getSecurityFilter(HttpSecurity http, AuthTokenFilter authTokenFilter) throws Exception {
	    http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	      .csrf(csrf -> csrf.disable())
	        .authorizeHttpRequests(auth -> auth
	        	.requestMatchers(HttpMethod.OPTIONS).permitAll()
	        	.requestMatchers("/auth/**").permitAll() 
	            .requestMatchers("/ws**", "/ws/**").authenticated() 
	            .requestMatchers("/api/**").authenticated()
	            .anyRequest().authenticated())
	            
	        .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

	    return http.build();
	}

	@Bean
	PasswordEncoder getPasswordEncode() {
		return new BCryptPasswordEncoder();
	}
}
