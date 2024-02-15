package natlex.example.geologicalproject.service;

import natlex.example.geologicalproject.data.entity.Section;
import natlex.example.geologicalproject.exceptions.NotFoundException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Disabled
public class SectionServiceTests {
    @Autowired
    private SectionService sectionService;

    @Test
    public void testSectionServiceFindAll() {
        List<Section> allSections = sectionService.findAll();
        assertNotNull(allSections);
        assertFalse(allSections.isEmpty());
    }

    @Test
    public void testSectionServiceFindById() {
        Long sectionId = 1L;
        Section foundSection = sectionService.findById(sectionId);
        assertNotNull(foundSection);
        assertEquals(sectionId, foundSection.getId());
    }

    @Test
    public void testSectionServiceSave() {
        Section newSection = new Section();
        newSection.setName("Test Section");
        sectionService.save(newSection);
        Section savedSection = sectionService.findById(newSection.getId());
        assertNotNull(savedSection);
        assertEquals(newSection.getName(), savedSection.getName());
    }

    @Test
    public void testSectionServiceUpdate() {
        // Save
        Section newSection = new Section();
        newSection.setName("Test Section");
        sectionService.save(newSection);
        Section savedSection = sectionService.findById(newSection.getId());

        // Update
        String updatedSectionName = "Updated Section";
        savedSection.setName(updatedSectionName);
        sectionService.update(savedSection.getId(), savedSection);
        Section updatedSection = sectionService.findById(savedSection.getId());
        assertEquals(updatedSectionName, updatedSection.getName());
    }

    @Test
    public void testSectionServiceDelete() {
        // Save
        Section newSection = new Section();
        newSection.setName("Test Section");
        sectionService.save(newSection);
        Section savedSection = sectionService.findById(newSection.getId());

        // Delete
        sectionService.delete(savedSection.getId());
        assertThrows(NotFoundException.class, () -> sectionService.findById(savedSection.getId()));
    }
}
