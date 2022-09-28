package mg.softlab.sirh.department;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
}
