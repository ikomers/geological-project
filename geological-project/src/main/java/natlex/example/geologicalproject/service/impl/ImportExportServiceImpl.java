package natlex.example.geologicalproject.service.impl;


import lombok.AllArgsConstructor;
import natlex.example.geologicalproject.data.dtos.GeologicalClassDto;
import natlex.example.geologicalproject.data.dtos.SectionDto;
import natlex.example.geologicalproject.data.entity.GeologicalClass;
import natlex.example.geologicalproject.data.entity.JobResult;
import natlex.example.geologicalproject.data.entity.JobStatus;
import natlex.example.geologicalproject.data.entity.Section;
import natlex.example.geologicalproject.data.mapper.SectionMapper;
import natlex.example.geologicalproject.repositories.JobResultRepository;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
@AllArgsConstructor
public class ImportExportServiceImpl {
    private final JobResultRepository jobRepository;
    private final SectionServiceImpl sectionService;
    private final SectionMapper sectionMapper;

    @Async
    @Transactional
    public CompletableFuture<JobResult> importAsync(MultipartFile file) {
        JobResult jobEntity = new JobResult();
        jobEntity.setStatus(JobStatus.IN_PROGRESS);
        jobRepository.save(jobEntity);

        try (Workbook workbook = new HSSFWorkbook(file.getInputStream())) {
            processImportedData(workbook, jobEntity);
        } catch (Exception e) {
            jobEntity.setStatus(JobStatus.ERROR);
            jobEntity.setResult("Error during import: " + e.getMessage());
        } finally {
            jobRepository.save(jobEntity);
        }

        return CompletableFuture.completedFuture(jobEntity);
    }

    private void processImportedData(Workbook workbook, JobResult jobEntity) {
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

                    List<GeologicalClassDto> geologicalClasses = new ArrayList<>();
                    for (int i = 1; i < row.getLastCellNum(); i += 2) {
                        String className = row.getCell(i).getStringCellValue();
                        String classCode = row.getCell(i + 1).getStringCellValue();
                        String classNumber = extractClassNumber(className);

                        if (isValidData(sectionNumber, className, classCode)) {
                            GeologicalClassDto geologicalClassDto = new GeologicalClassDto();
                            geologicalClassDto.setName(className);
                            geologicalClassDto.setCode(classCode);

                            geologicalClasses.add(geologicalClassDto);
                        }
                    }

                    SectionDto sectionDto = new SectionDto();
                    sectionDto.setName(sectionName);
                    sectionDto.setGeologicalClasses(geologicalClasses);

                    Section existingSection = sectionService.findByName(sectionName);
                    if (existingSection != null) {
                        sectionService.update(existingSection.getId(), sectionMapper.toEntity(sectionDto));
                    } else {
                        sectionService.save(sectionMapper.toEntity(sectionDto));
                    }
                }
            }
        }
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
