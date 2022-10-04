package mg.softlab.sirh.candidate;

import mg.softlab.sirh.jobOffer.JobOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    List<Candidate> findByJobOffer(JobOffer jobOffer);

    @Query(value = "SELECT c " +
            "FROM Candidate c JOIN c.person p JOIN p.degrees d JOIN d.category cat " +
            "WHERE c.jobOffer.id = :offerId " +
            "ORDER BY cat.level DESC")
    List<Candidate> orderByDegree(@Param("offerId") Long jobOfferId);
}
