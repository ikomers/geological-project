package natlex.example.geologicalproject.data.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class SectionDto {
    @NotBlank
    private String name;
    private List<GeologicalClassDto> geologicalClasses;
}