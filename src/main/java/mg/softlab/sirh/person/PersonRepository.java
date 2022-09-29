package mg.softlab.sirh.person;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Boolean existsByEmail(String email);
}
