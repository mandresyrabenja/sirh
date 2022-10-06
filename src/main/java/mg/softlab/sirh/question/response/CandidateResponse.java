package mg.softlab.sirh.question.response;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.softlab.sirh.candidate.Candidate;
import mg.softlab.sirh.question.Question;

import javax.persistence.*;

@Entity(name = "candidate_response")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateResponse {
    @Id
    @SequenceGenerator(
            name = "sequence_response",
            sequenceName = "sequence_response",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequence_response"
    )
    private Long id;

    @org.springframework.data.annotation.Transient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id")
    @JsonBackReference("candidate_response")
    private Candidate candidate;

    @org.springframework.data.annotation.Transient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    @JsonBackReference("question_response")
    private Question question;

    @Column(nullable = false)
    private Double mark;
}
