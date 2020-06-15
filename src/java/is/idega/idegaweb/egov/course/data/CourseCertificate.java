package is.idega.idegaweb.egov.course.data;


import java.sql.Timestamp;

import com.idega.company.data.Company;
import com.idega.core.file.data.ICFile;
import com.idega.data.IDOEntity;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

public interface CourseCertificate extends IDOEntity {

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseCertificateBMPBean#setCertificateType
	 */
	public void setCertificateType(CourseCertificateType type);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseCertificateBMPBean#setCertificateFile
	 */
	public void setCertificateFile(ICFile file);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseCertificateBMPBean#getCertificateFile
	 */
	public ICFile getCertificateFile();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseCertificateBMPBean#getCourseCertificateType
	 */
	public CourseCertificateType getCourseCertificateType();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseCertificateBMPBean#setParticipant
	 */
	public void setParticipant(User participant);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseCertificateBMPBean#getParticipant
	 */
	public User getParticipant();

	public int getParticipantId();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseCertificateBMPBean#setCompany
	 */
	public void setCompany(Company company);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseCertificateBMPBean#getCompany
	 */
	public Company getCompany();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseCertificateBMPBean#setCourse
	 */
	public void setCourse(Course course);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseCertificateBMPBean#getCourse
	 */
	public Course getCourse();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseCertificateBMPBean#setValidFrom
	 */
	public void setValidFrom(Timestamp validFrom);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseCertificateBMPBean#getValidFrom
	 */
	public IWTimestamp getValidFrom();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseCertificateBMPBean#setValidThru
	 */
	public void setValidThru(Timestamp validThru);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseCertificateBMPBean#getValidThru
	 */
	public IWTimestamp getValidThru();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseCertificateBMPBean#setNumber
	 */
	public void setNumber(int number);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseCertificateBMPBean#getNumber
	 */
	public int getNumber();
}