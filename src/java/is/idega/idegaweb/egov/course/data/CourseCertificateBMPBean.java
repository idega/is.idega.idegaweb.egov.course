package is.idega.idegaweb.egov.course.data;

import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.company.data.Company;
import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

public class CourseCertificateBMPBean extends GenericEntity implements CourseCertificate {

	private static final long serialVersionUID = 6176175280049602632L;

	public static final String ENTITY_NAME = "COU_CERTIFICATE";
	
	private static final String COLUMN_TYPE_OF_CERTIFICATE_ID = "type_of_certificate_id";
	private static final String COLUMN_PARTICIPANT_ID = "user_id";
	private static final String COLUMN_COMPANY_ID = "company_id";
	private static final String COLUMN_VALID_FROM = "valid_from";
	private static final String COLUMN_VALID_THRU = "valid_thru";
	private static final String COLUMN_COURSE_ID = "course_id";
	
	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		
		addAttribute(COLUMN_VALID_FROM, "Certificate valid from", Timestamp.class);
		addAttribute(COLUMN_VALID_THRU, "Certificate valid thru", Timestamp.class);
		
		addOneToOneRelationship(COLUMN_TYPE_OF_CERTIFICATE_ID, CourseCertificateType.class);
		addOneToOneRelationship(COLUMN_PARTICIPANT_ID, User.class);
		addOneToOneRelationship(COLUMN_COMPANY_ID, Company.class);
		addOneToOneRelationship(COLUMN_COURSE_ID, Course.class);
	}
	
	public void setCertificateType(CourseCertificateType type) {
		setColumn(COLUMN_TYPE_OF_CERTIFICATE_ID, type);
	}
	
	public CourseCertificateType getCourseCertificateType() {
		return (CourseCertificateType) getColumnValue(COLUMN_TYPE_OF_CERTIFICATE_ID);
	}
	
	public void setParticipant(User participant) {
		setColumn(COLUMN_PARTICIPANT_ID, participant);
	}

	public User getParticipant() {
		return (User) getColumnValue(COLUMN_PARTICIPANT_ID);
	}
	
	public void setCompany(Company company) {
		setColumn(COLUMN_COMPANY_ID, company);
	}
	
	public Company getCompany() {
		return (Company) getColumnValue(COLUMN_COMPANY_ID);
	}
	
	public void setCourse(Course course) {
		setColumn(COLUMN_COURSE_ID, course);
	}
	
	public Course getCourse() {
		return (Course) getColumnValue(COLUMN_COURSE_ID);
	}
	
	public void setValidFrom(Timestamp validFrom) {
		setColumn(COLUMN_VALID_FROM, validFrom);
	}
	
	public IWTimestamp getValidFrom() {
		return getIWTimestamp((Timestamp) getColumnValue(COLUMN_VALID_FROM));
	}
	
	public void setValidThru(Timestamp validThru) {
		setColumn(COLUMN_VALID_THRU, validThru);
	}
	
	public IWTimestamp getValidThru() {
		return getIWTimestamp((Timestamp) getColumnValue(COLUMN_VALID_THRU));
	}
	
	private IWTimestamp getIWTimestamp(Timestamp time) {
		return time == null ? null : new IWTimestamp(time);
	}
	
	public Collection ejbFindAllByUser(User user) throws FinderException {
		IDOQuery query = idoQueryGetSelect();
		query.appendWhereEquals(COLUMN_PARTICIPANT_ID, user.getId());
		return super.idoFindPKsByQuery(query);
	}
}
