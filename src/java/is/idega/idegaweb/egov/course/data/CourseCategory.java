package is.idega.idegaweb.egov.course.data;


import com.idega.data.IDOEntity;

public interface CourseCategory extends IDOEntity, CourseProviderType {

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
	 * @see is.idega.idegaweb.egov.course.data.CourseCategoryBMPBean#sendReminderEmail
	 */
	public boolean sendReminderEmail();
	
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
	
	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseCategoryBMPBean#setSendReminderEmail
	 */
	public void setSendReminderEmail(boolean sendReminderEmail);
}