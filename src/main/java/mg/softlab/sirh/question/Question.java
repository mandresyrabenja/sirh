package mg.softlab.sirh.question;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.softlab.sirh.jobOffer.JobOffer;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Question {
    @Id
    @SequenceGenerator(
            name = "sequence_question",
            sequenceName = "sequence_question",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequence_question"
    )
    private Long id;

    @org.springframework.data.annotation.Transient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_offer_id")
    @JsonBackReference("job_offer_question")
    private JobOffer jobOffer;

    @Column(nullable = false)
    private String description;

    /**
     * Note maximale du question
     */
    @Column(nullable = false)
    private Short maxMark;
}
