package is.idega.idegaweb.egov.course.data;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

import javax.ejb.FinderException;

import com.idega.company.data.Company;
import com.idega.core.file.data.ICFile;
import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
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
	private static final String COLUMN_CERTIFICATE_FILE_ID = "cert_file_id";
	private static final String COLUMN_CERTIFICATE_NUMBER = "certificate_number";

	@Override
	public String getEntityName() {
		return ENTITY_NAME;
	}

	@Override
	public void initializeAttributes() {
		addAttribute(getIDColumnName());

		addAttribute(COLUMN_VALID_FROM, "Certificate valid from", Timestamp.class);
		addAttribute(COLUMN_VALID_THRU, "Certificate valid thru", Timestamp.class);
		addAttribute(COLUMN_CERTIFICATE_NUMBER, "Number", Integer.class);

		addManyToOneRelationship(COLUMN_CERTIFICATE_FILE_ID, ICFile.class);
		addOneToOneRelationship(COLUMN_TYPE_OF_CERTIFICATE_ID, CourseCertificateType.class);
		addOneToOneRelationship(COLUMN_PARTICIPANT_ID, User.class);
		addOneToOneRelationship(COLUMN_COMPANY_ID, Company.class);
		addOneToOneRelationship(COLUMN_COURSE_ID, Course.class);
	}

	@Override
	public void setCertificateType(CourseCertificateType type) {
		setColumn(COLUMN_TYPE_OF_CERTIFICATE_ID, type);
	}

	@Override
	public void setCertificateFile(ICFile file) {
		setColumn(COLUMN_CERTIFICATE_FILE_ID, file);
	}

	@Override
	public ICFile getCertificateFile() {
		return (ICFile) getColumnValue(COLUMN_CERTIFICATE_FILE_ID);
	}

	@Override
	public CourseCertificateType getCourseCertificateType() {
		return (CourseCertificateType) getColumnValue(COLUMN_TYPE_OF_CERTIFICATE_ID);
	}

	@Override
	public void setParticipant(User participant) {
		setColumn(COLUMN_PARTICIPANT_ID, participant);
	}

	@Override
	public User getParticipant() {
		return (User) getColumnValue(COLUMN_PARTICIPANT_ID);
	}

	@Override
	public int getParticipantId() {
		return getIntColumnValue(COLUMN_PARTICIPANT_ID);
	}

	@Override
	public void setCompany(Company company) {
		setColumn(COLUMN_COMPANY_ID, company);
	}

	@Override
	public Company getCompany() {
		return (Company) getColumnValue(COLUMN_COMPANY_ID);
	}

	@Override
	public void setCourse(Course course) {
		setColumn(COLUMN_COURSE_ID, course);
	}

	@Override
	public Course getCourse() {
		return (Course) getColumnValue(COLUMN_COURSE_ID);
	}

	@Override
	public void setValidFrom(Timestamp validFrom) {
		setColumn(COLUMN_VALID_FROM, validFrom);
	}

	@Override
	public IWTimestamp getValidFrom() {
		return getIWTimestamp((Timestamp) getColumnValue(COLUMN_VALID_FROM));
	}

	@Override
	public void setValidThru(Timestamp validThru) {
		setColumn(COLUMN_VALID_THRU, validThru);
	}

	@Override
	public IWTimestamp getValidThru() {
		return getIWTimestamp((Timestamp) getColumnValue(COLUMN_VALID_THRU));
	}

	private IWTimestamp getIWTimestamp(Timestamp time) {
		return time == null ? null : new IWTimestamp(time);
	}

	@Override
	public void setNumber(int number) {
		setColumn(COLUMN_CERTIFICATE_NUMBER, number);
	}

	@Override
	public int getNumber() {
		return getIntColumnValue(COLUMN_CERTIFICATE_NUMBER);
	}

	public Collection ejbFindAllByUser(User user) throws FinderException {
		IDOQuery query = idoQueryGetSelect();
		query.appendWhereEquals(COLUMN_PARTICIPANT_ID, user.getId());
		return super.idoFindPKsByQuery(query);
	}

	public Collection ejbFindAllByUserAndCourse(User user, Course course) throws FinderException {
		IDOQuery query = idoQueryGetSelect();
		query.appendWhereEquals(COLUMN_PARTICIPANT_ID, user.getId());
		query.appendAndEquals(COLUMN_COURSE_ID, course.getPrimaryKey());
		return super.idoFindPKsByQuery(query);
	}

	public Object ejbFindByUserAndCourse(User user, Course course) throws FinderException {
		IDOQuery query = idoQueryGetSelect();
		query.appendWhereEquals(COLUMN_PARTICIPANT_ID, user.getId());
		query.appendAndEquals(COLUMN_COURSE_ID, course.getPrimaryKey());
		query.appendOrderByDescending(COLUMN_VALID_THRU);
		return super.idoFindOnePKByQuery(query);
	}

	public Collection ejbFindByUsersAndValidityAndType(List usersIds, boolean onlyValidCertificates, String certificateTypeId) throws FinderException {
		IDOQuery query = idoQueryGetSelect();

		if (usersIds == null || usersIds.size() == 0) {
			query.appendWhere().append(COLUMN_PARTICIPANT_ID).appendIsNotNull();
		}
		else {
			query.appendWhere().append(COLUMN_PARTICIPANT_ID).appendInCollection(usersIds);
		}
		if (onlyValidCertificates) {
			query.appendAnd().append(COLUMN_VALID_THRU).appendGreaterThanOrEqualsSign().append(IWTimestamp.RightNow());
		}
		if (certificateTypeId != null) {
			query.appendAnd().appendEquals(COLUMN_TYPE_OF_CERTIFICATE_ID, certificateTypeId);
		}
		return super.idoFindPKsByQuery(query);
	}

	public Object ejbFindHighestNumber() throws FinderException {
		Table table = new Table(this);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(table.getColumn(getIDColumnName()));
		query.addCriteria(new MatchCriteria(table.getColumn(COLUMN_CERTIFICATE_NUMBER), true));
		query.addOrder(table, COLUMN_CERTIFICATE_NUMBER, false);

		return idoFindOnePKByQuery(query);
	}
}