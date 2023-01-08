package mg.softlab.sirh.jobOffer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobOfferRepository extends JpaRepository<JobOffer, Long> {
    Page<JobOffer> findByIsDone(Boolean b, Pageable pageable);
}
