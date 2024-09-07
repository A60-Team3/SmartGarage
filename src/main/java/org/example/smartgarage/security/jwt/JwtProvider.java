package org.example.smartgarage.security.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
public class JwtProvider {

    private static final SecretKey key = Jwts.SIG.HS512.key().build();
    public static final int TOKEN_VALIDITY_HOURS = 1;

    public String generateToken(UserDetails userDetails) {
        Date issueDate = Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC));
        Date expirationDate = Date.from(LocalDateTime.now()
                .plusHours(TOKEN_VALIDITY_HOURS)
                .toInstant(ZoneOffset.UTC));

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("authorities",populateAuthorities(userDetails.getAuthorities()))
                .issuedAt(issueDate)
                .expiration(expirationDate)
                .signWith(key)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build().parseSignedClaims(token).getPayload().getSubject();
    }

    public Date getExpirationDateFromToken(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getExpiration();
    }

    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<String> authoritiesSet = new HashSet<>();
        for(GrantedAuthority authority: authorities) {
            authoritiesSet.add(authority.getAuthority());
        }
        return String.join(",", authoritiesSet);
    }
}
