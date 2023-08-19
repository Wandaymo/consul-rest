package br.com.wandaymo.consulrest.service;

import br.com.wandaymo.consulrest.entity.User;
import br.com.wandaymo.consulrest.log.Logged;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    @Logged
    public String getToken(User user) {
        return JWT.create()
                .withIssuer("Restrictives")
                .withSubject(user.getUsername())
                .withClaim("id", user.getId())
                .withExpiresAt(LocalDateTime.now().plusDays(1).toInstant(ZoneOffset.of("-03:00"))
                ).sign(Algorithm.HMAC256("bWV1bm9tZW5hb2Vqb25ueQ=="));
    }

    public String getSubject(String token) {
        return JWT.require(Algorithm.HMAC256("bWV1bm9tZW5hb2Vqb25ueQ=="))
                    .withIssuer("Restrictives")
                    .build().verify(token).getSubject();
    }
}
