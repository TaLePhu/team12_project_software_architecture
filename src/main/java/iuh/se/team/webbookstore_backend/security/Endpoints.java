package iuh.se.team.webbookstore_backend.security;

public class Endpoints {

    public static final String front_end_host = "http://localhost:3000";
    public static final String[] PUBLIC_GET_ENDPOINTS = {
            "/books",
            "/books/**",
            "/books/search",
            "/books/search/**",
            "/books/search/findByBookNameContainingAndCategoryIds",
            "/search/",
            "/search/**",
            "/books/*/images",
            "/images",
            "/images/**",
            "/users/search/existsByUsername",
            "/users/search/existsByEmail",
            "/account/activate",
            "/api/payment-methods",
            "/api/shipping-methods",
            "/api/orders/confirm",
            "/categories",
            "/categories/**",
            "/api/orders",
            "/api/orders/**",


    };

    public static final String[] PUBLIC_POST_ENDPOINTS = {
            "/account/sign-up",
            "/api/orders",
            "/account/sign-in",
            "/account/forgot-password",
            "/account/reset-password",
            "/images/upload",
    };

        public static final String[] ADMIN_GET_ENDPOINTS = {
            "/users",
            "/users/**",
                "/api/statistics/dashboard",
                "/admin/orders",
                "/admin/users",
                "/admin/users/**"
    };

    public static final String[] ADMIN_POST_ENDPOINTS = {
            "/books",
            "/categories",
            "/admin/orders",
            "/admin/orders/**",
            "/admin/users",

    };

    public static final String[] ADMIN_DELETE_ENDPOINTS = {
            "/books",
            "/categories",
            "/admin/orders",
            "/admin/orders/**",
            "/admin/users",
            "/admin/users/**"
    };

    public static final String[] ADMIN_PUT_ENDPOINTS = {
            "/admin/orders",
            "/admin/orders/**",
            "/admin/users",
    };
}
