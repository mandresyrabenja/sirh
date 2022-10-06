package mg.softlab.sirh.candidate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.softlab.sirh.interview.Interview;
import mg.softlab.sirh.jobOffer.JobOffer;
import mg.softlab.sirh.person.Person;
import mg.softlab.sirh.question.response.CandidateResponse;

import javax.persistence.*;
import java.util.Collection;

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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id")
    private Person person;

    @org.springframework.data.annotation.Transient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_offer_id")
    @JsonBackReference("job_offer_candidate")
    private JobOffer jobOffer;

    @Column(nullable = false)
    private Boolean isChoosen;

    @org.springframework.data.annotation.Transient
    @OneToOne(mappedBy = "candidate")
    @JsonBackReference("candidate_interview")
    private Interview interview;

    @org.springframework.data.annotation.Transient
    @OneToMany(mappedBy = "candidate")
    @JsonManagedReference("candidate_response")
    private Collection<CandidateResponse> candidateResponses;

    @Override
    public String toString() {
        return "Candidate{" +
                "id=" + id +
                ", person=" + person +
                ", jobOffer=" + jobOffer +
                ", isChoosen=" + isChoosen +
                '}';
    }
}
