package mg.softlab.sirh.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import mg.softlab.sirh.authentication.UsernameAndPasswordRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

/**
 * Filtre d'authentification JWT d'un utilisateur
 *
 * @author Mandresy
 */
@RequiredArgsConstructor
public class JwtAuthentication extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // Récuperation des données d'authentification
            UsernameAndPasswordRequest usernameAndPasswordRequest = new ObjectMapper().readValue(
                    request.getInputStream(), UsernameAndPasswordRequest.class
            );

            // Hashage du mot de passe en sha256
            usernameAndPasswordRequest.hashPassword();

            // Création d'une nouvelle authentification
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    usernameAndPasswordRequest.getUsername(), usernameAndPasswordRequest.getPassword()
            );
            return authenticationManager.authenticate(authentication);
        } catch (IOException e) {
            logger.warn(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String token = Jwts.builder()
                .setSubject(authResult.getName())
                .claim("authorities", authResult.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration( java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpiredAfterDays())) )
                .signWith(secretKey)
                .compact();

        response.addHeader(jwtConfig.getAuthorizationHeader(), jwtConfig.getTokenPrefix() + token);
    }
}
