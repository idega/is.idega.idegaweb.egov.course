package is.idega.idegaweb.egov.course.data;


import com.idega.data.IDOLegacyEntity;

public interface CourseCertificateType extends IDOLegacyEntity {
	
	public void setType(Integer type);
	
	public Integer getType();
	
	public void setDescription(String description);
	
	public String getDescription();
	
	public CourseCertificateType findByType(Integer type);
}