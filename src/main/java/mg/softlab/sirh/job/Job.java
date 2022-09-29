package mg.softlab.sirh.job;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.softlab.sirh.department.Department;
import mg.softlab.sirh.jobCategory.JobCategory;
import mg.softlab.sirh.jobOffer.JobOffer;

import javax.persistence.*;
import java.util.Collection;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Job {
    @Id
    @SequenceGenerator(
            name = "sequence_job",
            sequenceName = "sequence_job"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequence_job"
    )
    private Long id;

    @Column(nullable = false)
    private String name;

    @org.springframework.data.annotation.Transient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_category_id")
    @JsonBackReference("job_category")
    private JobCategory jobCategory;

    @org.springframework.data.annotation.Transient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    @JsonBackReference("job_department")
    private Department department;

    @org.springframework.data.annotation.Transient
    @OneToMany(mappedBy = "job")
    @JsonManagedReference("job_offer")
    private Collection<JobOffer> jobOffers;
}
