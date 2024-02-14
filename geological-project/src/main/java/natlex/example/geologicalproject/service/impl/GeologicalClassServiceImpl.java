package natlex.example.geologicalproject.service.impl;

import lombok.RequiredArgsConstructor;
import natlex.example.geologicalproject.data.entity.GeologicalClass;
import natlex.example.geologicalproject.exceptions.NotFoundException;
import natlex.example.geologicalproject.repositories.GeologicalClassRepository;
import natlex.example.geologicalproject.service.GeologicalClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GeologicalClassServiceImpl implements GeologicalClassService {
    private final GeologicalClassRepository geologicalClassRepository;

    @Override
    public List<GeologicalClass> findAll() {
        return geologicalClassRepository.findAll();
    }

    @Override
    public GeologicalClass findById(Long id) {
        return geologicalClassRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("GeologicalClass not found with id: " + id));
    }

    @Override
    public GeologicalClass save(GeologicalClass geologicalClass) {
        return geologicalClassRepository.save(geologicalClass);
    }

    @Transactional (propagation = Propagation.NESTED)
    @Override
    public void saveAll(List<GeologicalClass> geologicalClasses) {
        geologicalClassRepository.saveAll(geologicalClasses);
    }

    @Override
    public void update(Long id, GeologicalClass updatedGeologicalClass) {
        GeologicalClass existingGeologicalClass = findById(id);

        existingGeologicalClass.setName(updatedGeologicalClass.getName());
        existingGeologicalClass.setCode(updatedGeologicalClass.getCode());

        geologicalClassRepository.save(existingGeologicalClass);
    }

    @Override
    public void delete(Long id) {
        GeologicalClass geologicalClass = this.findById(id);
        geologicalClassRepository.delete(geologicalClass);
    }
}
