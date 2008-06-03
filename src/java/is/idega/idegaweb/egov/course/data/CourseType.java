package is.idega.idegaweb.egov.course.data;


import com.idega.data.IDOEntity;

public interface CourseType extends IDOEntity {

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseTypeBMPBean#getName
	 */
	public String getName();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseTypeBMPBean#getDescription
	 */
	public String getDescription();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseTypeBMPBean#getLocalizationKey
	 */
	public String getLocalizationKey();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseTypeBMPBean#getCourseCategory
	 */
	public CourseCategory getCourseCategory();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseTypeBMPBean#getAccountingKey
	 */
	public String getAccountingKey();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseTypeBMPBean#getAbbreviation
	 */
	public String getAbbreviation();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseTypeBMPBean#getOrder
	 */
	public int getOrder();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseTypeBMPBean#setName
	 */
	public void setName(String name);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseTypeBMPBean#setDescription
	 */
	public void setDescription(String description);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseTypeBMPBean#setLocalizationKey
	 */
	public void setLocalizationKey(String localizationKey);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseTypeBMPBean#setCourseCategory
	 */
	public void setCourseCategory(CourseCategory category);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseTypeBMPBean#setAccountingKey
	 */
	public void setAccountingKey(String key);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseTypeBMPBean#setAbbreviation
	 */
	public void setAbbreviation(String abbreviation);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseTypeBMPBean#setOrder
	 */
	public void setOrder(int order);
}