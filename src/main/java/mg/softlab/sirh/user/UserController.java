package mg.softlab.sirh.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok("Utilisateur numero " + userId + " effacé avec succès");
        }catch (IllegalStateException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping("/search")
    public List<User> search(@RequestParam String username) {
        return userService.search(username);
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<String> updateUser(@PathVariable("id") Long userId, @RequestParam String password) {
        try {
            userService.updateUser(userId, password);
            return ResponseEntity.ok("Utilisateur numero " + userId + " modifié avec succès");
        }catch (IllegalStateException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity getUser(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(userService.findById(id));
        }catch (IllegalStateException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping
    public List<User> getAllUsers() { return userService.findAll(); }

    @PostMapping
    public User createUser(@RequestBody User user) { return userService.createUser(user); }
}
