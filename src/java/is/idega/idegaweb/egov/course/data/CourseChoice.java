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
	
	public void setPassed(boolean passed);
	public boolean hasPassed();
	
	public void setCourseCertificateFee(float fee);
	public float getCourseCertificateFee();
	
	public void setVerificationFromGovermentOffice(boolean verificationFromGovermentOffice);
	public boolean isVerificationFromGovermentOffice();
	
	public void setCertificateOfProperty(boolean certificateOfProperty);
	public boolean isCertificateOfProperty();
	
	public void setCriminalRecord(boolean criminalRecord);
	public boolean isCriminalRecord();
	
	public void setVerificationOfPayment(boolean verificationOfPayment);
	public boolean isVerificationOfPayment();
	
	public void setNeedsVerificationFromGovermentOffice(boolean needsVerificationFromOffice);
	public boolean isNeedVerificationFromGovermentOffice();
	
	public void setDidNotShowUp(boolean didNotShowUp);
	public boolean isDidNotShowUp();
	
	public void setBooleanValueForColumn(boolean value, String columnName);
	public boolean getBooleanValueFromColumn(String columnName);
}