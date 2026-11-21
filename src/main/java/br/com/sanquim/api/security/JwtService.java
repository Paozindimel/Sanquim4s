package br.com.sanquim.api.security;

import br.com.sanquim.api.domain.entity.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class JwtService {

    private static final String ISSUER = "colegio-sanquim-api";

    @Value("${api.security.jwt.secret}")
    private String secret;

    @Value("${api.security.jwt.expiration-ms}")
    private long expirationMs;

    public String gerarToken(Usuario usuario) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        Instant agora = Instant.now();
        Instant expiracao = agora.plusMillis(expirationMs);

        return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(usuario.getEmail())
                .withClaim("userId", usuario.getId())
                .withClaim("nome", usuario.getNome())
                .withClaim("role", usuario.getRole().name())
                .withIssuedAt(agora)
                .withExpiresAt(expiracao)
                .sign(algorithm);
    }

    public String validarTokenEObterEmail(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException ex) {
            return null;
        }
    }

    public long getExpirationSeconds() {
        return expirationMs / 1000;
    }

    public Instant calcularDataExpiracao() {
        return Instant.now().plusMillis(expirationMs);
    }
}
