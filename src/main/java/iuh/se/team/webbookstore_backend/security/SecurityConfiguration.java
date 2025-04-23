package iuh.se.team.webbookstore_backend.security;

import iuh.se.team.webbookstore_backend.services.UserSevice;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
    //Mã hóa mật khẩu bằng BCryptPasswordEncoder
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //Cung cấp một AuthenticationProvider để xác thực người dùng dựa trên cơ sở dữ liệu
    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserSevice userService){
        DaoAuthenticationProvider dap = new DaoAuthenticationProvider();
        dap.setUserDetailsService(userService);
        dap.setPasswordEncoder(passwordEncoder());
        return dap;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                config -> config
                        .requestMatchers(HttpMethod.GET, "/books").permitAll()
                        .requestMatchers(HttpMethod.GET, "/books/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/images").permitAll()
                        .requestMatchers(HttpMethod.GET, "/images/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users").hasAnyAuthority("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.GET, "/users/search/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/account/sign-up").permitAll()
        );
        //Mục đích: Kích hoạt xác thực HTTP Basic
        http.httpBasic(Customizer.withDefaults());
        //Vô hiệu hóa CSRF (Cross-Site Request Forgery)
        http.csrf(csrf -> csrf.disable());//Là một cơ chế bảo mật để ngăn chặn các yêu cầu giả mạo từ trình duyệt.
        return http.build();
    }


}
