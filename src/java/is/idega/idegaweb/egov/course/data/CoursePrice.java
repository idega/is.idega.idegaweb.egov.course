package is.idega.idegaweb.egov.course.data;


import java.sql.Timestamp;

import com.idega.data.IDOEntity;

public interface CoursePrice extends IDOEntity {

	/**
	 * @see is.idega.idegaweb.egov.course.data.CoursePriceBMPBean#getName
	 */
	public String getName();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CoursePriceBMPBean#getSchoolArea
	 */
	public CourseProviderArea getSchoolArea();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CoursePriceBMPBean#getCourseType
	 */
	public CourseType getCourseType();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CoursePriceBMPBean#getValidFrom
	 */
	public Timestamp getValidFrom();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CoursePriceBMPBean#getValidTo
	 */
	public Timestamp getValidTo();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CoursePriceBMPBean#getNumberOfDays
	 */
	public int getNumberOfDays();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CoursePriceBMPBean#getPrice
	 */
	public int getPrice();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CoursePriceBMPBean#getPreCarePrice
	 */
	public int getPreCarePrice();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CoursePriceBMPBean#getPostCarePrice
	 */
	public int getPostCarePrice();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CoursePriceBMPBean#setName
	 */
	public void setName(String name);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CoursePriceBMPBean#setSchoolArea
	 */
	public void setSchoolArea(CourseProviderArea area);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CoursePriceBMPBean#setCourseType
	 */
	public void setCourseType(CourseType type);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CoursePriceBMPBean#setValidFrom
	 */
	public void setValidFrom(Timestamp stamp);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CoursePriceBMPBean#setValidTo
	 */
	public void setValidTo(Timestamp stamp);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CoursePriceBMPBean#setValid
	 */
	public void setValid(boolean valid);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CoursePriceBMPBean#setNumberOfDays
	 */
	public void setNumberOfDays(int noDays);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CoursePriceBMPBean#setPrice
	 */
	public void setPrice(int price);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CoursePriceBMPBean#setPreCarePrice
	 */
	public void setPreCarePrice(int price);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CoursePriceBMPBean#setPostCarePrice
	 */
	public void setPostCarePrice(int price);
	
	public CourseProviderType getSchoolType();
	public void setSchoolType(CourseProviderType schoolType);

//	public SchoolSeason getSchoolSeason();
//	public void setSchoolSeason(SchoolSeason season);
}