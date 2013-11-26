/**
 * @(#)CourseProviderCategoryBPMBean.java    1.0.0 3:40:34 PM
 *
 * Idega Software hf. Source Code Licence Agreement x
 *
 * This agreement, made this 10th of February 2006 by and between 
 * Idega Software hf., a business formed and operating under laws 
 * of Iceland, having its principal place of business in Reykjavik, 
 * Iceland, hereinafter after referred to as "Manufacturer" and Agura 
 * IT hereinafter referred to as "Licensee".
 * 1.  License Grant: Upon completion of this agreement, the source 
 *     code that may be made available according to the documentation for 
 *     a particular software product (Software) from Manufacturer 
 *     (Source Code) shall be provided to Licensee, provided that 
 *     (1) funds have been received for payment of the License for Software and 
 *     (2) the appropriate License has been purchased as stated in the 
 *     documentation for Software. As used in this License Agreement, 
 *     Licensee shall also mean the individual using or installing 
 *     the source code together with any individual or entity, including 
 *     but not limited to your employer, on whose behalf you are acting 
 *     in using or installing the Source Code. By completing this agreement, 
 *     Licensee agrees to be bound by the terms and conditions of this Source 
 *     Code License Agreement. This Source Code License Agreement shall 
 *     be an extension of the Software License Agreement for the associated 
 *     product. No additional amendment or modification shall be made 
 *     to this Agreement except in writing signed by Licensee and 
 *     Manufacturer. This Agreement is effective indefinitely and once
 *     completed, cannot be terminated. Manufacturer hereby grants to 
 *     Licensee a non-transferable, worldwide license during the term of 
 *     this Agreement to use the Source Code for the associated product 
 *     purchased. In the event the Software License Agreement to the 
 *     associated product is terminated; (1) Licensee's rights to use 
 *     the Source Code are revoked and (2) Licensee shall destroy all 
 *     copies of the Source Code including any Source Code used in 
 *     Licensee's applications.
 * 2.  License Limitations
 *     2.1 Licensee may not resell, rent, lease or distribute the 
 *         Source Code alone, it shall only be distributed as a 
 *         compiled component of an application.
 *     2.2 Licensee shall protect and keep secure all Source Code 
 *         provided by this this Source Code License Agreement. 
 *         All Source Code provided by this Agreement that is used 
 *         with an application that is distributed or accessible outside
 *         Licensee's organization (including use from the Internet), 
 *         must be protected to the extent that it cannot be easily 
 *         extracted or decompiled.
 *     2.3 The Licensee shall not resell, rent, lease or distribute 
 *         the products created from the Source Code in any way that 
 *         would compete with Idega Software.
 *     2.4 Manufacturer's copyright notices may not be removed from 
 *         the Source Code.
 *     2.5 All modifications on the source code by Licencee must 
 *         be submitted to or provided to Manufacturer.
 * 3.  Copyright: Manufacturer's source code is copyrighted and contains 
 *     proprietary information. Licensee shall not distribute or 
 *     reveal the Source Code to anyone other than the software 
 *     developers of Licensee's organization. Licensee may be held 
 *     legally responsible for any infringement of intellectual property 
 *     rights that is caused or encouraged by Licensee's failure to abide 
 *     by the terms of this Agreement. Licensee may make copies of the 
 *     Source Code provided the copyright and trademark notices are 
 *     reproduced in their entirety on the copy. Manufacturer reserves 
 *     all rights not specifically granted to Licensee.
 *
 * 4.  Warranty & Risks: Although efforts have been made to assure that the 
 *     Source Code is correct, reliable, date compliant, and technically 
 *     accurate, the Source Code is licensed to Licensee as is and without 
 *     warranties as to performance of merchantability, fitness for a 
 *     particular purpose or use, or any other warranties whether 
 *     expressed or implied. Licensee's organization and all users 
 *     of the source code assume all risks when using it. The manufacturers, 
 *     distributors and resellers of the Source Code shall not be liable 
 *     for any consequential, incidental, punitive or special damages 
 *     arising out of the use of or inability to use the source code or 
 *     the provision of or failure to provide support services, even if we 
 *     have been advised of the possibility of such damages. In any case, 
 *     the entire liability under any provision of this agreement shall be 
 *     limited to the greater of the amount actually paid by Licensee for the 
 *     Software or 5.00 USD. No returns will be provided for the associated 
 *     License that was purchased to become eligible to receive the Source 
 *     Code after Licensee receives the source code. 
 */
package is.idega.idegaweb.egov.course.data;


import java.util.logging.Level;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;
import com.idega.util.StringUtil;

/**
 * <p>Implementation for {@link CourseProviderCategory} EJB entity</p>
 * <p>You can report about problems to: 
 * <a href="mailto:martynas@idega.is">Martynas Stakė</a></p>
 *
 * @version 1.0.0 Oct 23, 2013
 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
 */
public class CourseProviderCategoryBMPBean extends GenericEntity implements
		CourseProviderCategory {

	private static final long serialVersionUID = 3347005347872140752L;

	public static final String TABLE_NAME = "cou_course_provider_category";
	public static final String COLUMN_CATEGORY = "CATEGORY";
	public static final String COLUMN_LOCALIZED_KEY = "localized_key";
	public static final String COLUMN_NAME = "CATEGORY_NAME";
	
	public static final String CATEGORY_MUSIC_SCHOOL = "MUSIC_SCHOOL";
	public static final String CATEGORY_AFTER_SCHOOL_CARE = "AFTER_SCHOOL_CARE";
	public static final String CATEGORY_CHILD_CARE = "CHILD_CARE";
	public static final String CATEGORY_ELEMENTARY_SCHOOL = "ELEMENTARY_SCHOOL";
	public static final String CATEGORY_HIGH_SCHOOL = "HIGH_SCHOOL";
	public static final String CATEGORY_COLLEGE = "COLLEGE";
	public static final String CATEGORY_UNIVERSITY = "UNIVERSITY";
	public static final String CATEGORY_ADULT_EDUCATION = "ADULT_EDUCATION";

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	@Override
	public String getEntityName() {
		return TABLE_NAME;
	}

	/*
	 * (non-Javadoc)
	 * @see com.idega.data.GenericEntity#getIDColumnName()
	 */
	@Override
	public String getIDColumnName() {
		return COLUMN_CATEGORY;
	}

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	@Override
	public void initializeAttributes() {
		addAttribute(getIDColumnName(), "Category", String.class, 30);
		setAsPrimaryKey(COLUMN_CATEGORY, true);
		addAttribute(COLUMN_NAME, "Name of category", String.class, 255);
		addAttribute(COLUMN_LOCALIZED_KEY, "Localized key for category", String.class, 255);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderCategory#setLocalizedKey(java.lang.String)
	 */
	@Override
	public void setLocalizedKey(String key) {
		setColumn(COLUMN_LOCALIZED_KEY, key);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderCategory#getLocalizedKey()
	 */
	@Override
	public String getLocalizedKey() {
		return getStringColumnValue(COLUMN_LOCALIZED_KEY);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderCategory#setCategory(java.lang.String)
	 */
	@Override
	public void setCategory(String category) {
		setColumn(COLUMN_CATEGORY, category.toUpperCase());
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderCategory#getCategory()
	 */
	@Override
	public String getCategory() {
		return getStringColumnValue(COLUMN_CATEGORY);
	}

	/*
	 * (non-Javadoc)
	 * @see com.idega.data.GenericEntity#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		setColumn(COLUMN_NAME, name);
	}

	/*
	 * (non-Javadoc)
	 * @see com.idega.data.GenericEntity#getName()
	 */
	@Override
	public String getName() {
		return getStringColumnValue(COLUMN_NAME);
	}
	
	public String ejbFindByLocalizedKey(String key) {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this).appendWhereEqualsQuoted(COLUMN_LOCALIZED_KEY, key);

		try {
			return (String) idoFindOnePKByQuery(query);
		} catch (FinderException e) {
			getLogger().log(Level.WARNING, 
					"Failed to get primary key by localization key: '" + key + 
					"' and query:'" + query.toString() + "'", e);
		}

		return null;
	}

	/**
	 * 
	 * @param category is {@link CourseProviderCategory#getCategory()}, 
	 * not <code>null</code>;
	 * @return {@link CourseProviderCategory#getPrimaryKey()} or <code>null</code>
	 * on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public Object ejbFindPrimaryKey(String category) {
		if (StringUtil.isEmpty(category)) {
			return null;
		}

		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		query.appendWhereEquals(COLUMN_CATEGORY, category);
		try {
			return idoFindOnePKByQuery(query);
		} catch (FinderException e) {
			getLogger().log(Level.WARNING, 
					"Failed to find primary key for " + this.getClass().getName() + 
					" by query: '" + query + "'");
		}

		return null;
	}

	public String ejbFindAfterSchoolCareCategory() {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this).appendWhereEqualsQuoted(
				COLUMN_CATEGORY, CATEGORY_AFTER_SCHOOL_CARE);

		try {
			return (String) idoFindOnePKByQuery(query);
		} catch (FinderException e) {
			getLogger().log(Level.WARNING, 
					"Failed to get course provider category by id: '" + 
							CATEGORY_AFTER_SCHOOL_CARE + "' and query: '" + 
							query.toString() + "'");
		}

		return null;
	}

	public String ejbFindChildcareCategory() {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this).appendWhereEqualsQuoted(
				COLUMN_CATEGORY, CATEGORY_CHILD_CARE);

		try {
			return (String) idoFindOnePKByQuery(query);
		} catch (FinderException e) {
			getLogger().log(Level.WARNING, 
					"Failed to get course provider category by id: '" + 
							CATEGORY_CHILD_CARE + "' and query: '" + 
							query.toString() + "'");
		}

		return null;
	}

	public String ejbFindElementarySchoolCategory() {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this).appendWhereEqualsQuoted(
				COLUMN_CATEGORY, CATEGORY_ELEMENTARY_SCHOOL);

		try {
			return (String) idoFindOnePKByQuery(query);
		} catch (FinderException e) {
			getLogger().log(Level.WARNING, 
					"Failed to get course provider category by id: '" + 
							CATEGORY_ELEMENTARY_SCHOOL + "' and query: '" + 
							query.toString() + "'");
		}

		return null;
	}

	public String ejbFindHighSchoolCategory() {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this).appendWhereEqualsQuoted(
				COLUMN_CATEGORY, CATEGORY_HIGH_SCHOOL);

		try {
			return (String) idoFindOnePKByQuery(query);
		} catch (FinderException e) {
			getLogger().log(Level.WARNING, 
					"Failed to get course provider category by id: '" + 
							CATEGORY_HIGH_SCHOOL + "' and query: '" + 
							query.toString() + "'");
		}

		return null;
	}

	public String ejbFindCollegeCategory() {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this).appendWhereEqualsQuoted(
				COLUMN_CATEGORY, CATEGORY_COLLEGE);

		try {
			return (String) idoFindOnePKByQuery(query);
		} catch (FinderException e) {
			getLogger().log(Level.WARNING, 
					"Failed to get course provider category by id: '" + 
							CATEGORY_COLLEGE + "' and query: '" + 
							query.toString() + "'");
		}

		return null;
	}

	public String ejbFindUniversityCategory() {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this).appendWhereEqualsQuoted(
				COLUMN_CATEGORY, CATEGORY_UNIVERSITY);

		try {
			return (String) idoFindOnePKByQuery(query);
		} catch (FinderException e) {
			getLogger().log(Level.WARNING, 
					"Failed to get course provider category by id: '" + 
							CATEGORY_UNIVERSITY + "' and query: '" + 
							query.toString() + "'");
		}

		return null;
	}

	public String ejbFindAdultEducationCategory() {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this).appendWhereEqualsQuoted(
				COLUMN_CATEGORY, CATEGORY_ADULT_EDUCATION);

		try {
			return (String) idoFindOnePKByQuery(query);
		} catch (FinderException e) {
			getLogger().log(Level.WARNING, 
					"Failed to get course provider category by id: '" + 
							CATEGORY_ADULT_EDUCATION + "' and query: '" + 
							query.toString() + "'");
		}
	
		return null;
	}

	public String ejbFindMusicSchoolCategory() {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this).appendWhereEqualsQuoted(
				COLUMN_CATEGORY, CATEGORY_MUSIC_SCHOOL);

		try {
			return (String) idoFindOnePKByQuery(query);
		} catch (FinderException e) {
			getLogger().log(Level.WARNING, 
					"Failed to get course provider category by id: '" + 
							CATEGORY_MUSIC_SCHOOL + "' and query: '" + 
							query.toString() + "'");
		}

		return null;
	}
}
