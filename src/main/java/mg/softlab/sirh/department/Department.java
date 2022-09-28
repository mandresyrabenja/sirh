package mg.softlab.sirh.department;

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
public class Department {
    @Id
    @SequenceGenerator(
            name = "sequence_department",
            sequenceName = "sequence_department"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequence_department"
    )
    private Long id;

    @Column(nullable = false)
    private String name;

    @org.springframework.data.annotation.Transient
    @OneToMany(mappedBy = "department")
    @JsonManagedReference("job_department")
    private Collection<Job> jobs;
}
