package cineconecta.cineconecta.service.Jwt;

import cineconecta.cineconecta.model.Users.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;

@Service
public class JwtService {

    @Value("${jwt.secret.access}")
    private String SECRET_KEY_ACCESS_TOKEN;

    public String getAccessToken(User user){
        return getAccessToken(new HashMap<>(), user);
    }

    private String getAccessToken(HashMap<String,Object> extraClaims, User user) {
        extraClaims.put("name", user.getName());
        extraClaims.put("email", user.getEmail());
        extraClaims.put("userType", user.getClass().getSimpleName());

        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getId().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60)) // 1 hora
                .signWith(getKeyAccessToken(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKeyAccessToken() {
        return Keys.hmacShaKeyFor(SECRET_KEY_ACCESS_TOKEN.getBytes());
    }

    public String extractUserIdFromAccessToken(String accessToken) {
        Key key = getKeyAccessToken();
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();
        return claims.getSubject();
    }


}
