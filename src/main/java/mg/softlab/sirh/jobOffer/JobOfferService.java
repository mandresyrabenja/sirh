package mg.softlab.sirh.jobOffer;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class JobOfferService {
    private final JobOfferRepository jobOfferRepository;

    public void createJobOffer(JobOffer jobOffer) {
        jobOfferRepository.save(jobOffer);
    }

    public Page<JobOffer> getAvailableJobOffers( Pageable pageable) {
        return jobOfferRepository.findByIsDone(false, pageable);
    }

    public JobOffer findById(Long id) {
        return jobOfferRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("Aucune offre d'emploi n'a " + id + " comme ID")
        );
    }

    @Transactional
    public void markAsDone(Long offerId) {
        JobOffer jobOffer = jobOfferRepository.findById(offerId).orElseThrow(() ->
                new IllegalStateException("Aucune offre n'a " + offerId + " comme ID"));
        jobOffer.setIsDone(true);
    }
}
