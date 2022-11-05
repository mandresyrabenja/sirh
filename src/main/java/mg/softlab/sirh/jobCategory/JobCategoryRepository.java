package mg.softlab.sirh.jobCategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobCategoryRepository extends JpaRepository<JobCategory, Long> {

    @Query("SELECT cat FROM JobCategory cat WHERE lower(cat.name) LIKE lower(concat('%', :name,'%') ) ")
    List<JobCategory> searchJobCat(@Param("name") String name);
}
