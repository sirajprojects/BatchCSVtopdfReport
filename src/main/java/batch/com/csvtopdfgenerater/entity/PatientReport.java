package batch.com.csvtopdfgenerater.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "patient_report")
public class PatientReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	private String patientId;
	private String patientName;
	private String dateOfBirth;
	private String gender;
	private String reportId;
	private String dateOfReport;
	private String specimenType;
	private String specimenCollectionDate;
	private String diagnosis;
	private String microscopicDescription;
	private String pathologistName;
	private String comments;

	// Getters and setters
	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	

	public String getSpecimenType() {
		return specimenType;
	}

	public void setSpecimenType(String specimenType) {
		this.specimenType = specimenType;
	}

	

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getDateOfReport() {
		return dateOfReport;
	}

	public void setDateOfReport(String dateOfReport) {
		this.dateOfReport = dateOfReport;
	}

	public String getSpecimenCollectionDate() {
		return specimenCollectionDate;
	}

	public void setSpecimenCollectionDate(String specimenCollectionDate) {
		this.specimenCollectionDate = specimenCollectionDate;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public String getMicroscopicDescription() {
		return microscopicDescription;
	}

	public void setMicroscopicDescription(String microscopicDescription) {
		this.microscopicDescription = microscopicDescription;
	}

	public String getPathologistName() {
		return pathologistName;
	}

	public void setPathologistName(String pathologistName) {
		this.pathologistName = pathologistName;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@Override
	public String toString() {
		return "PatientReport{" + "patientId='" + patientId + '\'' + ", patientName='" + patientName + '\''
				+ ", dateOfBirth=" + dateOfBirth + ", gender='" + gender + '\'' + ", reportId='" + reportId + '\''
				+ ", dateOfReport=" + dateOfReport + ", specimenType='" + specimenType + '\''
				+ ", specimenCollectionDate=" + specimenCollectionDate + ", diagnosis='" + diagnosis + '\''
				+ ", microscopicDescription='" + microscopicDescription + '\'' + ", pathologistName='" + pathologistName
				+ '\'' + ", comments='" + comments + '\'' + '}';
	}
}
