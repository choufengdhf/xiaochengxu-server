package edu.platform.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Value("${app.jwtSecret}")
  private String jwtSecret;

  @Bean
  public JwtUtil jwtUtil() {
    return new JwtUtil(jwtSecret);
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new AuthInterceptor(jwtUtil()))
      .addPathPatterns("/api/**");
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
      .allowedOrigins("*")
      .allowedMethods("*")
      .allowedHeaders("*");
  }
}