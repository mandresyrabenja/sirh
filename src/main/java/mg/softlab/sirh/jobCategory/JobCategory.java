package mg.softlab.sirh.jobCategory;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.softlab.sirh.job.Job;

import javax.persistence.*;
import java.util.Collection;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JobCategory {
    @Id
    @SequenceGenerator(
            name = "sequence_jobcategory",
            sequenceName = "sequence_jobcategory"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequence_jobcategory"
    )
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double minSalary;

    @org.springframework.data.annotation.Transient
    @OneToMany(mappedBy = "jobCategory")
    @JsonManagedReference("job_category")
    private Collection<Job> jobs;
}
