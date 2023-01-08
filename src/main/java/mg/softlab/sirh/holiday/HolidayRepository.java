package mg.softlab.sirh.holiday;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {

    @Query("SELECT h FROM Holiday h WHERE lower(h.name) LIKE lower(concat('%', :name,'%') ) ")
    Page<Holiday> searchHoliday(@Param("name") String name, Pageable pageable);
}
