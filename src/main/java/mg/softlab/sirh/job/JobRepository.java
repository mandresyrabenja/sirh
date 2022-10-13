package mg.softlab.sirh.job;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    @Query(value =  "SELECT j FROM Job j WHERE  lower(j.name) LIKE lower(concat('%', :name,'%')) ")
    List<Job> searchJob(@Param("name") String name);
}
