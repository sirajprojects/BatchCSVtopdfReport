package batch.com.csvtopdfgenerater.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

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


}