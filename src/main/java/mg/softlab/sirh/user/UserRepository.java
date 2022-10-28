package mg.softlab.sirh.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM simple_user u WHERE lower(u.username) LIKE lower(concat('%', :username,'%') ) ")
    public List<User> searchUser(@Param("username") String username);

}
