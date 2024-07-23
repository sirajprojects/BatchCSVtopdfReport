package batch.com.csvtopdfgenerater.writer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.batch.item.ItemWriter;

import com.itextpdf.text.DocumentException;

import batch.com.csvtopdfgenerater.entity.PatientReport;

public class PdfItemWriter implements ItemWriter<PatientReport> {

    private static final String OUTPUT_PATH = "OUTPUT/";
    private final ReportTemplate pdfReportGenerator;

    public PdfItemWriter() {
        this.pdfReportGenerator = new ReportTemplate();
    }

    @Override
    public void write(List<? extends PatientReport> items) throws Exception {
        // Group items by report ID
        Map<String, List<PatientReport>> groupedReports = items.stream()
                .collect(Collectors.groupingBy(PatientReport::getReportId));

        // Process each group and create a ZIP file
        for (Map.Entry<String, List<PatientReport>> entry : groupedReports.entrySet()) {
            String reportId = entry.getKey();
            List<PatientReport> reports = entry.getValue();

            // Create a ZIP file for each report ID
            File zipFile = new File(OUTPUT_PATH + "reports_" + reportId + "_" + System.currentTimeMillis() + ".zip");
            try (FileOutputStream fos = new FileOutputStream(zipFile);
                 ZipOutputStream zos = new ZipOutputStream(fos)) {

                // Create a PDF file for each report and add to ZIP
                for (PatientReport report : reports) {
                    // Use a UUID to ensure unique file names
                    String pdfFileName = "report_" + UUID.randomUUID().toString() + ".pdf";
                    File pdfFile = new File(OUTPUT_PATH + pdfFileName);

                    try {
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
                        }

                        // Optionally delete the PDF file after adding it to the ZIP
                        if (!pdfFile.delete()) {
                            System.err.println("Failed to delete temporary PDF file: " + pdfFile.getAbsolutePath());
                        }
                    } catch (DocumentException | IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException("Error creating PDF or adding to ZIP", e);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Error creating ZIP file", e);
            }
        }
    }
}
