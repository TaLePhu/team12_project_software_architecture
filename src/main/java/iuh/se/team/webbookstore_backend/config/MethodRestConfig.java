package iuh.se.team.webbookstore_backend.config;

import iuh.se.team.webbookstore_backend.entities.Category;
import iuh.se.team.webbookstore_backend.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class MethodRestConfig implements RepositoryRestConfigurer {
    private final String[] allowedOrigins = {
            "http://localhost:3000",
            "http://localhost:3001",
            "http://localhost:5173" // Nếu dùng Vite
    };

    @Autowired
    private EntityManager entityManager;

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        config.exposeIdsFor(entityManager.getMetamodel().getEntities().stream().map(Type::getJavaType).toArray(Class[]::new));

        // Cấu hình CORS
        cors.addMapping("/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Thêm OPTIONS
                .allowedHeaders("*")
                .allowCredentials(true); // Nếu có Cookie/X-Auth-Token

        // Chặn các phương thức không mong muốn
        HttpMethod[] chanCacPhuongThuc = {
                HttpMethod.POST,
                HttpMethod.PUT,
                HttpMethod.DELETE,
                HttpMethod.PATCH
        };
        disableHttpMethods(Category.class, config, chanCacPhuongThuc);

        // Nếu muốn mở DELETE cho User, xóa dòng dưới
        HttpMethod[] phuongThucDelete = { HttpMethod.DELETE };
        disableHttpMethods(User.class, config, phuongThucDelete);
    }

    private void disableHttpMethods(Class<?> c, RepositoryRestConfiguration config, HttpMethod[] methods) {
        config.getExposureConfiguration()
                .forDomainType(c)
                .withItemExposure((metadata, httpMethods) -> httpMethods.disable(methods))
                .withCollectionExposure((metadata, httpMethods) -> httpMethods.disable(methods));
    }
}