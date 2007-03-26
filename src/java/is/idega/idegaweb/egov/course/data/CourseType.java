package is.idega.idegaweb.egov.course.data;


import com.idega.block.school.data.SchoolType;
import com.idega.data.IDOEntity;

public interface CourseType extends IDOEntity {
	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseTypeBMPBean#getName
	 */
	public String getName();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseTypeBMPBean#setName
	 */
	public void setName(String name);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseTypeBMPBean#getDescription
	 */
	public String getDescription();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseTypeBMPBean#setDescription
	 */
	public void setDescription(String description);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseTypeBMPBean#getLocalizationKey
	 */
	public String getLocalizationKey();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseTypeBMPBean#setLocalizationKey
	 */
	public void setLocalizationKey(String localizationKey);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseTypeBMPBean#getSchoolType
	 */
	public SchoolType getSchoolType();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseTypeBMPBean#setSchoolType
	 */
	public void setSchoolType(SchoolType type);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseTypeBMPBean#setSchoolTypePK
	 */
	public void setSchoolTypePK(Object primaryKey);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseTypeBMPBean#getOrder
	 */
	public int getOrder();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseTypeBMPBean#setOrder
	 */
	public void setOrder(int order);
}