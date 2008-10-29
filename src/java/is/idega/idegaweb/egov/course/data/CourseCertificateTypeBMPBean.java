package is.idega.idegaweb.egov.course.data;

import java.util.Collection;
import java.util.logging.Level;

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

	public static final Integer FULL_CERTIFICATE_TYPE = 1;
	public static final Integer RENEWAL_CERTIFICATE_TYPE = 2;
	public static final Integer TEMPORARY_CERTIFICATE_TYPE = 3;
	public static final Integer LIMITED_CERTIFICATE_TYPE = 4;
	
	@Override
	public String getEntityName() {
		return "COU_CERTIFICATE_TYPE";
	}

	@Override
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		
		addAttribute(COLUMN_CERTIFICATE_TYPE, "Certificate type", Integer.class);
		addAttribute(COLUMN_DESCRIPTION, "Description", String.class);
	}
	
	@Override
	public void insertStartData() throws Exception {
		//	1
		CourseCertificateType fullCertificate = ((CourseCertificateTypeHome) IDOLookup.getHomeLegacy(CourseCertificateType.class)).create();
		fullCertificate.setType(FULL_CERTIFICATE_TYPE);
		fullCertificate.setDescription("Full certificate");
		fullCertificate.store();
		
		//	2
		CourseCertificateType renewalCertificate = ((CourseCertificateTypeHome) IDOLookup.getHomeLegacy(CourseCertificateType.class)).create();
		renewalCertificate.setType(RENEWAL_CERTIFICATE_TYPE);
		renewalCertificate.setDescription("Renewal certificate");
		renewalCertificate.store();
		
		//	3
		CourseCertificateType temporaryCertificate = ((CourseCertificateTypeHome) IDOLookup.getHomeLegacy(CourseCertificateType.class)).create();
		temporaryCertificate.setType(TEMPORARY_CERTIFICATE_TYPE);
		temporaryCertificate.setDescription("Temporary certificate");
		temporaryCertificate.store();
		
		//	4
		CourseCertificateType limitedCertificate = ((CourseCertificateTypeHome) IDOLookup.getHomeLegacy(CourseCertificateType.class)).create();
		limitedCertificate.setType(LIMITED_CERTIFICATE_TYPE);
		limitedCertificate.setDescription("Limited certificate");
		limitedCertificate.store();
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
			log(Level.WARNING, "Error getting '" + CourseCertificateType.class.getName() + "' by type: " + type);
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Collection<CourseCertificateType> ejbFindAllTypes() throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);

		return idoFindPKsByQuery(query);
	}

}
