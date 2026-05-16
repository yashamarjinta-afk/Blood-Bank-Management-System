package com.BloodBank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class config {

	@Bean
	public WebMvcConfigurer crossorigin() {
		return new WebMvcConfigurer() {
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
				.allowedOrigins("http://localhost:5173/")
				.allowedMethods("GET","POST","DELETE","PUT")
				.allowedHeaders("*");
			}
		};
	}
	
	
	@Bean
	 public BCryptPasswordEncoder passwordencoder() {
        return new BCryptPasswordEncoder();
    }
}
