package iuh.se.team.webbookstore_backend.security;

import iuh.se.team.webbookstore_backend.services.JwtService;
import iuh.se.team.webbookstore_backend.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        String path = request.getRequestURI();

        if (path.startsWith("/books") && request.getMethod().equalsIgnoreCase("GET")) {
            // Không cần kiểm JWT cho các route GET của /books/**
            filterChain.doFilter(request, response);
            return;
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);

        System.out.println("🟡 Header: " + authHeader);
        System.out.println("🟢 Token: " + token);
        System.out.println("🟢 Username: " + username);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService.loadUserByUsername(username);

            if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
        System.out.println("Vào JwtAuthenticationFilter, URI: " + request.getRequestURI());

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();


        // Ví dụ bạn dùng hàm kiểm tra các path public, hoặc hardcode như trên
        // Nếu là OPTIONS method thì cũng bỏ qua
        System.out.println("🔍 Checking shouldNotFilter for path: " + path);

        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            System.out.println("✅ OPTIONS request, skipping filter.");
            return true;
        }
        // Check các path public
        return path.startsWith("/books")
                || path.startsWith("/categories")
                || path.startsWith("/images")
                || path.startsWith("/users/search/existsByUsername")
                || path.startsWith("/users/search/existsByEmail")
                || path.startsWith("/account/activate")
                || path.startsWith("/api/payment-methods")
                || path.startsWith("/api/shipping-methods")
                || path.startsWith("/api/orders/confirm")
                || path.startsWith("/account/sign-in")
                || path.startsWith("/account/sign-up")
                ||path.startsWith("/books/")
                || path.startsWith("/books/search")
                || path.startsWith("/books/search/")
                || path.equals("/api/orders");

    }

}
