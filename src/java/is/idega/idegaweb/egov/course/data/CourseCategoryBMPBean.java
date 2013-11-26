/*
 * $Id$
 * Created on Jan 3, 2008
 *
 * Copyright (C) 2008 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.egov.course.data;



public class CourseCategoryBMPBean extends CourseProviderTypeBMPBean implements CourseCategory, CourseProviderType {

	public static final String ALLOWS_ALL_CHILDREN = "allows_all_children";
	public static final String HAS_CARE = "has_care";
	public static final String USE_FIXED_PRICING = "use_fixed_pricing";
	public static final String SEND_REMINDER_EMAIL = "send_reminder_email";

	public void initializeAttributes() {
		super.initializeAttributes();

		this.addAttribute(ALLOWS_ALL_CHILDREN, "Allow all children", Boolean.class);
		this.addAttribute(HAS_CARE, "Has care", Boolean.class);
		this.addAttribute(USE_FIXED_PRICING, "Use fixed pricing", Boolean.class);
		this.addAttribute(SEND_REMINDER_EMAIL, "Send reminder email", Boolean.class);
	}

	//Getters
	public boolean allowsAllChildren() {
		return getBooleanColumnValue(ALLOWS_ALL_CHILDREN, true);
	}

	public boolean hasCare() {
		return getBooleanColumnValue(HAS_CARE, true);
	}

	public boolean useFixedPricing() {
		return getBooleanColumnValue(USE_FIXED_PRICING, false);
	}
	
	public boolean sendReminderEmail() {
		return getBooleanColumnValue(SEND_REMINDER_EMAIL, true);
	}

	//Setters
	public void setAllowsAllChildren(boolean allowAll) {
		setColumn(ALLOWS_ALL_CHILDREN, allowAll);
	}

	public void setHasCare(boolean hasCare) {
		setColumn(HAS_CARE, hasCare);
	}

	public void setUseFixedPricing(boolean useFixedPricing) {
		setColumn(USE_FIXED_PRICING, useFixedPricing);
	}
	
	public void setSendReminderEmail(boolean sendReminderEmail) {
		setColumn(SEND_REMINDER_EMAIL, sendReminderEmail);
	}
}