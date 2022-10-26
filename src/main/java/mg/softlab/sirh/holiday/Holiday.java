package mg.softlab.sirh.holiday;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Entité contenant les jours feriés
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Holiday {
    @Id
    @SequenceGenerator(
            name = "sequence_holiday",
            sequenceName = "sequence_holiday",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequence_holiday"
    )
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @JsonProperty("date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
}
