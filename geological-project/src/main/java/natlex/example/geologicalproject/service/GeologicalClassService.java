package natlex.example.geologicalproject.service;

import natlex.example.geologicalproject.data.entity.GeologicalClass;

import java.util.List;

public interface GeologicalClassService {
    List<GeologicalClass> getAllGeologicalClasses();
    GeologicalClass getGeologicalClassById(Long id);
    GeologicalClass createGeologicalClass(GeologicalClass geologicalClass);
    GeologicalClass updateGeologicalClass(Long id, GeologicalClass updatedGeologicalClass);
    void deleteGeologicalClass(Long id);
}
