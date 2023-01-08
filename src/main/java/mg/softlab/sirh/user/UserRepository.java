package mg.softlab.sirh.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByUsername(String username);

    Optional<User> findByUsernameIgnoreCase(String username);

    @Query("SELECT u FROM simple_user u WHERE lower(u.username) LIKE lower(concat('%', :username,'%') ) ")
    Page<User> searchUser(@Param("username") String username, Pageable pageable);

}
