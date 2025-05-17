package iuh.se.team.webbookstore_backend.security;

public class JwtResponse {
    private final String jwt;
    private String role;

    public JwtResponse(String jwt, String role) {
        this.jwt = jwt;
        this.role = role;
    }

    public String getJwt() {
        return jwt;
    }
    public String getRole() {
        return role;
    }
}
