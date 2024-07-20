package batch.com.csvtopdfgenerater.writer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.batch.item.ItemWriter;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import batch.com.csvtopdfgenerater.entity.PatientReport;

public class PdfItemWriter implements ItemWriter<PatientReport> {

    private static final String OUTPUT_PATH = "OUTPUT/output.pdf";
    private static final Font TITLE_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD, BaseColor.BLUE);
    private static final Font SECTION_TITLE_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD, BaseColor.DARK_GRAY);
    private static final Font TEXT_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK);
    private static final BaseColor TABLE_HEADER_COLOR = new BaseColor(200, 200, 200);

    @Override
    public void write(List<? extends PatientReport> items) throws Exception {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(OUTPUT_PATH));
            document.open();
            for (PatientReport report : items) {
                addReportToDocument(document, report);
                document.newPage();  // Start a new page for each report
            }
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating PDF", e);
        } finally {
            document.close();
        }
    }

    private void addReportToDocument(Document document, PatientReport report) throws DocumentException {
        document.add(new Paragraph("Pathologist's Report", TITLE_FONT));
        document.add(new Paragraph(" ")); // Add an empty line

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // Adding patient information
        addCellToTable(table, "Patient Information", SECTION_TITLE_FONT, 2, TABLE_HEADER_COLOR);
        addCellToTable(table, "Patient ID: ", TEXT_FONT);
        addCellToTable(table, report.getPatientId(), TEXT_FONT);
        addCellToTable(table, "Patient Name: ", TEXT_FONT);
        addCellToTable(table, report.getPatientName(), TEXT_FONT);
        addCellToTable(table, "Date of Birth: ", TEXT_FONT);
        addCellToTable(table, report.getDateOfBirth(), TEXT_FONT);
        addCellToTable(table, "Gender: ", TEXT_FONT);
        addCellToTable(table, report.getGender(), TEXT_FONT);

        // Adding report details
        addCellToTable(table, "Report Details", SECTION_TITLE_FONT, 2, TABLE_HEADER_COLOR);
        addCellToTable(table, "Report ID: ", TEXT_FONT);
        addCellToTable(table, report.getReportId(), TEXT_FONT);
        addCellToTable(table, "Date of Report: ", TEXT_FONT);
        addCellToTable(table, report.getDateOfReport(), TEXT_FONT);
        addCellToTable(table, "Specimen Type: ", TEXT_FONT);
        addCellToTable(table, report.getSpecimenType(), TEXT_FONT);
        addCellToTable(table, "Specimen Collection Date: ", TEXT_FONT);
        addCellToTable(table, report.getSpecimenCollectionDate(), TEXT_FONT);

        document.add(table);

        // Adding diagnosis
        document.add(new Paragraph("Diagnosis", SECTION_TITLE_FONT));
        document.add(new Paragraph(report.getDiagnosis(), TEXT_FONT));
        document.add(new Paragraph(" ")); // Add an empty line

        // Adding microscopic description
        document.add(new Paragraph("Microscopic Description", SECTION_TITLE_FONT));
        document.add(new Paragraph(report.getMicroscopicDescription(), TEXT_FONT));
        document.add(new Paragraph(" ")); // Add an empty line

        // Adding pathologist information
        document.add(new Paragraph("Pathologist Information", SECTION_TITLE_FONT));
        document.add(new Paragraph("Pathologist Name: " + report.getPathologistName(), TEXT_FONT));
        document.add(new Paragraph(" ")); // Add an empty line

        // Adding comments
        document.add(new Paragraph("Comments", SECTION_TITLE_FONT));
        document.add(new Paragraph(report.getComments(), TEXT_FONT));
    }

    private void addCellToTable(PdfPTable table, String text, Font font) {
        addCellToTable(table, text, font, 1, BaseColor.WHITE);
    }

    private void addCellToTable(PdfPTable table, String text, Font font, int colspan, BaseColor backgroundColor) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setColspan(colspan);
        cell.setBackgroundColor(backgroundColor);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
    }
}
