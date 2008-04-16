package is.idega.idegaweb.egov.course.data;


import com.idega.block.school.data.SchoolType;
import com.idega.data.IDOEntity;

public interface CourseCategory extends IDOEntity, SchoolType {

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseCategoryBMPBean#allowsAllChildren
	 */
	public boolean allowsAllChildren();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseCategoryBMPBean#hasCare
	 */
	public boolean hasCare();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseCategoryBMPBean#useFixedPricing
	 */
	public boolean useFixedPricing();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseCategoryBMPBean#setAllowsAllChildren
	 */
	public void setAllowsAllChildren(boolean allowAll);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseCategoryBMPBean#setHasCare
	 */
	public void setHasCare(boolean hasCare);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseCategoryBMPBean#setUseFixedPricing
	 */
	public void setUseFixedPricing(boolean useFixedPricing);
}