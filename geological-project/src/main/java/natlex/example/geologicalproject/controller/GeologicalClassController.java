package natlex.example.geologicalproject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import natlex.example.geologicalproject.data.RestResponse;
import natlex.example.geologicalproject.data.entity.GeologicalClass;
import natlex.example.geologicalproject.service.GeologicalClassService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/geological-classes")
@RequiredArgsConstructor
@Slf4j
public class GeologicalClassController {

    private final GeologicalClassService geologicalClassService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RestResponse getGeologicalClassById(@PathVariable Long id) {
        log.info("Received request to get GeologicalClass by ID: {}", id);
        GeologicalClass geologicalClass = geologicalClassService.getGeologicalClassById(id);
        return new RestResponse(geologicalClass);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public RestResponse getAllGeologicalClasses() {
        log.info("Received request to get all GeologicalClasses");
        List<GeologicalClass> geologicalClasses = geologicalClassService.getAllGeologicalClasses();
        return new RestResponse(geologicalClasses);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestResponse createGeologicalClass(@RequestBody GeologicalClass geologicalClass) {
        log.info("Received request to create GeologicalClass: {}", geologicalClass);
        GeologicalClass createdGeologicalClass = geologicalClassService.createGeologicalClass(geologicalClass);
        return new RestResponse(createdGeologicalClass);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RestResponse updateGeologicalClass(@PathVariable Long id, @RequestBody GeologicalClass updatedGeologicalClass) {
        log.info("Received request to update GeologicalClass with ID {}: {}", id, updatedGeologicalClass);
        GeologicalClass updatedClass = geologicalClassService.updateGeologicalClass(id, updatedGeologicalClass);
        return new RestResponse(updatedClass);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RestResponse deleteGeologicalClass(@PathVariable Long id) {
        log.info("Received request to delete GeologicalClass with ID: {}", id);
        geologicalClassService.deleteGeologicalClass(id);
        return new RestResponse("GeologicalClass with ID " + id + " deleted successfully.");
    }

}

