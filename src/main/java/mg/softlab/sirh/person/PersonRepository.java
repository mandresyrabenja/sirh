package mg.softlab.sirh.person;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Boolean existsByEmail(String email);

    Optional<Person> findByEmail(String email);

    @Query(value = """
        SELECT p 
        FROM Person p 
        WHERE 
            lower(p.firstname) like lower(concat('%', :name, '%'))
            OR lower(p.name) like lower(concat('%', :name, '%')) 
            OR lower(concat(p.firstname, ' ', p.name)) like lower(concat('%', :name, '%'))
        """)
    Page<Person> searchByName(@Param("name") String name, Pageable pageable);
}
