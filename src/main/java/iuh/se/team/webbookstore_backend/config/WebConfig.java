package iuh.se.team.webbookstore_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Cho phép tất cả các domain truy cập
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")  // Chỉ cho phép truy cập từ frontend tại localhost:3001
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Các phương thức HTTP được phép
                .allowedHeaders("*"); // Cho phép tất cả các header
    }
}
