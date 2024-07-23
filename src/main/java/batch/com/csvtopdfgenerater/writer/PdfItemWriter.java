package batch.com.csvtopdfgenerater.writer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.batch.item.ItemWriter;

import batch.com.csvtopdfgenerater.entity.PatientReport;

public class PdfItemWriter implements ItemWriter<PatientReport> {

    private static final String OUTPUT_PATH = "OUTPUT/";
    private final ReportTemplate pdfReportGenerator;
    private final Set<String> createdZipFiles;

    public PdfItemWriter() {
        this.pdfReportGenerator = new ReportTemplate();
        this.createdZipFiles = new HashSet<>();
    }

    @Override
    public void write(List<? extends PatientReport> items) throws Exception {
        // Group items by patient ID
        Map<String, List<PatientReport>> groupedReports = items.stream()
                .collect(Collectors.groupingBy(PatientReport::getPatientId));

        // Define timestamp formatter with specified format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

        // Create output directory if it doesn't exist
        File outputDir = new File(OUTPUT_PATH);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        // Process each group and create a ZIP file
        for (Map.Entry<String, List<PatientReport>> entry : groupedReports.entrySet()) {
            String patientId = entry.getKey();
            List<PatientReport> reports = entry.getValue();

            // Generate ZIP file name and check if it already exists
            String timestamp = LocalDateTime.now().format(formatter); // Get current timestamp
            String zipFileName = "patient_" + patientId + "_" + timestamp + ".zip";
            File zipFile = new File(OUTPUT_PATH + zipFileName);

            if (createdZipFiles.contains(zipFileName)) {
                System.out.println("Skipping ZIP file creation for patient ID " + patientId + " as it already exists.");
                continue;
            }

            // Add the zip file name to the set of created files
            createdZipFiles.add(zipFileName);

            try (FileOutputStream fos = new FileOutputStream(zipFile);
                 ZipOutputStream zos = new ZipOutputStream(fos)) {

                // Create a PDF file for each report and add it to the ZIP
                for (PatientReport report : reports) {
                    // Use a UUID to ensure unique file names
                    String pdfFileName = "report_" + UUID.randomUUID().toString() + ".pdf";
                    File pdfFile = new File(OUTPUT_PATH + pdfFileName);

                    try {
                        // Generate PDF using ReportTemplate
                        pdfReportGenerator.generatePdf(List.of(report), pdfFile.getAbsolutePath());

                        // Add PDF file to the ZIP
                        try (FileInputStream fis = new FileInputStream(pdfFile)) {
                            ZipEntry zipEntry = new ZipEntry(pdfFileName);
                            zos.putNextEntry(zipEntry);

                            byte[] buffer = new byte[1024];
                            int len;
                            while ((len = fis.read(buffer)) > 0) {
                                zos.write(buffer, 0, len);
                            }

                            zos.closeEntry();
                        } catch (IOException e) {
                            e.printStackTrace();
                            throw new RuntimeException("Error reading PDF file or adding to ZIP", e);
                        } finally {
                            // Optionally delete the PDF file after adding it to the ZIP
                            if (!pdfFile.delete()) {
                                System.err.println("Failed to delete temporary PDF file: " + pdfFile.getAbsolutePath());
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException("Error creating PDF", e);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Error creating ZIP file", e);
            }
        }
    }
}
