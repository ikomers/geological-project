package natlex.example.geologicalproject.data.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class GeologicalClassDto {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Code is required")
    private String code;
    private Long sectionId;
}