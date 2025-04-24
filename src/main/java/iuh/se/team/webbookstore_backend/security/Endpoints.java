package iuh.se.team.webbookstore_backend.security;

public class Endpoints {

    public static final String front_end_host = "http://localhost:3000";
    public static final String[] PUBLIC_GET_ENDPOINS = {
            "/books",
            "/books/**",
            "/images",
            "/images/**",
            "/users/search/existsByTenDangNhap",
            "/users/search/existsByEmail",
    };

    public static final String[] PUBLIC_POST_ENDPOINS = {
            "/account/sign-up",
    };

        public static final String[] ADMIN_GET_ENDPOINS = {
            "/users",
            "/users/**",
    };
}
