/**
 * @(#)CourseProviderUserBMPBean.java    1.0.0 3:45:45 PM
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


import is.idega.idegaweb.egov.course.event.CourseProviderUserUpdatedEvent;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDOQuery;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.data.IDOStoreException;
import com.idega.user.data.User;
import com.idega.util.ListUtil;
import com.idega.util.StringUtil;
import com.idega.util.expression.ELUtil;

/**
 * <p>Implementation for {@link CourseProviderUser} EJB entity.</p>
 * <p>You can report about problems to: 
 * <a href="mailto:martynas@idega.is">Martynas Stakė</a></p>
 *
 * @version 1.0.0 Oct 23, 2013
 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
 */
public class CourseProviderUserBMPBean extends GenericEntity implements
		CourseProviderUser {

	private static final long serialVersionUID = -6809478800798738414L;
	public static final String TABLE_NAME = "cou_course_provider_user";
	public static final String COLUMN_NAME_USER_ID = "IC_USER_ID";
	public static final String COLUMN_NAME_USER_TYPE = "USER_TYPE";
	public static final String COLUMN_CREATION_DATE = "CREATION_DATE";

	private CourseProviderUserHome courseProviderUserHome = null;

	protected CourseProviderUserHome getCourseProviderUserHome() {
		if (this.courseProviderUserHome == null) {
			try {
				this.courseProviderUserHome = (CourseProviderUserHome) IDOLookup
						.getHome(CourseProviderUser.class);
			} catch (IDOLookupException e) {
				java.util.logging.Logger.getLogger(getClass().getName()).log(
						Level.WARNING,
						"Failed to get "
								+ CourseProviderUserHome.class.getSimpleName()
								+ " cause of: ", e);
			}
		}

		return this.courseProviderUserHome;
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderUser#setPrimaryKey(java.lang.String)
	 */
	@Override
	public void setPrimaryKey(String pk) {
		super.setPrimaryKey(pk);
	}

	public void setCreationDate(Date date) {
		setColumn(COLUMN_CREATION_DATE, date);
	}

	/*
	 * (non-Javadoc)
	 * @see com.idega.data.GenericEntity#store()
	 */
	@Override
	public void store() throws IDOStoreException {
		/* if it is one of extending entities, will update that one too */
		if (isSubclass()) {			
			CourseProviderUser updatedEntity = getCourseProviderUserHome().update(
					getPrimaryKey() != null ? getPrimaryKey().toString() : null, 
					null,
					null, 
					null, 
					null,
					null, 
					null, 
					false);

			setPrimaryKey(updatedEntity.getPrimaryKey().toString());
		}

		setCreationDate(new Date(System.currentTimeMillis()));
		super.store();
	}

	/*
	 * (non-Javadoc)
	 * @see com.idega.data.GenericEntity#delete()
	 */
	@Override
	public void delete() throws SQLException {
		super.delete();
		/* if it is one of extending entities, will update that one too */
		if (isSubclass()) {
			getCourseProviderUserHome().remove(getPrimaryKey().toString());
		}
	}

	/* (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderUser#getUser()
	 */
	@Override
	public User getUser() {
		if (!isSubclass()) {
			throw new UnsupportedOperationException(
					"This method should be called from one of sub-types!");
		}

		return (User) getColumnValue(COLUMN_NAME_USER_ID);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderUser#setUserId(int)
	 */
	@Override
	public void setUserId(int userId) {
		if (!isSubclass()) {
			throw new UnsupportedOperationException(
					"This method should be called from one of sub-types!");
		}

		setColumn(COLUMN_NAME_USER_ID, userId);	
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderUser#setUser(com.idega.user.data.User)
	 */
	@Override
	public void setUser(User user) {
		if (!isSubclass()) {
			throw new UnsupportedOperationException(
					"This method should be called from one of sub-types!");
		}
		
		if (user != null && user.getPrimaryKey() != null) {
			try {
				setUserId(Integer.valueOf(user.getPrimaryKey().toString()));
			} catch (NumberFormatException e) {
				getLogger().warning("Failed to convert " + user.getPrimaryKey().toString() + 
						" to " + Integer.class.getName());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderUser#getUserId()
	 */
	@Override
	public int getUserId() {
		if (isSubclass()) {
			return getIntColumnValue(COLUMN_NAME_USER_ID);
		}

		throw new UnsupportedOperationException(
				"This method should be called from one of sub-types!");
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderUser#setUserType(int)
	 */
	@Override
	public void setUserType(Integer userType) {
		if (!isSubclass()) {
			throw new UnsupportedOperationException(
					"This method should be called from one of sub-types!");
		}

		setColumn(COLUMN_NAME_USER_TYPE, userType);	
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderUser#setUserType(java.lang.String)
	 */
	@Override
	public void setUserType(String userType) {
		if (!isSubclass()) {
			throw new UnsupportedOperationException(
					"This method should be called from one of sub-types!");
		}
		
		if (!StringUtil.isEmpty(userType)) {
			try {
				setUserType(Integer.valueOf(userType));
			} catch (NumberFormatException e) {
				getLogger().warning("Failed to convert " + userType + 
						" to " + Integer.class.getName());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderUser#setUserType(is.idega.idegaweb.egov.course.data.CourseProviderUserType)
	 */
	@Override
	public void setUserType(CourseProviderUserType userType) {
		if (!isSubclass()) {
			throw new UnsupportedOperationException(
					"This method should be called from one of sub-types!");
		}

		if (userType != null && userType.getPrimaryKey() != null) {
			setUserType(userType.getPrimaryKey().toString());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderUser#getUserType()
	 */
	@Override
	public Integer getUserType() {
		if (!isSubclass()) {
			throw new UnsupportedOperationException(
					"This method should be called from one of sub-types!");
		}
		
		return getIntColumnValue(COLUMN_NAME_USER_TYPE);	
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderUser#setCourseProvider(is.idega.idegaweb.egov.course.data.CourseProvider)
	 */
	@Override
	public void setCourseProvider(CourseProvider courseProvider) {
		if (!isSubclass()) {
			throw new UnsupportedOperationException(
					"This method should be called from one of sub-types!");
		}

		if (courseProvider != null && courseProvider.getPrimaryKey() != null) {
			try {
				setSchoolId(Integer.valueOf(courseProvider.getPrimaryKey().toString()));
			} catch (NumberFormatException e) {
				getLogger().log(
						Level.WARNING, 
						"Failed to convert: '" + courseProvider.getPrimaryKey().toString() + 
						"' to " + Integer.class.getName());
			}
		}
	}

	/* (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderUser#getSchools()
	 */
	@Override
	public Collection<? extends CourseProvider> getCourseProviders() {
		throw new UnsupportedOperationException(
				"This feature should be implemented in subclasses only!");
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderUser#addSchools(java.util.Collection)
	 */
	@Override
	public void addSchools(Collection<? extends CourseProvider> schools) {
		if (!isSubclass()) {
			throw new UnsupportedOperationException(
					"This method should be called from one of sub-types!");
		}
		
		if (!ListUtil.isEmpty(schools)) {
			ArrayList<String> addedSchoolsIds = new ArrayList<String>(schools.size());
			for (Iterator<? extends CourseProvider> iter = schools.iterator(); iter.hasNext();) {
				CourseProvider entity = iter.next();
				if (entity == null) {
					continue;
				}

				try {
					idoAddTo(entity);
					addedSchoolsIds.add(entity.getPrimaryKey().toString());
					getLogger().info(
							"Added new relation of " + entity.getClass().getName() + 
							" by id: '" + entity.getPrimaryKey().toString() + 
							"' to " + this.getClass().getName() + 
							" by id: '" + this.getPrimaryKey().toString() + "'");
				} catch (IDOAddRelationshipException e) {
					getLogger().log(Level.WARNING, 
							"Failed to add: '" + entity.getClass().getName() + 
							"' by primary key: '" + entity.getPrimaryKey().toString() + 
							"' cause of: ", e);
				}
			}

			ELUtil.getInstance().publishEvent(new CourseProviderUserUpdatedEvent(
					String.valueOf(this.getUserId()), addedSchoolsIds, 
					null, null));
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.idega.data.GenericEntity#remove()
	 */
	@Override
	public void remove() {
		CourseProviderUserUpdatedEvent cpuue = null;

		/* Removing providers, if it is a subclass */
		if (isSubclass()) {
			List<String> providerIds = getIDOUtil().getPrimaryKeys(
					getCourseProviders());
			if (!ListUtil.isEmpty(providerIds)) {
				cpuue = new CourseProviderUserUpdatedEvent(String.valueOf(this
						.getUserId()), null, providerIds, null);
			}
		}

		removeSchools();
		String primarykey = this.getPrimaryKey().toString();
		try {
			super.remove();
			ELUtil.getInstance().publishEvent(cpuue);
			getLogger().info(this.getClass().getName() + 
					" by id: '" + primarykey + 
					"' was removed!"); 
		} catch (RemoveException e) {
			getLogger().log(Level.WARNING, 
					"Failed to remove " + this.getClass().getName() + 
					" by id: '" + primarykey + "' cause of: ", e);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderUser#removeSchool(is.idega.idegaweb.egov.course.data.CourseProvider)
	 */
	@Override
	public void removeSchool(CourseProvider school) {
		if (!isSubclass()) {
			throw new UnsupportedOperationException(
					"This method should be called from one of sub-types!");
		}

		if (school != null) {
			try {
				idoRemoveFrom(school);
				ELUtil.getInstance().publishEvent(new CourseProviderUserUpdatedEvent(
						 String.valueOf(this.getUserId()), 
						 null, 
						 Arrays.asList(school.getPrimaryKey().toString()), null));
				getLogger().info(school.getClass().getName() + 
						" by id: '" + school.getPrimaryKey() + 
						"' is removed from " + this.getClass().getName() + 
						" by id: '" + this.getPrimaryKey().toString() + "'");
			} catch (IDORemoveRelationshipException e) { 
				getLogger().log(Level.WARNING, "Failed to remove '" + 
						school.getClass().getName() + "' by primary key: '" + 
						school.getPrimaryKey() + "' cause of: ", e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderUser#removeSchools()
	 */
	@Override
	public void removeSchools() {
		if (isSubclass()) {
			CourseProviderUserUpdatedEvent cpuue = null;
			List<String> providerIds = getIDOUtil().getPrimaryKeys(this.getCourseProviders());
			if (!ListUtil.isEmpty(providerIds)) {
			cpuue = new CourseProviderUserUpdatedEvent(
					String.valueOf(this.getUserId()), null, providerIds, null);
			}

			try {
				idoRemoveFrom(CourseProvider.class);
				ELUtil.getInstance().publishEvent(cpuue);
				getLogger().info(
						"All relations of " + CourseProvider.class.getName() + 
						" is removed from " + this.getClass().getName() + 
						" by id: '" + this.getPrimaryKey().toString() + "'");
			} catch (IDORemoveRelationshipException e) {
				getLogger().log(Level.WARNING, 
						"Failed to remove all " + CourseProvider.class.getName() + 
						"'s cause of: " , e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderUser#getSchoolId()
	 */
	@Override
	public int getSchoolId() {
		if (!isSubclass()) {
			throw new UnsupportedOperationException(
					"This method should be called from one of sub-types!");
		}
		
		Collection<? extends CourseProvider> providers = getCourseProviders();
		if (ListUtil.isEmpty(providers)) {
			return -1;
		}

		Object primaryKey = providers.iterator().next().getPrimaryKey();
		try {
			return Integer.valueOf(	primaryKey.toString());
		} catch (NumberFormatException e) {
			getLogger().warning("Failed to convert: '" + primaryKey + 
					"' to " + Integer.class.getName());
			return -1;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderUser#setSchoolId(int)
	 */
	@Override
	public void setSchoolId(int schoolId) {
		if (!isSubclass()) {
			throw new UnsupportedOperationException(
					"This method should be called from one of sub-types!");
		}

		try {
			idoAddTo(CourseProvider.class, schoolId);
			ELUtil.getInstance().publishEvent(new CourseProviderUserUpdatedEvent(
					 String.valueOf(this.getUserId()), 
					 Arrays.asList(String.valueOf(schoolId)), 
					 null, null));
			getLogger().info(
					"Added new relation of " + CourseProvider.class.getName() + 
					" by id: '" + schoolId + 
					"' to " + this.getClass().getName() + 
					" by id: '" + this.getPrimaryKey().toString() + "'");
		} catch (IDOAddRelationshipException e) {
			getLogger().log(Level.WARNING, 
					"Failed to add '" + CourseProvider.class.getName() + 
					"' by primary key:'" + schoolId + "' cause of: ", e);
		}
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
		addAttribute(COLUMN_CREATION_DATE, 
				"Dummy column to get this entity working with id only", 
				Date.class);
	}

	/**
	 * 
	 * @param school is {@link CourseProvider} to search by. Not <code>null</code>;
	 * @param userType
	 * @return {@link Collection} of {@link CourseProviderUser#getPrimaryKey()}
	 * found by criteria or {@link Collections#emptyList()} on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public Collection<Object> ejbFindBySchoolAndType(
			CourseProvider school, int userType) {
		if (school == null) {
			return Collections.emptyList();
		}

		return ejbFindPrimaryKeys(school, null, userType);
	}

	/**
	 * 
	 * @param school is {@link CourseProvider}, not <code>null</code>;
	 * @param user is {@link User}, not <code>null</code>;
	 * @return entities by criteria from data source or 
	 * {@link Collections#emptyList()} on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public Collection<Object> ejbFindBySchoolAndUser(CourseProvider school, User user) {
		if (school == null || user == null) {
			return Collections.emptyList();
		}

		return ejbFindPrimaryKeys(Arrays.asList(school), Arrays.asList(user), null);
	}

	/**
	 * 
	 * @param school is {@link CourseProvider}, skipped if <code>null</code>;
	 * @param user is {@link User}, skipped if <code>null</code>;
	 * @param userType skipped if <code>null</code>;
	 * @return first {@link CourseProviderUser#getPrimaryKey()} in {@link Collection}
	 * found by criteria or {@link Collections#emptyList()} on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public Object ejbHomeGetSchoolUserId(CourseProvider school, User user, int userType) {
		if (school == null || user == null) {
			return null;
		}

		Collection<Object> primaryKeys = ejbFindPrimaryKeys(school, user, userType);
		if (ListUtil.isEmpty(primaryKeys)) {
			return null;
		}

		getLogger().info("More than one primary key found by query: '" + 
				getQuery(
						Arrays.asList(school), 
						Arrays.asList(user), userType) + "' will return only first one!");
		return primaryKeys.iterator().next();
	}

	/**
	 * 
	 * @param school is {@link CourseProvider}, not <code>null</code>;
	 * @return primary keys by criteria from data source or 
	 * {@link Collections#emptyList()} on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public Collection<Object> ejbFindBySchool(CourseProvider school) {
		if (school == null) {
			return Collections.emptyList();
		}
		
		return ejbFindPrimaryKeys(Arrays.asList(school), null, null);	
	}

	/**
	 * 
	 * @param school is {@link CourseProvider}, skipped if <code>null</code>;
	 * @param user is {@link User}, skipped if <code>null</code>;
	 * @param userType
	 * @return primary keys by criteria from data source or 
	 * {@link Collections#emptyList()} on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public Collection<Object> ejbFindPrimaryKeys(
			CourseProvider school, 
			User user, 
			int userType) {
		IDOQuery query = getQuery(
				school != null ? Arrays.asList(school) : null, 
				user != null ? Arrays.asList(user) : null,
				userType);
		
		try {
			return idoFindPKsByQuery(query);
		} catch (FinderException e) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(
					Level.WARNING, 
					"Failed to find primary keys of " + this.getClass().getName() + 
					" by query: '" + query.toString() + "'" );
		}

		return Collections.emptyList();	}

	/**
	 * 
	 * <p>Finds primary keys for {@link CourseProviderUser}s</p>
	 * @param school is {@link CourseProvider}, skipped if <code>null</code>;
	 * @param user is {@link User}, skipped if <code>null</code>;
	 * @param userType skipped if <code>null</code>;
	 * @return primary keys by criteria from data source or 
	 * {@link Collections#emptyList()} on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public Collection<Object> ejbFindPrimaryKeys(
			Collection<? extends CourseProvider> school, 
			Collection<? extends User> user, 
			Collection<? extends CourseProviderUserType> userType) {
		IDOQuery query = getQuery(school, user, userType);
		
		try {
			return idoFindPKsByQuery(query);
		} catch (FinderException e) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(
					Level.WARNING, 
					"Failed to find primary keys of " + this.getClass().getName() + 
					" by query: '" + query.toString() + "'" );
		}

		return Collections.emptyList();
	}

	/**
	 * 
	 * <p>Creates query with and relation.</p>
	 * @param school is {@link CourseProvider}, skipped if <code>null</code>;
	 * @param user is {@link User}, skipped if <code>null</code>;
	 * @param userType
	 * @return SQL for idega data object query; 
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	protected IDOQuery getQuery(
			Collection<? extends CourseProvider> school, 
			Collection<? extends User> user, 
			int userType) {
		IDOQuery sql = idoQuery();
		sql.useDefaultAlias = Boolean.TRUE;
		sql.appendSelectAllFrom(this)
		.appendJoinOn(school)
		.appendJoinOn(user);
		sql.appendWhereEquals(COLUMN_NAME_USER_TYPE, userType);
		return sql;
	}
	
	/**
	 * 
	 * <p>Creates query with and relation.</p>
	 * @param school is {@link CourseProvider}, skipped if <code>null</code>;
	 * @param user is {@link User}, skipped if <code>null</code>;
	 * @param userType skipped if <code>null</code>;
	 * @return SQL for idega data object query; 
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	protected IDOQuery getQuery(
			Collection<? extends CourseProvider> school, 
			Collection<? extends User> user, 
			Collection<? extends CourseProviderUserType> userType) {
		IDOQuery sql = idoQuery();
		sql.useDefaultAlias = Boolean.TRUE;
		sql.appendSelectAllFrom(this)
		.appendJoinOn(school)
		.appendJoinOn(user)
		.appendJoinOn(userType);
		return sql;
	}

	/**
	 * 
	 * <p>Check if it is one of extending entities.</p>
	 * @return
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	protected boolean isSubclass() {
		return !getClass().equals(CourseProviderUserBMPBean.class);
	}
}
