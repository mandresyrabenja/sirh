package mg.softlab.sirh.user;

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

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameIgnoreCase(username).orElseThrow( () -> {
            String msg = String.format("Aucune compte d'employe RH n'a %s comme nom d'utilisateur", username);
            log.warn(msg);
            return new UsernameNotFoundException(msg);
            }
        );
        return new ApplicationUser(
                user.getUsername(),
                passwordEncoder.encode( user.getPassword() ),
                AppUserRole.HR_EMPLOYEE.getGrantedAutorities(),
                true,
                true,
                true,
                true
        );
    }

    public List<User> search(String username) {
        return userRepository.searchUser(username);
    }

    public User createUser(User user) {
        if(userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalStateException("Le nom d'utilisateur " + user.getUsername() + " existe déjà");
        }
        user.setPassword(Sha256.hash(user.getPassword()));
        return userRepository.save(user);
    }

    public List<User> findAll() { return userRepository.findAll(); }

    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
           new IllegalStateException("Aucun utilisateur n'a " + userId + " comme ID"));
    }

    @Transactional
    public void updateUser(Long userId, String password) {
        User user = findById(userId);
        if(null != password && !"".equalsIgnoreCase(password)) {
            user.setPassword(Sha256.hash(password));
        }
    }

    public void deleteUser(Long userId) {
        userRepository.delete(findById(userId));
    }

}
