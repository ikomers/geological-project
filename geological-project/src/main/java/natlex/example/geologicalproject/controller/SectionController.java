package natlex.example.geologicalproject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import natlex.example.geologicalproject.data.RestResponse;
import natlex.example.geologicalproject.data.entity.GeologicalClass;
import natlex.example.geologicalproject.data.entity.Section;
import natlex.example.geologicalproject.service.SectionService;
import natlex.example.geologicalproject.service.TokenValidationService;
import natlex.example.geologicalproject.service.impl.GeologicalClassServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sections")
@RequiredArgsConstructor
@Slf4j
public class SectionController {
    private final SectionService sectionService;
    private final TokenValidationService tokenValidationService;
    private final GeologicalClassServiceImpl geologicalClassService;

    @Value("${auth.token}")
    private String token;
    @Value("${auth.header.name}")
    private String header;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public RestResponse getAllSections(@RequestHeader(name = "X-Auth-Token") String token) {
        tokenValidationService.validateToken(token);
        log.info("Received request to get all sections");
        List<Section> sections = sectionService.getAllSections();
        return new RestResponse(sections);
    }

    @GetMapping("/{sectionId}")
    @ResponseStatus(HttpStatus.OK)
    public RestResponse findById(@RequestHeader(name = "X-Auth-Token") String token, @PathVariable Long sectionId) {
        tokenValidationService.validateToken(token);
        log.info("Received request to get section by ID: {}", sectionId);
        Section section = sectionService.findById(sectionId);
        return new RestResponse(section);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestResponse createSection(@RequestHeader(name = "X-Auth-Token") String token, @RequestBody Section section) {
        tokenValidationService.validateToken(token);
        log.info("Received request to create a new section: {}", section);
        Section createdSection = sectionService.createSection(section);
       /* List<GeologicalClass> geologicalClasses = section.getGeologicalClasses();
        if (geologicalClasses != null) {
            geologicalClasses.forEach(geologicalClass -> geologicalClass.setSection(createdSection));
        }*/
        return new RestResponse(createdSection);
    }

    @PutMapping("/{sectionId}")
    @ResponseStatus(HttpStatus.OK)
    public RestResponse updateSection(@RequestHeader(name = "X-Auth-Token") String token,
                                      @PathVariable Long sectionId,
                                      @RequestBody Section section) {
        tokenValidationService.validateToken(token);
        log.info("Received request to update section with ID {}: {}", sectionId, section);
        section.setId(sectionId);

        Section updatedSection = sectionService.updateSection(sectionId, section);
        return new RestResponse(updatedSection);
    }

    @DeleteMapping("/{sectionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSection(@RequestHeader(name = "X-Auth-Token") String token, @PathVariable Long sectionId) {
        tokenValidationService.validateToken(token);
        log.info("Received request to delete section with ID: {}", sectionId);
        sectionService.deleteSection(sectionId);
    }
}