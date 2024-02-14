package natlex.example.geologicalproject.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import natlex.example.geologicalproject.data.entity.GeologicalClass;
import natlex.example.geologicalproject.data.entity.JobResult;
import natlex.example.geologicalproject.data.entity.JobStatus;
import natlex.example.geologicalproject.data.entity.Section;
import natlex.example.geologicalproject.service.ImportExportService;
import natlex.example.geologicalproject.service.JobResultService;
import natlex.example.geologicalproject.service.SectionService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ImportExportServiceImpl implements ImportExportService {
    private final JobResultService jobResultService;
    private final SectionService sectionService;
    private final ApplicationContext applicationContext;
    private ImportExportService importExportService;

    @PostConstruct
    public void init() {
        importExportService = applicationContext.getBean(ImportExportService.class);
    }

    public JobResult importAsync(MultipartFile file) {
        JobResult jobResult = jobResultService.create();
        importExportService.importData(file, jobResult);
        return jobResult;
    }

    @Transactional
    @Async
    public void importData(MultipartFile file, JobResult jobResult) {
        try (Workbook workbook = new HSSFWorkbook(file.getInputStream())) {
            List<Section> sections = processImportedData(workbook);
            sectionService.saveAll(sections);
            jobResult.setStatus(JobStatus.DONE);
            jobResult.setResult("Import successful");
            jobResultService.save(jobResult);
        } catch (Exception e) {
            // to check with exitis names
            jobResult.setStatus(JobStatus.ERROR);
            jobResult.setResult("Error during import: " + e.getMessage());
        } finally {
            jobResultService.save(jobResult);
        }
    }

    private List<Section> processImportedData(Workbook workbook) {
        List<Section> sections = new ArrayList<>();
        for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
            Sheet sheet = workbook.getSheetAt(sheetIndex);

            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);

                if (row != null) {
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

    @Async
    @Transactional
    public CompletableFuture<JobResult> exportAsync() {
        List<Section> sections = sectionService.findAll();

        JobResult jobEntity = new JobResult();
        jobEntity.setStatus(JobStatus.IN_PROGRESS);
        jobResultService.save(jobEntity);

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
            jobResultService.save(jobEntity);
        }

        return CompletableFuture.completedFuture(jobEntity);
    }
}
