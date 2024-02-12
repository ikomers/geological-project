package natlex.example.geologicalproject.data.mapper;

import lombok.RequiredArgsConstructor;
import natlex.example.geologicalproject.data.dtos.GeologicalClassDto;
import natlex.example.geologicalproject.data.entity.GeologicalClass;
import natlex.example.geologicalproject.service.SectionService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GeologicalClassMapper {
    private final SectionService sectionService;
    public GeologicalClass toEntity(GeologicalClassDto geologicalClassDto) {
        GeologicalClass geologicalClass = new GeologicalClass();
        geologicalClass.setName(geologicalClassDto.getName());
        geologicalClass.setCode(geologicalClassDto.getCode());

        if (geologicalClassDto.getSectionId() != null) {
            geologicalClass.setSection(sectionService.findById(geologicalClassDto.getSectionId()));

        }
        return geologicalClass;
    }

    public GeologicalClassDto toDto(GeologicalClass geologicalClass) {
        GeologicalClassDto geologicalClassDto = new GeologicalClassDto();
        geologicalClassDto.setName(geologicalClass.getName());
        geologicalClassDto.setCode(geologicalClass.getCode());

        if (geologicalClass.getSection() != null) {
            geologicalClassDto.setSectionId(geologicalClass.getSection().getId());
        }
        return geologicalClassDto;
    }
}
