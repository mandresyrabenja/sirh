package mg.softlab.sirh.security.jwt;

import com.google.common.net.HttpHeaders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

/**
 * Les configurations d'authentification JWT dans le fichier 'application.yml'
 *
 * @author Mandresy
 */
@ConfigurationProperties(prefix = "application.jwt")
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class JwtConfig {
    private String secretKey;
    private String tokenPrefix;
    private Integer tokenExpiredAfterDays;

    @Bean
    public SecretKey getSecretKeyForSigning() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }
}
