package mg.softlab.sirh.Candidate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.softlab.sirh.jobOffer.JobOffer;
import mg.softlab.sirh.person.Person;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Candidate {
    @Id
    @SequenceGenerator(
            name = "sequence_candidate",
            sequenceName = "sequence_candidate"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequence_candidate"
    )
    private Long id;

    @org.springframework.data.annotation.Transient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    @JsonBackReference("person_candidate")
    private Person person;

    @org.springframework.data.annotation.Transient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_offer_id")
    @JsonBackReference("job_offer_candidate")
    private JobOffer jobOffer;

    @Column(nullable = false)
    private Boolean isChoosen;
}
