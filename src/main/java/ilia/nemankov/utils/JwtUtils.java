package ilia.nemankov.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.lifetime}")
    private Integer lifetime;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public Date getIssueDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    public String getUserIdFromToken(String token) {
        return getClaimFromToken(token, Claims::getId);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public String getTokenFromHeader(String header) {
        String jwtToken = null;
        if (header != null && header.startsWith("Bearer ")) {
            jwtToken = header.substring(7);
        } else {

        }
        return jwtToken;
    }

    private Boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(String login, long id) {
        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder().setClaims(claims).setSubject(login).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + lifetime * 60 * 60 * 1000))
                .setId(id + "")
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails, Date lastLogout) {
        String username = getUsernameFromToken(token);
        if (getIssueDateFromToken(token).before(lastLogout)) {
            return false;
        }
        return (username.equals(userDetails.getUsername())  && !isTokenExpired(token));
    }

}
