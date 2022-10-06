package mg.softlab.sirh.interview;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.softlab.sirh.candidate.Candidate;
import mg.softlab.sirh.jobOffer.JobOffer;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Interview {
    @Id
    @SequenceGenerator(
            name = "sequence_interview",
            sequenceName = "sequence_interview",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequence_interview"
    )
    private Long id;

    @org.springframework.data.annotation.Transient
    @OneToOne
    @JsonBackReference("candidate_interview")
    private Candidate candidate;

    @org.springframework.data.annotation.Transient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_offer_id")
    @JsonBackReference("job_offer_interview")
    private JobOffer jobOffer;

    @Column(nullable = false)
    @JsonProperty("dateTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dateTime;

    @Column(nullable = true)
    private String remark;
}
