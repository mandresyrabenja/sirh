package mg.softlab.sirh.person;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Boolean existsByEmail(String email);

    Optional<Person> findByEmail(String email);
}
