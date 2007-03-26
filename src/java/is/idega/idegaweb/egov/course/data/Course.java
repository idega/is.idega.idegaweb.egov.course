package is.idega.idegaweb.egov.course.data;


import com.idega.block.school.data.SchoolType;
import java.sql.Timestamp;
import com.idega.data.IDOEntity;

public interface Course extends IDOEntity {
	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#getName
	 */
	public String getName();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#setName
	 */
	public void setName(String name);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#getDescription
	 */
	public String getDescription();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#setDescription
	 */
	public void setDescription(String description);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#getStartDate
	 */
	public Timestamp getStartDate();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#setStartDate
	 */
	public void setStartDate(Timestamp startDate);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#getEndDate
	 */
	public Timestamp getEndDate();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#setEndDate
	 */
	public void setEndDate(Timestamp endDate);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#getSchoolType
	 */
	public SchoolType getSchoolType();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#setSchoolTypePK
	 */
	public void setSchoolTypePK(Object PK);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#setSchoolType
	 */
	public void setSchoolType(SchoolType schoolType);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#getCourseType
	 */
	public CourseType getCourseType();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#setCourseTypePK
	 */
	public void setCourseTypePK(Object PK);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#setCourseType
	 */
	public void setCourseType(CourseType courseType);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#getAccountingKey
	 */
	public String getAccountingKey();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#setAccountingKey
	 */
	public void setAccountingKey(String key);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#getBirthyearFrom
	 */
	public int getBirthyearFrom();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#setBirthyearFrom
	 */
	public void setBirthyearFrom(int from);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#getBirthyearTo
	 */
	public int getBirthyearTo();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#setBirthyearTo
	 */
	public void setBirthyearTo(int to);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#getMax
	 */
	public int getMax();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#setMax
	 */
	public void setMax(int max);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#getPrice
	 */
	public CoursePrice getPrice();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#setPrice
	 */
	public void setPrice(CoursePrice price);
}