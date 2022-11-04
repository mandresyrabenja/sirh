package mg.softlab.sirh.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mg.softlab.sirh.authentication.ApplicationUser;
import mg.softlab.sirh.security.AppUserRole;
import mg.softlab.sirh.util.Sha256;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class AdminService implements UserDetailsService {
    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin user = adminRepository.findByUsernameIgnoreCase(username).orElseThrow( () -> {
                String msg = String.format("Aucune compte admin n'a %s comme nom d'utilisateur", username);
                log.warn(msg);
                return new UsernameNotFoundException(msg);
            }
        );
        return new ApplicationUser(
                user.getUsername(),
                passwordEncoder.encode( user.getPassword() ),
                AppUserRole.CEO.getGrantedAutorities(),
                true,
                true,
                true,
                true
        );
    }

    public Admin createAdmin(Admin admin) {
        if(adminRepository.existsByUsername(admin.getUsername())) {
            throw new IllegalStateException("Le nom d'utilisateur " + admin.getUsername() + " existe déjà");
        }
        admin.setPassword(Sha256.hash(admin.getPassword()));
        return adminRepository.save(admin);
    }
}
