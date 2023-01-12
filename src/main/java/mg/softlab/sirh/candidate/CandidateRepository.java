package mg.softlab.sirh.candidate;

import mg.softlab.sirh.job.Job;
import mg.softlab.sirh.jobOffer.JobOffer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    Page<Candidate> findByJobOffer(JobOffer jobOffer, Pageable pageable);

    List<Candidate> findByJobOffer(JobOffer jobOffer);


    @Query(value = "SELECT c.id " +
            "FROM Candidate c JOIN c.person p JOIN p.degrees d JOIN d.category cat " +
            "WHERE c.jobOffer.id = :offerId " +
            "ORDER BY cat.level DESC")
    List<Long> orderByDegree(@Param("offerId") Long jobOfferId);

    /**
     * Trier les candidats d'une offre de travail par année d'expérience
     * @param jobOfferId ID de l'offre de travail
     * @return La liste des ID des candidats avec le nombre des jours de ses expériences professionelles
     */
    @Query(value = "SELECT c.id, SUM(date_part('day', e.end_date\\:\\:timestamp - e.start_date\\:\\:timestamp)) AS jour_experience " +
            "FROM candidate c JOIN person p on c.person_id = p.id JOIN experience e on p.id = e.person_id " +
            "WHERE c.job_offer_id = :offer_id " +
            "GROUP BY c.id " +
            "ORDER BY jour_experience DESC ",
            nativeQuery = true)
    List<Object[]> orderByExperience(@Param("offer_id") Long jobOfferId);

    Page<Candidate> findByIsChoosenTrueAndJobOffer(JobOffer jobOffer, Pageable pageable);

    @Query(value = "SELECT j FROM Candidate c JOIN c.jobOffer o JOIN o.job j WHERE c.id = :id")
    Job getCandidateJob(@Param("id") Long id);
}
