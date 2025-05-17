package iuh.se.team.webbookstore_backend.security;

import iuh.se.team.webbookstore_backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

@Configuration
public class SecurityConfiguration {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    //Mã hóa mật khẩu bằng BCryptPasswordEncoder
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //Cung cấp một AuthenticationProvider để xác thực người dùng dựa trên cơ sở dữ liệu
    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserService userService){
        DaoAuthenticationProvider dap = new DaoAuthenticationProvider();
        dap.setUserDetailsService(userService);
        dap.setPasswordEncoder(passwordEncoder());
        return dap;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                config -> config
                        .requestMatchers(HttpMethod.GET, Endpoints.PUBLIC_GET_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.POST, Endpoints.PUBLIC_POST_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/orders").permitAll()

                        .requestMatchers("/api/cart/**").authenticated()

                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, Endpoints.ADMIN_GET_ENDPOINTS).hasAuthority("ADMIN")

                        .anyRequest().authenticated()
        );

        //- Cấu hình CORS cho phép các yêu cầu từ phía frontend (client) truy cập đến API.
        http.cors(cors -> {
            cors.configurationSource(request -> {
                CorsConfiguration corsConfig = new CorsConfiguration();
                corsConfig.addAllowedOrigin(Endpoints.front_end_host);//- Cho phép yêu cầu từ một host được chỉ định (`front_end_host`).
                corsConfig.addAllowedOriginPattern("*");

                corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                corsConfig.addAllowedHeader("*"); // -Chấp nhận mọi header từ phía client.
                return corsConfig;
            });
        });



        //Mục đích: Kích hoạt xác thực HTTP Basic
        http.httpBasic(Customizer.withDefaults());

        //- Cấu hình để sử dụng JWT cho xác thực
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        //Vô hiệu hóa CSRF (Cross-Site Request Forgery)
        http.csrf(csrf -> csrf.disable());//Là một cơ chế bảo mật để ngăn chặn các yêu cầu giả mạo từ trình duyệt.
        return http.build();
    }


}
