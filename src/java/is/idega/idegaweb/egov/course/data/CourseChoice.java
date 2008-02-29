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
}