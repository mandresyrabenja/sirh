package mg.softlab.sirh.interview;

import mg.softlab.sirh.jobOffer.JobOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InterviewRepository extends JpaRepository<Interview, Long> {
    List<Interview> findByJobOffer(JobOffer offer);

    /**
     * Trier le resultat des candidats par les points qu'ils ont obtenus
     * @param jobOfferId ID de l'offre d'emploi
     * @return Une liste des tableaux contenant le resultat:<br>
     *         <ul>
     *             <li>[0] -> ID du candidat</li>
     *             <li>[1] -> somme des points obtenus par les reponses du candidat</li>
     *             <li>[2] -> bonus obtenu par le candidat</li>
     *             <li>[3] -> total des points obtenu par le candidat</li>
     *         </ul>
     */
    @Query(value = """
                SELECT
                    c.id, SUM(cr.mark) AS response, coalesce(b.point, 0) AS bonus, SUM(cr.mark) + coalesce(b.point, 0) AS total
                FROM
                    interview i JOIN candidate c on i.candidate_id = c.id
                    JOIN candidate_response cr on c.id = cr.candidate_id
                    LEFT OUTER JOIN bonus b on i.id = b.interview_id
                WHERE
                    c.job_offer_id = 1
                GROUP BY
                    c.id, bonus
                ORDER BY
                    total DESC
                """,
        nativeQuery = true
    )
    List<Object[]> orderCandidateResponseByPoint(@Param("offer_id") Long jobOfferId);
}
