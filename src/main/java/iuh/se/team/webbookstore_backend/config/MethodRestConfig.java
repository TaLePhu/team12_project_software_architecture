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
    private String url = "http://localhost:3000";

    @Autowired
    private EntityManager entityManager;

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        config.exposeIdsFor(entityManager.getMetamodel().getEntities().stream().map(Type::getJavaType).toArray(Class[]::new));
//       //config.exposeIdsFor(TheLoai.class);


        //CORS configuration
        cors.addMapping("/**")
                .allowedOrigins(url)
                .allowedMethods("GET", "POST", "PUT", "DELETE");


//        chan cac method
        HttpMethod[] chanCacPhuongThuc = {
                HttpMethod.POST,
                HttpMethod.PUT,
                HttpMethod.DELETE,
                HttpMethod.PATCH,
        };
        disableHttpMethods(Category.class, config, chanCacPhuongThuc);

// Chặn các method DELETE
        HttpMethod[] phuongThucDelete = {
                HttpMethod.DELETE
        };
        disableHttpMethods(User.class, config,phuongThucDelete );

    }

    // vo hieu hoa cac method http cho bat ky entities nao
    private void disableHttpMethods(Class c,
                                    RepositoryRestConfiguration config,
                                    HttpMethod[] methods){
        config.getExposureConfiguration()
                .forDomainType(c)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(methods))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(methods));
    }
}