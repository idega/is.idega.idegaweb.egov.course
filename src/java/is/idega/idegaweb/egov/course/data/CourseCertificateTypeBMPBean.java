package is.idega.idegaweb.egov.course.data;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDOQuery;

public class CourseCertificateTypeBMPBean extends GenericEntity implements
		CourseCertificateType {

	private static final long serialVersionUID = -3694212604080565748L;
	
	private static final String COLUMN_CERTIFICATE_TYPE = "certificate_type";
	private static final String COLUMN_DESCRIPTION = "description";

	public static final Integer FULL_CERTIFICATE_TYPE = new Integer(1);
	public static final Integer TEMPORARY_CERTIFICATE_TYPE = new Integer(2);
	
	public String getEntityName() {
		return "COU_CERTIFICATE_TYPE";
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		
		addAttribute(COLUMN_CERTIFICATE_TYPE, "Certificate type", Integer.class);
		addAttribute(COLUMN_DESCRIPTION, "Description", String.class);
	}
	
	public void insertStartData() throws Exception {
		CourseCertificateType fullCertificate = ((CourseCertificateTypeHome) IDOLookup.getHomeLegacy(CourseCertificateType.class)).create();
		fullCertificate.setType(FULL_CERTIFICATE_TYPE);
		fullCertificate.setDescription("Full certificate");
		fullCertificate.store();
		
		CourseCertificateType temporaryCertificate = ((CourseCertificateTypeHome) IDOLookup.getHomeLegacy(CourseCertificateType.class)).create();
		temporaryCertificate.setType(TEMPORARY_CERTIFICATE_TYPE);
		temporaryCertificate.setDescription("Temporary certificate");
		temporaryCertificate.store();
		
		//	TODO: add 3 more types (2, 3, 4, VOTT.xml)
	}
	
	public void setType(Integer type) {
		setColumn(COLUMN_CERTIFICATE_TYPE, type);
	}
	
	public Integer getType() {
		return getIntegerColumnValue(COLUMN_CERTIFICATE_TYPE);
	}
	
	public void setDescription(String description) {
		setColumn(COLUMN_DESCRIPTION, description);
	}
	
	public String getDescription() {
		return getStringColumnValue(COLUMN_DESCRIPTION);
	}
	
	public CourseCertificateType findByType(Integer type) {
		if (type == null) {
			return null;
		}
		
		IDOQuery query = idoQuery();
		query.append("select * from ").append(getEntityName()).appendWhereEquals(COLUMN_CERTIFICATE_TYPE, type);
		
		try {
			Object record = idoFindOnePKByQuery(query);
			if (record instanceof Integer) {
				try {
					return (CourseCertificateType) getIDOHome(CourseCertificateType.class).findByPrimaryKeyIDO(record);
				} catch (IDOLookupException e) {
					e.printStackTrace();
					return null;
				}
			}
			return null;
		} catch (FinderException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
