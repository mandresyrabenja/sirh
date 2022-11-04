package mg.softlab.sirh.security;

import lombok.AllArgsConstructor;
import mg.softlab.sirh.admin.AdminService;
import mg.softlab.sirh.security.jwt.JwtAuthentication;
import mg.softlab.sirh.security.jwt.JwtConfig;
import mg.softlab.sirh.security.jwt.JwtTokenVerifier;
import mg.softlab.sirh.user.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.crypto.SecretKey;

/**
 * Configuration du securité de l'application
 *
 * @author Mandresy
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserService userService;
    private final AdminService adminService;
    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // CORS
                .cors()

                .and()

                // L'authentification JWT ne supporte pas la protection CSRF
                .csrf().disable()
                // L'authentification JWT necessite une session STATELESS
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()

                // Filtre HTTP d'authentification d'utilisateur
                .addFilter(new JwtAuthentication(authenticationManager(), jwtConfig, secretKey))
                // Filtre HTTP de vérification de token
                .addFilterAfter(new JwtTokenVerifier(secretKey, jwtConfig), JwtAuthentication.class)
                // Tous les requêtes autres que '/login' necessite une authentification
                .authorizeRequests()
                    .anyRequest().authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(ceoAuthProvider())
            .authenticationProvider(hrEmployeeAuthProvider());
    }

    /**
     * AuthenticationProvider d'un compte d'employé du RH
     */
    public DaoAuthenticationProvider hrEmployeeAuthProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userService);

        return provider;
    }

    /**
     * AuthenticationProvider du compte du DG
     */
    public DaoAuthenticationProvider ceoAuthProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(adminService);

        return provider;
    }

}
