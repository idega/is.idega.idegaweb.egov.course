/**
 * @(#)CourseProviderAreaBPMBean.java    1.0.0 3:43:41 PM
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


import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.logging.Level;

import javax.ejb.EJBLocalObject;
import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOEntity;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDOQuery;
import com.idega.data.IDOStoreException;
import com.idega.util.ListUtil;
import com.idega.util.StringUtil;

/**
 * <p>{@link EJBLocalObject} implementation for {@link CourseProviderArea}</p>
 * <p>You can report about problems to: 
 * <a href="mailto:martynas@idega.is">Martynas Stakė</a></p>
 *
 * @version 1.0.0 Oct 23, 2013
 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
 */
public class CourseProviderAreaBMPBean extends GenericEntity implements
		CourseProviderArea {

	private static final long serialVersionUID = -5186591319889899533L;

	public static final String TABLE_NAME = "cou_course_provider_area";
	public static final String COLUMN_NAME = "AREA_NAME";
	public static final String COLUMN_ACCOUNTING_KEY = "ACCOUNTING_KEY";
	public static final String COLUMN_SCHOOL_CATEGORY = "SCHOOL_CATEGORY";
	public final static String COLUMN_INFO = "AREA_INFO";
	public final static String COLUMN_CITY = "AREA_CITY";
	/*
	 * (non-Javadoc)
	 * @see com.idega.data.GenericEntity#getName()
	 */
	@Override
	public String getName() {
		return getSchoolAreaName();
	}

	/* (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderArea#getAccountingKey()
	 */
	@Override
	public String getAccountingKey() {
		return getStringColumnValue(COLUMN_ACCOUNTING_KEY);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderArea#getCategory()
	 */
	@Override
	public CourseProviderCategory getCategory() {
		return (CourseProviderCategory) getColumnValue(COLUMN_SCHOOL_CATEGORY);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderArea#setCategory(is.idega.idegaweb.egov.course.data.CourseProviderCategory)
	 */
	@Override
	public void setCategory(CourseProviderCategory category) {
		setColumn(COLUMN_SCHOOL_CATEGORY, category);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderArea#setAccountingKey(java.lang.String)
	 */
	@Override
	public void setAccountingKey(String accountingKey) {
		this.setColumn(COLUMN_ACCOUNTING_KEY, accountingKey);
	}

	/* (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderArea#getSchoolAreaName()
	 */
	@Override
	public String getSchoolAreaName() {
		return this.getStringColumnValue(COLUMN_NAME);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderArea#setSchoolAreaName(java.lang.String)
	 */
	@Override
	public void setSchoolAreaName(String name) {
		this.setColumn(COLUMN_NAME, name);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderArea#setSchoolAreaInfo(java.lang.String)
	 */
	@Override
	public void setSchoolAreaInfo(String info) {
		this.setColumn(COLUMN_INFO, info);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderArea#getSchoolAreaInfo()
	 */
	@Override
	public String getSchoolAreaInfo() {
		return this.getStringColumnValue(COLUMN_INFO);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderArea#getSchoolAreaCity()
	 */
	@Override
	public String getSchoolAreaCity() {
		return this.getStringColumnValue(COLUMN_CITY);
	}
	
	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderArea#setSchoolAreaCity(java.lang.String)
	 */
	@Override
	public void setSchoolAreaCity(String city) {
		this.setColumn(COLUMN_CITY, city);
	}

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	@Override
	public String getEntityName() {
		return TABLE_NAME;
	}
	
	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	@Override
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME, "Schoolname", true, true, String.class);
		addAttribute(COLUMN_INFO, "Info", true, true, String.class);
		addAttribute(COLUMN_CITY, "City", true, true, String.class);
		addAttribute(COLUMN_ACCOUNTING_KEY, "Accounting key", true, true, String.class);
		addManyToOneRelationship(COLUMN_SCHOOL_CATEGORY, CourseProviderCategory.class);
	}

	/*
	 * (non-Javadoc)
	 * @see com.idega.data.GenericEntity#remove()
	 */
	@Override
	public void remove() {
		String primaryKey = this.getPrimaryKey().toString();
		if (isSubclass()) {
			getCourseProviderAreaHome().remove(primaryKey);
		}

		try {
			super.remove();
			java.util.logging.Logger.getLogger(getClass().getName()).info(
					this.getClass().getSimpleName() + 
					" by id: '" + primaryKey + "' removed!");
		} catch (Exception e) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(
					Level.WARNING, 
					"Failed to remove " + this.getClass().getSimpleName() + 
					" by id: '" + primaryKey + 
					"' cause of: ", e);
		}
	}

	/**
	 * 
	 * @param category to search by, skipped if <code>null</code>;
	 * @return entities by criteria or {@link Collections#emptyList()} on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public Collection<Object> ejbFindAll(CourseProviderCategory category) {
		return ejbFindAll(category, false);
	}

	/**
	 * 
	 * @param category to search by, skipped if <code>null</code>;
	 * @param useNullValue show only where 
	 * {@link CourseProviderArea#getCategory()} is <code>null</code>, skipped
	 * if <code>null</code>;
	 * @return entities by criteria or {@link Collections#emptyList()} on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public Collection<Object> ejbFindAll(
			CourseProviderCategory category, 
			boolean useNullValue) {

		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);

		if (category != null) {
			sql.appendWhereEquals(COLUMN_SCHOOL_CATEGORY, category);
		} else if (useNullValue) {
			sql.appendWhereIsNull(COLUMN_SCHOOL_CATEGORY);
		}

		sql.appendOrderBy(COLUMN_NAME);

		try {
			return idoFindPKsByQuery(sql);
		} catch (FinderException e) {
			getLogger().log(
					Level.WARNING, 
					"Failed to get primary keys for " + getClass().getSimpleName() + 
					" by query: '" + sql.toString() + "'");
		}

		return Collections.emptyList();
	}

	public Integer ejbFindSchoolAreaByAreaName(
			CourseProviderCategory category, String name) {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(getEntityName())
		.appendWhereEqualsQuoted(COLUMN_NAME, name);
		if (category != null) {
			sql.appendAndEquals(COLUMN_SCHOOL_CATEGORY, category);
		}

		try {
			return (Integer) super.idoFindOnePKByQuery(sql);
		} catch (FinderException e) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(
					Level.WARNING, "Failed to find primary key for " + 
					CourseProviderArea.class.getName() + " by name: '" + name + 
					"' and " + CourseProviderCategory.class.getName() + ": '" + 
					category.getName() + "'" , e);
		}

		return null;
	}

	/**
	 * 
	 * @param providers to search by, not <code>null</code>;
	 * @return {@link Collection} of {@link CourseProviderArea#getPrimaryKey()}
	 * or {@link Collections#emptyList()} on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public Collection<Object> ejbFindByProviders(Collection<? extends CourseProvider> providers) {
		if (ListUtil.isEmpty(providers)) {
			return Collections.emptyList();
		}

		IDOQuery query = idoQuery();
		query.useDefaultAlias = Boolean.TRUE;
		query.appendSelectAllFrom(this);
		query.appendJoinOn(providers);

		try {
			Collection<Object> primaryKeys = idoFindPKsByQuery(query);
			if (!ListUtil.isEmpty(primaryKeys)) {
				return new HashSet<Object>(primaryKeys);
			}
		} catch (FinderException e) {
			getLogger().log(Level.WARNING, 
					"Failed to get primary keys for " + getInterfaceClass().getSimpleName() + 
					" by query: '" + query.toString() + "'");
		}

		return Collections.emptyList();
	}

	public Collection<Object> ejbFind() {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(getEntityName());
		sql.appendOrderBy(COLUMN_NAME);

		try {
			return super.idoFindPKsBySQL(sql.toString());
		} catch (FinderException e) {
			getLogger().log(
					Level.WARNING, 
					"Failed to get primary keys for " + CourseProviderArea.class.getName());
		}

		return Collections.emptyList();
	}

	/**
	 * 
	 * <p>Check if it is one of extending entities.</p>
	 * @return <code>true</code> if one of subclass, <code>false</code>
	 * otherwise.
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	protected boolean isSubclass() {
		return !getClass().equals(CourseProviderAreaBMPBean.class);
	}

	/*
	 * (non-Javadoc)
	 * @see com.idega.data.GenericEntity#store()
	 */
	@Override
	public void store() throws IDOStoreException {
		if (isSubclass()) {
			CourseProviderArea updatedEntity = getCourseProviderAreaHome().update(
					getPrimaryKey() != null ? getPrimaryKey().toString() : null, 
					getSchoolAreaName(), 
					getSchoolAreaInfo(), 
					getSchoolAreaCity(), 
					getAccountingKey(), 
					getCategory() != null ? getCategory().toString() : null);
			if (updatedEntity != null) {
				setPrimaryKey(updatedEntity.getPrimaryKey().toString());
			}
		}

		super.store();
	}

	/*
	 * (non-Javadoc)
	 * @see com.idega.data.GenericEntity#equals(com.idega.data.IDOEntity)
	 */
	@Override
	public boolean equals(IDOEntity entity) {
		if (!(entity instanceof CourseProviderArea)) {
			return Boolean.FALSE;
		}

		Object entityPrimaryKey = entity.getPrimaryKey();
		if (entityPrimaryKey == null || 
				StringUtil.isEmpty(entityPrimaryKey.toString())) {
			return Boolean.FALSE;
		}

		Object currentPrimaryKey = getPrimaryKey();
		if (currentPrimaryKey == null || 
				StringUtil.isEmpty(currentPrimaryKey.toString())) {
			return Boolean.FALSE;
		}

		if (!currentPrimaryKey.toString().equals(entityPrimaryKey.toString())) {
			return Boolean.FALSE;
		}

		return Boolean.TRUE;
	}

	private CourseProviderAreaHome courseProviderAreaHome = null;

	protected CourseProviderAreaHome getCourseProviderAreaHome() {
		if (this.courseProviderAreaHome == null) {
			try {
				this.courseProviderAreaHome = (CourseProviderAreaHome) IDOLookup
						.getHome(CourseProviderArea.class);
			} catch (IDOLookupException e) {
				getLogger().log(Level.WARNING, 
						"Failed to get " + CourseProviderAreaHome.class.getSimpleName() + 
						" cause of: ", e);
			}
		}

		return this.courseProviderAreaHome;
	}
}
