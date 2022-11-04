package mg.softlab.sirh.admin;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByUsernameIgnoreCase(String username);

    Boolean existsByUsername(String username);
}