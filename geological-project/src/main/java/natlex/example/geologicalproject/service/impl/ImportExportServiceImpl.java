package natlex.example.geologicalproject.service.impl;


import lombok.AllArgsConstructor;
import natlex.example.geologicalproject.data.entity.GeologicalClass;
import natlex.example.geologicalproject.data.entity.JobResult;
import natlex.example.geologicalproject.data.entity.JobStatus;
import natlex.example.geologicalproject.data.entity.Section;
import natlex.example.geologicalproject.repositories.JobResultRepository;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class ImportExportServiceImpl {
    private final JobResultRepository jobRepository;
    private final SectionServiceImpl sectionService;

    @Async
    @Transactional
    public CompletableFuture<JobResult> importAsync(MultipartFile file) {
        JobResult jobEntity = new JobResult();
        jobEntity.setStatus(JobStatus.IN_PROGRESS);
        jobRepository.save(jobEntity);

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            jobEntity.setStatus(JobStatus.DONE);
        } catch (Exception e) {
            jobEntity.setStatus(JobStatus.ERROR);
            jobEntity.setResult("Error during import: " + e.getMessage());
        } finally {
            jobRepository.save(jobEntity);
        }

        return CompletableFuture.completedFuture(jobEntity);
    }

    @Async
    @Transactional
    public CompletableFuture<JobResult> exportAsync() {
        List<Section> sections = sectionService.findAll();
        JobResult jobEntity = new JobResult();
        jobEntity.setStatus(JobStatus.IN_PROGRESS);
        jobRepository.save(jobEntity);

        try (Workbook workbook = new HSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("list1");

            // header
            Row headerRow = sheet.createRow(0);
            Cell sectionHeader = headerRow.createCell(0);
            sectionHeader.setCellValue("Section");

            int maxClassCount = sections.stream()
                    .mapToInt(section -> section.getGeologicalClasses().size())
                    .max()
                    .orElse(0);

            // Headers for GC
            for (int i = 1; i <= maxClassCount; i++) {
                Cell classNameHeader = headerRow.createCell(i * 2 - 1);
                classNameHeader.setCellValue("Class " + i + " Name");

                Cell classCodeHeader = headerRow.createCell(i * 2);
                classCodeHeader.setCellValue("Class " + i + " Code");
            }

            // Fill data
            int rowCount = 1;
            for (Section section : sections) {
                Row row = sheet.createRow(rowCount++);

                // Fill sections
                Cell sectionCell = row.createCell(0);
                sectionCell.setCellValue(section.getName());

                // Fill GCs
                List<GeologicalClass> geologicalClasses = section.getGeologicalClasses();
                for (int i = 0; i < geologicalClasses.size(); i++) {
                    GeologicalClass geologicalClass = geologicalClasses.get(i);


                    int classColumnIndex = 1 + i * 2;

                    // Fill GC name
                    Cell classNameCell = row.createCell(classColumnIndex);
                    classNameCell.setCellValue(geologicalClass.getName() + " " + (i + 1));

                    // Fill GC code
                    Cell classCodeCell = row.createCell(classColumnIndex + 1);
                    classCodeCell.setCellValue(geologicalClass.getCode() + " " + (i + 1));
                }
            }

            // saving
            try (FileOutputStream fileOut = new FileOutputStream("geological_data.xls")) {
                workbook.write(fileOut);
            } catch (IOException e) {
                jobEntity.setStatus(JobStatus.ERROR);
                jobEntity.setResult("Error during export: " + e.getMessage());
            }

            jobEntity.setStatus(JobStatus.DONE);
        } catch (Exception e) {
            jobEntity.setStatus(JobStatus.ERROR);
            jobEntity.setResult("Error during export: " + e.getMessage());
        } finally {
            jobRepository.save(jobEntity);
        }

        return CompletableFuture.completedFuture(jobEntity);
    }
}