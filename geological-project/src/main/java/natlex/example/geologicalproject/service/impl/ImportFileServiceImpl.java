package natlex.example.geologicalproject.service.impl;

import lombok.RequiredArgsConstructor;
import natlex.example.geologicalproject.data.entity.GeologicalClass;
import natlex.example.geologicalproject.data.entity.JobResult;
import natlex.example.geologicalproject.data.entity.JobStatus;
import natlex.example.geologicalproject.data.entity.Section;
import natlex.example.geologicalproject.service.ImportFileService;
import natlex.example.geologicalproject.service.JobResultService;
import natlex.example.geologicalproject.service.SectionService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ImportFileServiceImpl implements ImportFileService {
    private final SectionService sectionService;
    private final JobResultService jobResultService;

    @Override
    @Async
    public CompletableFuture<JobResult> importAsync(MultipartFile file) {
        return CompletableFuture.supplyAsync(() -> {
            JobResult jobResult = jobResultService.create();
            importData(file, jobResult);
            return jobResult;
        });
    }

    @Override
    public void importData(MultipartFile file, JobResult jobResult) {
        try (Workbook workbook = new HSSFWorkbook(file.getInputStream())) {
            List<Section> sections = processImportedData(workbook);
            sectionService.saveAll(sections);
            jobResult.setStatus(JobStatus.DONE);
            jobResult.setResult("Import successful");
            jobResultService.save(jobResult);
        } catch (Exception e) {
            jobResult.setStatus(JobStatus.ERROR);
            jobResult.setResult("Error during import: " + e.getMessage());
        } finally {
            jobResultService.save(jobResult);
        }
    }

    @Override
    public JobResult getImportStatus(Long id) {
        return jobResultService.findById(id);
    }

    private List<Section> processImportedData(Workbook workbook) {
        List<Section> sections = new ArrayList<>();
        for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
            Sheet sheet = workbook.getSheetAt(sheetIndex);

            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                Cell sectionNameCell = row.getCell(0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);

                if (sectionNameCell.getCellType() == CellType.STRING) {
                    String sectionName = row.getCell(0).getStringCellValue();

                    String sectionNumber = extractSectionNumber(sectionName);

                    if (sectionNumber == null) {
                        continue;
                    }

                    List<GeologicalClass> geologicalClasses = new ArrayList<>();
                    for (int i = 1; i < row.getLastCellNum(); i += 2) {
                        String className = row.getCell(i).getStringCellValue();
                        String classCode = row.getCell(i + 1).getStringCellValue();

                        if (isValidData(sectionNumber, className, classCode)) {
                            GeologicalClass geologicalClass = new GeologicalClass();
                            geologicalClass.setName(className);
                            geologicalClass.setCode(classCode);

                            geologicalClasses.add(geologicalClass);
                        }
                    }

                    Section section = new Section();
                    section.setName(sectionName);
                    section.setGeologicalClasses(geologicalClasses);

                    sections.add(section);
                }
            }
        }
        return sections;
    }

    private String extractSectionNumber(String sectionName) {
        Pattern pattern = Pattern.compile("Section (\\d+)");
        Matcher matcher = pattern.matcher(sectionName);

        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private String extractClassNumber(String className) {
        Pattern pattern = Pattern.compile("Geo Class (\\d+)");
        Matcher matcher = pattern.matcher(className);

        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private boolean isValidClassNumber(String sectionNumber, String geologicalClassName, String geologicalClassNumber) {
        String classPart = geologicalClassNumber.substring(sectionNumber.length());

        String resultString = "Geo Class " + sectionNumber + classPart;
        return geologicalClassName.equals(resultString);
    }

    private boolean isValidClassCode(String sectionNumber, String geologicalClassNumber, String geologicalClassCode) {
        String classCodePart = geologicalClassNumber.substring(sectionNumber.length());
        String expectedCode = "GC" + sectionNumber + classCodePart;
        return geologicalClassCode.equals(expectedCode);
    }

    private boolean isValidData(String sectionNumber, String geologicalClassName, String geologicalClassCode) {
        String geologicalClassNumber = extractClassNumber(geologicalClassName);

        if (geologicalClassName == null || geologicalClassCode == null || geologicalClassNumber == null) {
            return false;
        }

        if (!isValidClassNumber(sectionNumber, geologicalClassName, geologicalClassNumber)) {
            return false;
        }

        return isValidClassCode(sectionNumber, geologicalClassNumber, geologicalClassCode);
    }
}
