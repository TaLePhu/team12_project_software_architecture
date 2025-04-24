package iuh.se.team.webbookstore_backend.security;

public class Endpoints {

    public static final String front_end_host = "http://localhost:3000";
    public static final String[] PUBLIC_GET_ENDPOINTS = {
            "/books",
            "/books/**",
            "/books/*/images",
            "/images",
            "/images/**",
            "/users/search/existsByUsername",
            "/users/search/existsByEmail",
    };

    public static final String[] PUBLIC_POST_ENDPOINTS = {
            "/account/sign-up",
    };

        public static final String[] ADMIN_GET_ENDPOINTS = {
            "/users",
            "/users/**",
    };
}
