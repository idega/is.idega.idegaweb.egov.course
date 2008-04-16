package is.idega.idegaweb.egov.course.data;


import java.sql.Timestamp;

import com.idega.company.data.Company;
import com.idega.core.file.data.ICFile;
import com.idega.data.IDOEntity;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

public interface CourseCertificate extends IDOEntity {
	
	public void setCertificateType(CourseCertificateType type);
	
	public CourseCertificateType getCourseCertificateType();
	
	public void setParticipant(User participant);

	public User getParticipant();
	
	public void setCompany(Company company);
	
	public Company getCompany();
	
	public void setCourse(Course course);
	
	public Course getCourse();
	
	public void setValidFrom(Timestamp validFrom);
	
	public IWTimestamp getValidFrom();
	
	public void setValidThru(Timestamp validThru);
	
	public IWTimestamp getValidThru();
	
	public void setCertificateFile(ICFile file);
	
	public ICFile getCertificateFile();
	
}