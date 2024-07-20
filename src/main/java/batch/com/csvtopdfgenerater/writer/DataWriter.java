package batch.com.csvtopdfgenerater.writer;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import batch.com.csvtopdfgenerater.entity.PatientReport;
import batch.com.csvtopdfgenerater.repository.ReportRepository;

@Component
public class DataWriter implements ItemWriter<PatientReport> {

	private final ReportRepository personRepository;

	public DataWriter(ReportRepository personRepository) {
		this.personRepository = personRepository;
	}

	@Override
	public void write(List<? extends PatientReport> persons) throws Exception {
		personRepository.saveAll(persons);

	}

	@Bean
	public ItemWriter<PatientReport> pdfWriter() {
		return new ItemWriter<PatientReport>() {
			@Override
			public void write(List<? extends PatientReport> items) throws Exception {
				File outputDir = new File("OUTPUT");
				if (!outputDir.exists()) {
					outputDir.mkdirs();
				}
				Document document = new Document();
				String outputPath = "OUTPUT/report.pdf";
				PdfWriter.getInstance(document, new FileOutputStream(outputPath));
				document.open();
				for (PatientReport person : items) {
					document.add(new Paragraph(person.toString()));
				}
				document.close();

			}
		};
	}

}
