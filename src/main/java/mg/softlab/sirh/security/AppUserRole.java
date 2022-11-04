package mg.softlab.sirh.security;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static mg.softlab.sirh.security.AppUserPermission.*;

/**
 * Les roles des utilisateurs pour spring security
 *
 * @author Mandresy
 */
@RequiredArgsConstructor
@Getter
public enum AppUserRole {
    CEO(
        Sets.newHashSet(
                ADMIN
        )
    )
    ,HR_EMPLOYEE(
        Sets.newHashSet(
                SIMPLE_USER
        )
    );

    private final Set<AppUserPermission> permissions;

    /**
     * Avoir les authorities de cet role sous forme d'un Set
     * @return Set des authorities
     */
    public Set<SimpleGrantedAuthority> getGrantedAutorities() {

        // Affectation des authorities dans un Set
        Set<SimpleGrantedAuthority> authorities = getPermissions()
                .stream()
                .map(
                    permission -> new SimpleGrantedAuthority(permission.getPermission())
                )
                .collect(Collectors.toSet());

        // Ajout du role
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return authorities;
    }
}
