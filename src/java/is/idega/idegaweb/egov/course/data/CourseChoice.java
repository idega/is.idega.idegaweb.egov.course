package is.idega.idegaweb.egov.course.data;


import com.idega.user.data.User;
import java.sql.Timestamp;
import com.idega.data.IDOEntity;

public interface CourseChoice extends IDOEntity {

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseChoiceBMPBean#getApplication
	 */
	public CourseApplication getApplication();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseChoiceBMPBean#getCourse
	 */
	public Course getCourse();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseChoiceBMPBean#getUser
	 */
	public User getUser();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseChoiceBMPBean#getDayCare
	 */
	public int getDayCare();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseChoiceBMPBean#getPaymentTimestamp
	 */
	public Timestamp getPaymentTimestamp();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseChoiceBMPBean#isPickedUp
	 */
	public boolean isPickedUp();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseChoiceBMPBean#isValid
	 */
	public boolean isValid();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseChoiceBMPBean#hasDyslexia
	 */
	public boolean hasDyslexia();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseChoiceBMPBean#inOnWaitingList
	 */
	public boolean inOnWaitingList();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseChoiceBMPBean#hasReceivedReminder
	 */
	public boolean hasReceivedReminder();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseChoiceBMPBean#setApplication
	 */
	public void setApplication(CourseApplication application);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseChoiceBMPBean#setCourse
	 */
	public void setCourse(Course course);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseChoiceBMPBean#setUser
	 */
	public void setUser(User user);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseChoiceBMPBean#setPaymentTimestamp
	 */
	public void setPaymentTimestamp(Timestamp timestamp);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseChoiceBMPBean#setDayCare
	 */
	public void setDayCare(int dayCare);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseChoiceBMPBean#setPickedUp
	 */
	public void setPickedUp(boolean pickedUp);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseChoiceBMPBean#setValid
	 */
	public void setValid(boolean valid);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseChoiceBMPBean#setHasDyslexia
	 */
	public void setHasDyslexia(boolean dyslexia);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseChoiceBMPBean#setWaitingList
	 */
	public void setWaitingList(boolean waitingList);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseChoiceBMPBean#setReceivedReminder
	 */
	public void setReceivedReminder(boolean hasReceivedReminder);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseChoiceBMPBean#setPassed
	 */
	public void setPassed(boolean passed);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseChoiceBMPBean#hasPassed
	 */
	public boolean hasPassed();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseChoiceBMPBean#setCourseCertificateFee
	 */
	public void setCourseCertificateFee(float fee);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseChoiceBMPBean#getCourseCertificateFee
	 */
	public float getCourseCertificateFee();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseChoiceBMPBean#isCertificateOfProperty
	 */
	public boolean isCertificateOfProperty();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseChoiceBMPBean#isCriminalRecord
	 */
	public boolean isCriminalRecord();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseChoiceBMPBean#isDidNotShowUp
	 */
	public boolean isDidNotShowUp();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseChoiceBMPBean#isNeedVerificationFromGovermentOffice
	 */
	public boolean isNeedVerificationFromGovermentOffice();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseChoiceBMPBean#isVerificationFromGovermentOffice
	 */
	public boolean isVerificationFromGovermentOffice();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseChoiceBMPBean#isVerificationOfPayment
	 */
	public boolean isVerificationOfPayment();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseChoiceBMPBean#setCertificateOfProperty
	 */
	public void setCertificateOfProperty(boolean certificateOfProperty);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseChoiceBMPBean#setCriminalRecord
	 */
	public void setCriminalRecord(boolean criminalRecord);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseChoiceBMPBean#isLimitedCertificate
	 */
	public boolean isLimitedCertificate();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseChoiceBMPBean#setDidNotShowUp
	 */
	public void setDidNotShowUp(boolean didNotShowUp);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseChoiceBMPBean#setNeedsVerificationFromGovermentOffice
	 */
	public void setNeedsVerificationFromGovermentOffice(boolean needsVerificationFromOffice);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseChoiceBMPBean#setVerificationFromGovermentOffice
	 */
	public void setVerificationFromGovermentOffice(boolean verificationFromGovermentOffice);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseChoiceBMPBean#setVerificationOfPayment
	 */
	public void setVerificationOfPayment(boolean verificationOfPayment);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseChoiceBMPBean#setLimitedCertificate
	 */
	public void setLimitedCertificate(boolean limitedCertificate);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseChoiceBMPBean#getBooleanValueFromColumn
	 */
	public boolean getBooleanValueFromColumn(String columnName);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseChoiceBMPBean#setBooleanValueForColumn
	 */
	public void setBooleanValueForColumn(boolean value, String columnName);
}