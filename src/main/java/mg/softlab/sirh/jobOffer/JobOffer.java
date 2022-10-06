package mg.softlab.sirh.jobOffer;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.softlab.sirh.candidate.Candidate;
import mg.softlab.sirh.interview.Interview;
import mg.softlab.sirh.job.Job;

import javax.persistence.*;
import java.util.Collection;

@Entity(name = "job_offer")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JobOffer {
    @Id
    @SequenceGenerator(
            name = "sequence_job_offer",
            sequenceName = "sequence_job_offer"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequence_job_offer"
    )
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 800)
    private String description;

    @org.springframework.data.annotation.Transient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    @JsonBackReference("job_offer")
    private Job job;

    @Column(nullable = false)
    private Boolean isDone;

    @org.springframework.data.annotation.Transient
    @OneToMany(mappedBy = "jobOffer")
    @JsonManagedReference("job_offer_candidate")
    private Collection<Candidate> candidates;

    @org.springframework.data.annotation.Transient
    @OneToMany(mappedBy = "jobOffer")
    @JsonManagedReference("job_offer_interview")
    private Collection<Interview> interviews;

    @Override
    public String toString() {
        return "JobOffer{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", isDone=" + isDone +
                '}';
    }
}
