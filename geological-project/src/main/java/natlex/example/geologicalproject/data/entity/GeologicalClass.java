package natlex.example.geologicalproject.data.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString(exclude = "section_id")
@Table(name = "geological_classes")
@Where(clause = "is_deleted = false")
public class GeologicalClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint")
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @CreationTimestamp
    private LocalDateTime creationDate;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(
            name = "section_id",
            nullable = false)
    private Section section;
}