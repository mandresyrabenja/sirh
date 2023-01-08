package mg.softlab.sirh.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasRole('CEO')")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok("Utilisateur numero " + userId + " effacé avec succès");
        }catch (IllegalStateException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('CEO')")
    public Page<User> search(@RequestParam String username,
                             @RequestParam int page,
                             @RequestParam int size) {
        return userService.search(username, PageRequest.of(page, size));
    }

    @PutMapping(path = "{id}")
    @PreAuthorize("hasRole('HR_EMPLOYEE')")
    public ResponseEntity<String> updateUser(Authentication authentication,
                                             @PathVariable("id") Long userId,
                                             @RequestParam String password)
    {
        try {
            String username = (String) authentication.getPrincipal();
            User user = userService.findByUsername(username);
            if(!user.getId().equals(userId)) {
                throw new IllegalStateException("Vous ne pouvez pas modifier le mot de passe des autres comptes à part le votre");
            }
            userService.updateUser(userId, password);
            return ResponseEntity.ok("Utilisateur numero " + userId + " modifié avec succès");
        }catch (IllegalStateException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Object> getUser(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(userService.findById(id));
        }catch (IllegalStateException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('CEO')")
    public Page<User> getAllUsers(@RequestParam int page,
                                  @RequestParam int size)
    {
        return userService.findAll(PageRequest.of(page, size));
    }

    @PostMapping
    @PreAuthorize("hasRole('CEO')")
    public ResponseEntity<Object> createUser(@RequestBody User user) {
        try{
            return ResponseEntity.ok(userService.createUser(user));
        }catch (IllegalStateException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
