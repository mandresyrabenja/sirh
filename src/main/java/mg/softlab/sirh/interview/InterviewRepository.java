package mg.softlab.sirh.interview;

import mg.softlab.sirh.jobOffer.JobOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InterviewRepository extends JpaRepository<Interview, Long> {
    List<Interview> findByJobOffer(JobOffer offer);

    @Query(value = """
                SELECT
                    c.id, SUM(cr.mark) AS point
                FROM
                    interview i JOIN candidate c on i.candidate_id = c.id
                    JOIN candidate_response cr on c.id = cr.candidate_id
                WHERE
                    c.job_offer_id = :offer_id
                GROUP BY
                    c.id
                ORDER BY
                    point DESC
                """,
        nativeQuery = true
    )
    List<Object[]> orderCandidateResponseByPoint(@Param("offer_id") Long jobOfferId);
}
