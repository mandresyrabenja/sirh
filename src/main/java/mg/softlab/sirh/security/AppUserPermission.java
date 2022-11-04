package mg.softlab.sirh.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Les permissions des utilisateurs
 *
 * @author Mandresy
 */
@RequiredArgsConstructor
@Getter
public enum AppUserPermission {
    SIMPLE_USER("simple_user"),
    ADMIN("admin");

    private final String permission;
}