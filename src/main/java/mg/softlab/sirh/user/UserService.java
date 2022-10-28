package mg.softlab.sirh.user;

import lombok.AllArgsConstructor;
import mg.softlab.sirh.util.Sha256;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User createUser(User user) {
        user.setPassword(Sha256.hash(user.getPassword()));
        return userRepository.save(user);
    }
}
