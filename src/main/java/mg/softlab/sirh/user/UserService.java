package mg.softlab.sirh.user;

import lombok.AllArgsConstructor;
import mg.softlab.sirh.util.Sha256;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> search(String username) {
        return userRepository.searchUser(username);
    }

    public User createUser(User user) {
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
