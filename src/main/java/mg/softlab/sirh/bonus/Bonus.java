package mg.softlab.sirh.bonus;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.softlab.sirh.interview.Interview;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Bonus {
    @Id
    @SequenceGenerator(
            name = "sequence_bonus",
            sequenceName = "sequence_bonus"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequence_bonus"
    )
    private Long id;

    @org.springframework.data.annotation.Transient
    @OneToOne
    @JsonBackReference("interview_bonus")
    private Interview interview;

    @Column(nullable = false)
    private Short point;

    @Column(nullable = false)
    private String reason;
}
