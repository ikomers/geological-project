package natlex.example.geologicalproject.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "sections")
@Where(clause = "is_deleted = false")
public class Section implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;
    @Column(name = "is_deleted")
    private boolean isDeleted;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "section", orphanRemoval = true)
    private List<GeologicalClass> geologicalClasses = new ArrayList<>();
}
