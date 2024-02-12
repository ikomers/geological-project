package natlex.example.geologicalproject.data.mapper;

import lombok.RequiredArgsConstructor;
import natlex.example.geologicalproject.data.dtos.SectionDto;
import natlex.example.geologicalproject.data.entity.Section;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SectionMapper {
    private final GeologicalClassMapper geologicalClassMapper;

    public Section toEntity(SectionDto sectionDto) {
        Section section = new Section();
        section.setName(sectionDto.getName());
        return section;
    }

    public SectionDto toDto(Section section) {
        SectionDto sectionDto = new SectionDto();
        sectionDto.setName(section.getName());
        sectionDto.setGeologicalClasses(section
                .getGeologicalClasses()
                .stream()
                .map(geologicalClassMapper::toDto)
                .collect(Collectors.toList()));
        return sectionDto;
    }
}
