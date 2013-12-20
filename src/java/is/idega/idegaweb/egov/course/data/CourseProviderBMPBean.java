/**
 * @(#)CourseProviderBPMBean.java    1.0.0 3:16:43 PM
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


import is.idega.idegaweb.egov.course.event.CourseProviderUpdatedEvent;

import java.sql.Date;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;

import javax.ejb.FinderException;

import com.idega.core.location.data.Commune;
import com.idega.core.location.data.CommuneHome;
import com.idega.core.location.data.PostalCode;
import com.idega.data.GenericEntity;
import com.idega.data.IDOLookup;
import com.idega.data.IDOQuery;
import com.idega.data.IDORelationshipException;
import com.idega.util.ListUtil;
import com.idega.util.StringUtil;
import com.idega.util.expression.ELUtil;

/**
 * <p>Implementation of {@link CourseProvider}</p>
 * <p>You can report about problems to: 
 * <a href="mailto:martynas@idega.is">Martynas Stakė</a></p>
 *
 * @version 1.0.0 Oct 23, 2013
 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
 */
public class CourseProviderBMPBean extends GenericEntity implements
		CourseProvider {
	
	private static final long serialVersionUID = -2826496048886070573L;

	public static final String TABLE_NAME = "cou_course_provider";
	public final static String COLUMN_NAME = "SCHOOL_NAME";
	public final static String COLUMN_INFO = "school_info";
	public final static String COLUMN_ADDRESS = "school_address";
	public final static String COLUMN_ZIPCODE = "zip_code";
	public final static String COLUMN_ZIPAREA = "zip_area";
	public final static String COLUMN_PHONE = "phone";
	public final static String COLUMN_WEB_PAGE = "web_page";
	public final static String COLUMN_COMMUNE = "commune";
	public final static String COLUMN_ORGANIZATION_NUMBER = "organization_number";
	public final static String COLUMN_SCHOOL_AREA = "sch_school_area_id";
	public final static String TERMINATION_DATE = "termination_date";
	public final static String COLUMN_PROVIDER_STRING_ID = "provider_string_id";
	public final static String COLUMN_HAS_PRE_CARE = "has_pre_care";
	public final static String COLUMN_HAS_POST_CARE = "has_post_care";

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	@Override
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addManyToOneRelationship(COLUMN_SCHOOL_AREA, CourseProviderArea.class);
		addAttribute(COLUMN_NAME, "Schoolname", true, true, String.class);
		addAttribute(COLUMN_INFO, "Info", true, true, String.class, 4000);
		addAttribute(COLUMN_ADDRESS, "Address", true, true, String.class, 100);
		addAttribute(COLUMN_ZIPAREA, "Ziparea", true, true, String.class, 100);
		addAttribute(COLUMN_ZIPCODE, "Zipcode", true, true, String.class, 20);
		addAttribute(COLUMN_PHONE, "phone", true, true, String.class, 60);
		addAttribute(COLUMN_WEB_PAGE, "web_page", true, true, String.class, 500);
		addManyToManyRelationShip(CourseProviderType.class);
		addManyToOneRelationship(COLUMN_COMMUNE, Commune.class);
		addAttribute(COLUMN_ORGANIZATION_NUMBER, "organisationsnummer", true, true, String.class, 20);
		addAttribute(TERMINATION_DATE, "termination date", true, true, Date.class);
		addAttribute(COLUMN_PROVIDER_STRING_ID, "Extra provider id", true, true, String.class, 40);
		addAttribute(COLUMN_HAS_PRE_CARE, "Has pre care", Boolean.class);
		addAttribute(COLUMN_HAS_POST_CARE, "Has post care", Boolean.class);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProvider#getCommunePK()
	 */
	@Override
	public Object getCommunePK() {
		Object communeId = getColumnValue(COLUMN_COMMUNE);
		if (communeId == null) {
			try {
				CommuneHome cHome = (CommuneHome) IDOLookup.getHome(Commune.class);
				Commune comm = cHome.findDefaultCommune();
				if (comm != null) {
					setCommunePK(comm.getPrimaryKey());
					store();
					return comm.getPrimaryKey();
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return communeId;
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProvider#getTerminationDate()
	 */
	@Override
	public Date getTerminationDate() {
		return (Date) this.getColumnValue(TERMINATION_DATE);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProvider#setTerminationDate(java.sql.Date)
	 */
	@Override
	public void setTerminationDate(Date date) {
		this.setColumn(TERMINATION_DATE, date);
	}
	
	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProvider#getCommune()
	 */
	@Override
	public Commune getCommune() {
		return (Commune) getColumnValue(COLUMN_COMMUNE);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProvider#getCommuneId()
	 */
	@Override
	public int getCommuneId() {
		return this.getIntColumnValue(COLUMN_COMMUNE);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProvider#setCommunePK(java.lang.Object)
	 */
	@Override
	public void setCommunePK(Object pk) {
		this.setColumn(COLUMN_COMMUNE, pk);
	}
	
	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProvider#getSchoolInfo()
	 */
	@Override
	public String getSchoolInfo() {
		return this.getStringColumnValue(COLUMN_INFO);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProvider#setSchoolInfo(java.lang.String)
	 */
	@Override
	public void setSchoolInfo(String info) {
		this.setColumn(COLUMN_INFO, info);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProvider#getSchoolWebPage()
	 */
	@Override
	public String getSchoolWebPage() {
		return getStringColumnValue(COLUMN_WEB_PAGE);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProvider#setSchoolWebPage(java.lang.String)
	 */
	@Override
	public void setSchoolWebPage(String webPage) {
		setColumn(COLUMN_WEB_PAGE, webPage);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProvider#getSchoolZipArea()
	 */
	@Override
	public String getSchoolZipArea() {
		return this.getStringColumnValue(COLUMN_ZIPAREA);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProvider#setSchoolZipArea(java.lang.String)
	 */
	@Override
	public void setSchoolZipArea(String ziparea) {
		this.setColumn(COLUMN_ZIPAREA, ziparea);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProvider#getSchoolZipCode()
	 */
	@Override
	public String getSchoolZipCode() {
		return this.getStringColumnValue(COLUMN_ZIPCODE);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProvider#setSchoolZipCode(java.lang.String)
	 */
	@Override
	public void setSchoolZipCode(String zipcode) {
		this.setColumn(COLUMN_ZIPCODE, zipcode);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProvider#getSchoolAddress()
	 */
	@Override
	public String getSchoolAddress() {
		return this.getStringColumnValue(COLUMN_ADDRESS);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProvider#setSchoolAddress(java.lang.String)
	 */
	@Override
	public void setSchoolAddress(String address) {
		this.setColumn(COLUMN_ADDRESS, address);
	}
	
	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProvider#getProviderStringId()
	 */
	@Override
	public String getProviderStringId() {
		return this.getStringColumnValue(COLUMN_PROVIDER_STRING_ID);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProvider#setProviderStringId(java.lang.String)
	 */
	@Override
	public void setProviderStringId(String id) {
		this.setColumn(COLUMN_PROVIDER_STRING_ID, id);
	}
	
	/* (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProvider#getSchoolName()
	 */
	@Override
	public String getSchoolName() {
		return this.getStringColumnValue(COLUMN_NAME);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProvider#setSchoolName(java.lang.String)
	 */
	@Override
	public void setSchoolName(String name) {
		this.setColumn(COLUMN_NAME, name);
	}

	/* (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProvider#getSchoolArea()
	 */
	@Override
	public CourseProviderArea getCourseProviderArea() {
		return (CourseProviderArea) getColumnValue(COLUMN_SCHOOL_AREA);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProvider#setSchoolArea(is.idega.idegaweb.egov.course.data.CourseProviderArea)
	 */
	@Override
	public void setSchoolArea(CourseProviderArea area) {
		setColumn(COLUMN_SCHOOL_AREA, area);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProvider#setSchoolAreaId(int)
	 */
	@Override
	public void setSchoolAreaId(int id) {
		setColumn(COLUMN_SCHOOL_AREA, id);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProvider#getSchoolAreaId()
	 */
	@Override
	public int getSchoolAreaId() {
		return getIntColumnValue(COLUMN_SCHOOL_AREA);
	}

	/* (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProvider#getOrganizationNumber()
	 */
	@Override
	public String getOrganizationNumber() {
		return getStringColumnValue(COLUMN_ORGANIZATION_NUMBER);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProvider#setOrganizationNumber(java.lang.String)
	 */
	@Override
	public void setOrganizationNumber(String orgNo) {
		setColumn(COLUMN_ORGANIZATION_NUMBER, orgNo);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProvider#getSchoolPhone()
	 */
	@Override
	public String getSchoolPhone() {
		return this.getStringColumnValue(COLUMN_PHONE);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProvider#setSchoolPhone(java.lang.String)
	 */
	@Override
	public void setSchoolPhone(String phone) {
		this.setColumn(COLUMN_PHONE, phone);
	}

	/* (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProvider#getSchoolTypes()
	 */
	@Override
	public Collection<CourseProviderType> getCourseProviderTypes() {
		try {
			return idoGetRelatedEntities(CourseProviderType.class);
		} catch (IDORelationshipException e) {
			getLogger().log(
					Level.WARNING, 
					"Failed to get course provider types, cause of:", e);
		}

		return Collections.emptyList();
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProvider#setHasPreCare(boolean)
	 */
	@Override
	public void setHasPreCare(boolean hasPreCare) {
		setColumn(COLUMN_HAS_PRE_CARE, hasPreCare);
	}
	
	/* (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProvider#hasPreCare()
	 */
	@Override
	public boolean hasPreCare() {
		return getBooleanColumnValue(COLUMN_HAS_PRE_CARE, false);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProvider#setHasPostCare(boolean)
	 */
	@Override
	public void setHasPostCare(boolean hasPostCare) {
		setColumn(COLUMN_HAS_POST_CARE, hasPostCare);
	}
	
	/* (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProvider#hasPostCare()
	 */
	@Override
	public boolean hasPostCare() {
		return getBooleanColumnValue(COLUMN_HAS_POST_CARE, false);
	}

	/*
	 * (non-Javadoc)
	 * @see com.idega.data.GenericEntity#remove()
	 */
	@Override
	public void remove() {
		try {
			String primaryKey = this.getPrimaryKey().toString();
			super.remove();
			ELUtil.getInstance().publishEvent(new CourseProviderUpdatedEvent(
					primaryKey, true));
			java.util.logging.Logger.getLogger(getClass().getName()).info(
					this.getClass().getName() + 
					" by id: '" + primaryKey + "' removed!");
		} catch (Exception e) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(
					Level.WARNING, 
					"Unable to remove " + this.getClass().getName() + 
					" by primary key: '" + this.getPrimaryKey().toString() + 
					"' cause of:", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	@Override
	public String getEntityName() {
		return TABLE_NAME;
	}

	public Integer ejbFindByOrganizationNumber(String organizationNumber) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM ").append(getEntityName()).append(" ");
		query.append("WHERE ").append(COLUMN_ORGANIZATION_NUMBER).append(" = '").append(organizationNumber).append("' ");
		query.append("AND ").append(TERMINATION_DATE).append(" IS NULL ");
		query.append("OR ").append(TERMINATION_DATE).append(" > '").append(new Date(System.currentTimeMillis())).append("') ");

		try {
			return (Integer) super.idoFindOnePKBySQL(query.toString());
		} catch (FinderException e) {
			getLogger().log(Level.WARNING, 
					"Failed to get course provider id for organization number: '" + 
							organizationNumber + "' and query: '" + query.toString() + "'");
		}

		return null;
	}

	protected Date getCurrentDate() {
		return new Date(System.currentTimeMillis());
	}

	/**
	 * 
	 * @param name is {@link CourseProvider#getName()}, not <code>null</code>;
	 * @param postalCode is {@link PostalCode#getPostalCode()}, not <code>null</code>;
	 * @return {@link Collection} of {@link CourseProvider#getPrimaryKey()}s, 
	 * filtered by criteria or {@link Collections#emptyList()} on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public Collection<Object> ejbFindByNameAndPostalCode(String name, String postalCode) {
		if (StringUtil.isEmpty(name) || StringUtil.isEmpty(postalCode)) {
			return Collections.emptyList();
		}

		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhereEqualsQuoted(COLUMN_NAME, name);
		sql.appendAndEqualsQuoted(COLUMN_ZIPCODE, postalCode);
		try {
			return idoFindPKsByQuery(sql);
		} catch (FinderException e) {
			getLogger().log(Level.WARNING, 
					"Failed to get primary keys for: '" + this.getClass().getName() + 
					"' by name: '" + name + "' and postal code: '" + postalCode + 
					"' cause of: ", e);
		}

		return Collections.emptyList();
	}

	/**
	 * 
	 * @param types to search by, not <code>null</code>;
	 * @return {@link Collection} of {@link CourseProvider#getPrimaryKey()} or
	 * {@link Collections#emptyList()} on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public Collection<Object> ejbFindByType(
			Collection<? extends CourseProviderType> types) {
		if (ListUtil.isEmpty(types)) {
			return Collections.emptyList();
		}

		return ejbFind(types, null);
	}

	/**
	 * 
	 * @param areas to search by, not <code>null</code>;
	 * @return {@link Collection} of {@link CourseProvider#getPrimaryKey()} or
	 * {@link Collections#emptyList()} on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public Collection<Object> ejbFindByArea(
			Collection<? extends CourseProviderArea> areas) {
		if (ListUtil.isEmpty(areas)) {
			return Collections.emptyList();
		}

		return ejbFind(null, areas);
	}

	/**
	 * 
	 * @param types to search by, skipped if <code>null</code>;
	 * @param areas to search by, skipped if <code>null</code>;
	 * @return {@link Collection} of {@link CourseProvider#getPrimaryKey()}s
	 * or {@link Collections#emptyList()} on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public Collection<Object> ejbFind(
			Collection<? extends CourseProviderType> types,
			Collection<? extends CourseProviderArea> areas) {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendJoinOn(types);
		sql.appendJoinOn(areas);
		try {
			return idoFindPKsByQuery(sql);
		} catch (FinderException e) {
			getLogger().log(Level.WARNING, 
					"Failed to get primary keys for '" + this.getClass().getName() + 
					"'' by query: '" + sql.toString() + "'");
		}

		return Collections.emptyList();
	}

	public Collection<Object> ejbFindAll() {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM ").append(getEntityName()).append(" ");
		sb.append("ORDER BY UPPER (").append(COLUMN_NAME).append(") ");

		try {
			return super.idoFindPKsBySQL(sb.toString());
		} catch (FinderException e) {
			getLogger().log(Level.WARNING,
					"Failed to get primary keys for " + 
							this.getClass().getName());
		}

		// some databases doesn't understand UPPER
		sb = new StringBuilder();
		sb.append("SELECT * FROM ").append(getEntityName()).append(" ");
		sb.append("ORDER BY ").append(COLUMN_NAME);
		try {
			return super.idoFindPKsBySQL(sb.toString());
		} catch (FinderException e) {
			getLogger().log(
					Level.WARNING, 
					"Failed to get primary keys for " + 
					this.getClass().getName(), e);
		}

		return java.util.Collections.emptyList();
	}

	/*
	 * (non-Javadoc)
	 * @see com.idega.data.GenericEntity#getName()
	 */
	@Override
	public String getName() {
		return getSchoolName();
	}
}
