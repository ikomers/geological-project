package natlex.example.geologicalproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import natlex.example.geologicalproject.aspect.annotation.Authorized;
import natlex.example.geologicalproject.data.RestResponse;
import natlex.example.geologicalproject.data.dtos.GeologicalClassDto;
import natlex.example.geologicalproject.data.entity.GeologicalClass;
import natlex.example.geologicalproject.data.mapper.GeologicalClassMapper;
import natlex.example.geologicalproject.service.GeologicalClassService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/geological-classes")
@RequiredArgsConstructor
@Slf4j
@Authorized
public class GeologicalClassController {
    private final GeologicalClassMapper geologicalClassMapper;
    private final GeologicalClassService geologicalClassService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public RestResponse getAllGeologicalClasses() {
        log.info("Received request to get all GeologicalClasses");
        List<GeologicalClass> geologicalClasses = geologicalClassService.findAll();
        return new RestResponse(geologicalClasses);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RestResponse getGeologicalClassById(@PathVariable Long id) {
        log.info("Received request to get GeologicalClass by ID: {}", id);
        GeologicalClass geologicalClass = geologicalClassService.findById(id);
        return new RestResponse(geologicalClassMapper.toDto(geologicalClass));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestResponse createGeologicalClass(@RequestBody @Valid GeologicalClassDto geologicalClassDto) {
        log.info("Received request to create GeologicalClass: {}", geologicalClassDto);
        GeologicalClass geologicalClass = geologicalClassService.save(geologicalClassMapper.toEntity(geologicalClassDto));
        return new RestResponse(geologicalClassMapper.toDto(geologicalClass));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RestResponse updateGeologicalClass(@PathVariable Long id, @RequestBody @Valid GeologicalClassDto geologicalClassDto) {
        log.info("Received request to update GeologicalClass with ID {}: {}", id, geologicalClassDto);
        geologicalClassService.update(id, geologicalClassMapper.toEntity(geologicalClassDto));
        return new RestResponse("updated");
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RestResponse deleteGeologicalClass(@PathVariable Long id) {
        log.info("Received request to delete GeologicalClass with ID: {}", id);
        geologicalClassService.delete(id);
        return new RestResponse("GeologicalClass with ID " + id + " deleted successfully.");
    }

}
