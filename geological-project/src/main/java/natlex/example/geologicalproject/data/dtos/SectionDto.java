package natlex.example.geologicalproject.data.dtos;

import lombok.Data;

import java.util.List;

@Data
public class SectionDto {
    private String name;
    private List<GeologicalClassDto> geologicalClasses;
}