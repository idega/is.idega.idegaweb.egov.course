package is.idega.idegaweb.egov.course.data;


import com.idega.block.school.data.SchoolType;
import java.sql.Timestamp;
import com.idega.data.IDOEntity;

public interface CoursePrice extends IDOEntity {
	/**
	 * @see is.idega.idegaweb.egov.course.data.CoursePriceBMPBean#getName
	 */
	public String getName();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CoursePriceBMPBean#setName
	 */
	public void setName(String name);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CoursePriceBMPBean#getSchoolTypeID
	 */
	public SchoolType getSchoolTypeID();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CoursePriceBMPBean#setSchoolTypeID
	 */
	public void setSchoolTypeID(Integer pk);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CoursePriceBMPBean#getCourseTypeID
	 */
	public CourseType getCourseTypeID();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CoursePriceBMPBean#setCourseTypeID
	 */
	public void setCourseTypeID(Integer pk);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CoursePriceBMPBean#getValidFrom
	 */
	public Timestamp getValidFrom();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CoursePriceBMPBean#setValidFrom
	 */
	public void setValidFrom(Timestamp stamp);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CoursePriceBMPBean#getValidTo
	 */
	public Timestamp getValidTo();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CoursePriceBMPBean#setValidTo
	 */
	public void setValidTo(Timestamp stamp);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CoursePriceBMPBean#getNumberOfDays
	 */
	public int getNumberOfDays();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CoursePriceBMPBean#setNumberOfDays
	 */
	public void setNumberOfDays(int noDays);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CoursePriceBMPBean#getPrice
	 */
	public int getPrice();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CoursePriceBMPBean#setPrice
	 */
	public void setPrice(int price);
}