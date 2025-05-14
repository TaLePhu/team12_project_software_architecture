package iuh.se.team.webbookstore_backend.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import iuh.se.team.webbookstore_backend.entities.User;
import org.springframework.cglib.core.internal.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtService {
    //- Đây là **mã bí mật** (secret key) được sử dụng để ký (sign) các token.
    // Chuỗi mã này được mã hóa **Base64**.
    public static final String SERECT = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    // Tạo JWT dựa trên tên đang nhập
//    public String generateToken(String tenDangNhap) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("isAdmin", true);
//        claims.put("x", "ABC" );
//        return createToken(claims, tenDangNhap);
//    }
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getUserId());
        claims.put("fullName", user.getLastName() + " " + user.getFirstName());
        claims.put("lastName", user.getLastName());
        claims.put("firstName", user.getFirstName());
        claims.put("email", user.getEmail());
        claims.put("phone", user.getPhoneNumber());
        claims.put("billingAddress", user.getBillingAddress());
        claims.put("shippingAddress", user.getShippingAddress());
        claims.put("isActivated", user.isActivated());

        // Lấy vai trò đầu tiên (nếu có)
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            claims.put("role", user.getRoles().get(0).getRoleName()); // hoặc List<String> roles nếu bạn muốn gửi nhiều vai trò
        }

        return createToken(claims, user.getUsername()); // `sub` là username
    }

    // Tạo JWT với các claim đã chọn
    private  String createToken(Map<String, Object> claims, String tenDangNhap){
        return Jwts.builder()
                .setClaims(claims)//- Thêm payload (dữ liệu bổ sung) vào token.
                .setSubject(tenDangNhap)//- Thiết lập thông tin chính (tên người dùng) trong JWT (**subject**).
                .setIssuedAt(new Date(System.currentTimeMillis()))//- - Ngày phát hành token.
                .setExpiration(new Date(System.currentTimeMillis()+30*60*1000)) // JWT hết hạn sau 30 phút
                .signWith(SignatureAlgorithm.HS256,getSigneKey())//- Ký token bằng thuật toán **HS256** với khóa bí mật được trích xuất từ hàm `getSignKey()`.
                .compact();
    }

    // Lấy serect key
    private Key getSigneKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SERECT);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 1. Trích Xuất Toàn Bộ Thông Tin (Claims):
    private Claims extractAllClaims(String token){
        //- Dùng secret key để giải mã token và lấy tất cả các claim (payload + metadata).
        return Jwts.parser()
                .setSigningKey(getSigneKey())
                .parseClaimsJws(token)
                .getBody();
    }

    // Trích xuất thông tin cho 1 claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsTFunction){
        final Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    // Trích Xuất Thời Gian Hết Hạn:
    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    // Trích Xuất `Subject` (Username):
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    // Kiểm Tra Token Đã Hết Hạn:
    private Boolean isTokenExpired(String token){
        //- Kiểm tra thời gian hết hạn của token so với thời gian hiện tại.
        return extractExpiration(token).before(new Date());
    }

    // Kiểm tra tính hợp lệ
    public Boolean validateToken(String token, UserDetails userDetails){
        final String tenDangNhap = extractUsername(token);
        System.out.println(tenDangNhap);
        return (tenDangNhap.equals(userDetails.getUsername())&&!isTokenExpired(token));
    }


}
