package natlex.example.geologicalproject.service;

import natlex.example.geologicalproject.data.entity.GeologicalClass;
import natlex.example.geologicalproject.exceptions.NotFoundException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Disabled
public class GeologicalClassServiceTests {
    @Autowired
    private GeologicalClassService geologicalClassService;
    @Test
    public void testGeologicalClassServiceFindAll() {
        List<GeologicalClass> allGeologicalClasses = geologicalClassService.findAll();
        assertNotNull(allGeologicalClasses);
        assertFalse(allGeologicalClasses.isEmpty());
    }

    @Test
    public void testGeologicalClassServiceFindById() {
        Long geologicalClassId = 1L;
        GeologicalClass foundGeologicalClass = geologicalClassService.findById(geologicalClassId);
        assertNotNull(foundGeologicalClass);
        assertEquals(geologicalClassId, foundGeologicalClass.getId());
    }

    @Test
    public void testGeologicalClassServiceSave() {
        GeologicalClass newGeologicalClass = new GeologicalClass();
        newGeologicalClass.setName("Test GeologicalClass");
        newGeologicalClass.setCode("GC123");
        GeologicalClass savedGeologicalClass = geologicalClassService.save(newGeologicalClass);
        assertNotNull(savedGeologicalClass);
        assertNotNull(savedGeologicalClass.getId());
        assertEquals(newGeologicalClass.getName(), savedGeologicalClass.getName());
    }

    @Test
    public void testGeologicalClassServiceUpdate() {
        // Save
        GeologicalClass newGeologicalClass = new GeologicalClass();
        newGeologicalClass.setName("Test GeologicalClass");
        newGeologicalClass.setCode("GC123");
        GeologicalClass savedGeologicalClass = geologicalClassService.save(newGeologicalClass);

        // Update
        String updatedGeologicalClassName = "Updated GeologicalClass";
        savedGeologicalClass.setName(updatedGeologicalClassName);
        geologicalClassService.update(savedGeologicalClass.getId(), savedGeologicalClass);
        GeologicalClass updatedGeologicalClass = geologicalClassService.findById(savedGeologicalClass.getId());
        assertEquals(updatedGeologicalClassName, updatedGeologicalClass.getName());
    }

    @Test
    public void testGeologicalClassServiceDelete() {
        // Save

        GeologicalClass newGeologicalClass = new GeologicalClass();
        newGeologicalClass.setName("Test GeologicalClass");
        newGeologicalClass.setCode("GC123");
        GeologicalClass savedGeologicalClass = geologicalClassService.save(newGeologicalClass);

        // Delete
        geologicalClassService.delete(savedGeologicalClass.getId());
        assertThrows(NotFoundException.class, () -> geologicalClassService.findById(savedGeologicalClass.getId()));
    }

}
