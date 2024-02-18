package natlex.example.geologicalproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import natlex.example.geologicalproject.aspect.annotation.Authorized;
import natlex.example.geologicalproject.data.RestResponse;
import natlex.example.geologicalproject.data.dtos.SectionDto;
import natlex.example.geologicalproject.data.entity.Section;
import natlex.example.geologicalproject.data.mapper.SectionMapper;
import natlex.example.geologicalproject.service.SectionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sections")
@RequiredArgsConstructor
@Slf4j
public class SectionController {
    private final SectionService sectionService;
    private final SectionMapper sectionMapper;

    @Authorized
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public RestResponse getAllSections() {
        log.info("Received request to get all sections");
        List<Section> sections = sectionService.findAll();
        return new RestResponse(sections);
    }

    @Authorized
    @GetMapping("/{sectionId}")
    @ResponseStatus(HttpStatus.OK)
    public RestResponse findById(@PathVariable Long sectionId) {
        log.info("Received request to get section by ID: {}", sectionId);
        Section section = sectionService.findById(sectionId);
        return new RestResponse(sectionMapper.toDto(section));
    }

    @Authorized
    @GetMapping("/by-code")
    public RestResponse getSectionsByGeologicalClassCode(@RequestParam String code) {
        List<Section> sections = sectionService.getSectionsByGeologicalClassCode(code);
        return new RestResponse(sections);
    }

    @Authorized
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestResponse createSection(@RequestBody @Valid SectionDto sectionDto) {
        log.info("Received request to create a new section: {}", sectionDto);
        Section createdSection = sectionMapper.toEntity(sectionDto);
        sectionService.save(createdSection);
        return new RestResponse(sectionMapper.toDto(createdSection));
    }

    @Authorized
    @PutMapping("/{sectionId}")
    @ResponseStatus(HttpStatus.OK)
    public RestResponse updateSection(@PathVariable Long sectionId,
                                      @RequestBody @Valid SectionDto sectionDto) {
        log.info("Received request to update section with ID {}: {}", sectionId, sectionDto);
        sectionService.update(sectionId, sectionMapper.toEntity(sectionDto));
        return new RestResponse("updated");
    }

    @PatchMapping("/{sectionId}")
    @ResponseStatus(HttpStatus.OK)
    public RestResponse patchSection(@PathVariable Long sectionId,
                                     @RequestBody SectionDto sectionDto) {
        log.info("Received request to patch section with ID {}: {}", sectionId, sectionDto);
        sectionService.patch(sectionId, sectionDto);
        return new RestResponse("patched");
    }


    @Authorized
    @DeleteMapping("/{sectionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSection(@PathVariable Long sectionId) {
        log.info("Received request to delete section with ID: {}", sectionId);
        sectionService.delete(sectionId);
    }
}