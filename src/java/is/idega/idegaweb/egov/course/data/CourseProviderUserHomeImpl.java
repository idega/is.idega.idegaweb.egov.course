/**
 * @(#)CourseProviderUserHomeImpl.java    1.0.0 3:29:06 PM
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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDOStoreException;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;
import com.idega.util.CoreUtil;
import com.idega.util.ListUtil;
import com.idega.util.StringUtil;
import com.idega.util.expression.ELUtil;

/**
 * <p>Implementation for {@link CourseProviderUserHome}</p>
 * <p>You can report about problems to: 
 * <a href="mailto:martynas@idega.is">Martynas Stakė</a></p>
 *
 * @version 1.0.0 Oct 23, 2013
 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
 */
public class CourseProviderUserHomeImpl extends IDOFactory implements
		CourseProviderUserHome {

	private static final long serialVersionUID = 6892494193937209725L;

	/* (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.home.CourseProviderUserHome#findForUser(
	 * com.idega.user.data.User)
	 */
	@Override
	public CourseProviderUser findForUser(User user) {
		if (user == null) {
			return null;
		}

		Collection<? extends CourseProviderUser> entities = findByUsers(
				Arrays.asList(user)
				);
		if (ListUtil.isEmpty(entities)) {
			return null;
		}

		return entities.iterator().next();
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderUserHome#findByUsersInSubTypes(java.util.Collection)
	 */
	@Override
	public Collection<? extends CourseProviderUser> findByUsersInSubTypes(Collection<User> users) {
		if (ListUtil.isEmpty(users)) {
			return Collections.emptyList();
		}

		CourseProviderUserBMPBean entity = (CourseProviderUserBMPBean) idoCheckOutPooledEntity();
		if (entity == null) {
			return Collections.emptyList();
		}

		Collection<Object> ids = entity.ejbFindPrimaryKeys(null, users, null);
		if (ListUtil.isEmpty(ids)) {
			return Collections.emptyList();
		}

		return findSubTypesByPrimaryKeysIDO(ids);
	}
	
	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderUserHome#findByUsers(java.util.Collection)
	 */
	@Override
	public Collection<? extends CourseProviderUser> findByUsers(Collection<User> users) {
		if (ListUtil.isEmpty(users)) {
			return Collections.emptyList();
		}

		CourseProviderUserBMPBean entity = (CourseProviderUserBMPBean) idoCheckOutPooledEntity();
		if (entity == null) {
			return Collections.emptyList();
		}

		Collection<Object> ids = entity.ejbFindPrimaryKeys(null, users, null);
		if (ListUtil.isEmpty(ids)) {
			return Collections.emptyList();
		}

		try {
			return getEntityCollectionForPrimaryKeys(ids);
		} catch (FinderException e) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(
					Level.WARNING, 
					"Failed to find " + getEntityInterfaceClass().getName() + 
					"'s by id's: '" + ids + "'");
		}

		return Collections.emptyList();
	}

	/* (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.home.CourseProviderUserHome#findBySchool(
	 * is.idega.idegaweb.egov.course.data.CourseProvider)
	 */
	@Override
	public Collection<? extends CourseProviderUser> findBySchool(
			CourseProvider school) {
		if (school == null ) {
			return Collections.emptyList();
		}

		return find(Arrays.asList(school));
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderUserHome#find(
	 * java.util.Collection)
	 */
	@Override
	public Collection<? extends CourseProviderUser> find(
			Collection<? extends CourseProvider> providers) {
		if (ListUtil.isEmpty(providers)) {
			return Collections.emptyList();
		}

		CourseProviderUserBMPBean entity = (CourseProviderUserBMPBean) idoCheckOutPooledEntity();
		Collection<Object> ids = entity.ejbFindPrimaryKeys(providers, null, null);
		if (ListUtil.isEmpty(ids)) {
			return Collections.emptyList();
		}

		try {
			return getEntityCollectionForPrimaryKeys(ids);
		} catch (FinderException e) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(
					Level.WARNING, 
					"Failed to get " + getEntityInterfaceClass().getName() + 
					"'s by id's: " + ids);
		}

		return Collections.emptyList();
	}
	
	
	/* (non-Javadoc)
	 * @see com.idega.data.IDOFactory#getEntityInterfaceClass()
	 */
	@Override
	protected Class<? extends IDOEntity> getEntityInterfaceClass() {
		return CourseProviderUser.class;
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderUserHome#find()
	 */
	@Override
	public Collection<CourseProviderUser> find() {
		CourseProviderUserBMPBean entity = (CourseProviderUserBMPBean) idoCheckOutPooledEntity();
		Collection<Object> ids = entity.ejbFindPrimaryKeys(null, null, null);
		try {
			return getEntityCollectionForPrimaryKeys(ids);
		} catch (FinderException e) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(
					Level.WARNING, 
					"Failed to get " + getEntityInterfaceClass().getName() + 
					"'s by id's: " + ids);
		}

		return Collections.emptyList();
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderUserHome#find(
	 * java.lang.String)
	 */
	@Override
	public CourseProviderUser find(String courseProviderUserId) {
		if (StringUtil.isEmpty(courseProviderUserId)) {
			return null;
		}

		try {
			return findByPrimaryKeyIDO(courseProviderUserId);
		} catch (FinderException e) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(
					Level.WARNING, 
					"Failed to get " + getEntityInterfaceClass().getName() + 
					"'s by id's: " + courseProviderUserId);
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderUserHome#update(
	 * is.idega.idegaweb.egov.course.data.CourseProviderUser, 
	 * com.idega.user.data.User, 
	 * is.idega.idegaweb.egov.course.data.CourseProviderArea, 
	 * is.idega.idegaweb.egov.course.data.CourseProviderUserType)
	 */
	@Override
	public CourseProviderUser update(
			CourseProviderUser user, 
			User idegaUser,
			CourseProviderUserType userType,
			Collection<? extends CourseProvider> courseProviders, 
			String forcedId) {
		
		/* Creating new one if not exist */
		if (user == null && idegaUser != null) {
			try {
				user = createIDO();
				if (!StringUtil.isEmpty(forcedId)) {
					user.setPrimaryKey(forcedId);
				}
			} catch (CreateException e) {
				java.util.logging.Logger.getLogger(getClass().getName()).log(
						Level.WARNING, 
						"Failed to create " + getEntityInterfaceClass().getName() + 
						" cause of: ", e);
				return null;
			}
		}

		if (idegaUser != null) {
			user.setUser(idegaUser);
		}

		if (userType != null) {
			user.setUserType(userType);
		}

		try {
			user.store();
		} catch (IDOStoreException e) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(
					Level.WARNING, 
					"Failed to store " + getEntityInterfaceClass().getName() + 
					" cause of: ", e);
			return null;
		}

		/* Updating existing course providers */
		if (!ListUtil.isEmpty(courseProviders)) {
			user.removeSchools();
			user.addSchools(courseProviders);
		}

		/* Publishing event of changing role for administrator */
		if (userType != null) {
			ELUtil.getInstance().publishEvent(new CourseProviderUserUpdatedEvent(
					String.valueOf(user.getUserId()), 
					null, null, 
					userType.isSuperAdmin()));
		}

		java.util.logging.Logger.getLogger(getClass().getName()).info(
				getEntityInterfaceClass().getName()  + 
				" by id: '" + user.getPrimaryKey().toString() + 
				"' sucessfully updated!"
				);
		return user;
	}
	
	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderUserHome#update(
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String, 
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public CourseProviderUser update(
			String id, 
			String idegaUserPrimaryKey, 
			String name,
			String phone, 
			String eMail,
			String courseProviderUserTypeId, 
			Collection<String> courseProviderIds, 
			boolean forceId) {

		/* Searching for existing one by primary key */
		User user = null;
		CourseProviderUser courseProviderUser = null;
		if (!StringUtil.isEmpty(id)) {
			courseProviderUser = find(id);
			if (courseProviderUser != null) {
				user = courseProviderUser.getUser();
			}
		}
		
		if (StringUtil.isEmpty(idegaUserPrimaryKey)) {
			idegaUserPrimaryKey = user != null ? user.getPrimaryKey().toString() : null;
		}
		
		/* Updating existing users or creating new one */
		user = getUserBusiness().update(idegaUserPrimaryKey, name, eMail, phone);
		if (user == null) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(
					Level.WARNING, 
					"Failed to create " + getEntityInterfaceClass().getName() + 
					" because failed to create or update " + User.class.getName());
		}

		return update(
				courseProviderUser, 
				user,
				getCourseProviderUserTypeHome().find(courseProviderUserTypeId), 
				getCourseProviderHome().find(courseProviderIds), 
				forceId ? id : null);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderUserHome#findByType(java.util.Collection)
	 */
	@Override
	public Collection<? extends CourseProviderUser> findByType(
			Collection<? extends CourseProviderUserType> types) {
		if (ListUtil.isEmpty(types)) {
			return Collections.emptyList();
		}

		CourseProviderUserBMPBean entity = (CourseProviderUserBMPBean) idoCheckOutPooledEntity();
		if (entity == null) {
			return Collections.emptyList();
		}

		Collection<Object> ids = entity.ejbFindPrimaryKeys(null, null, types);
		try {
			return getEntityCollectionForPrimaryKeys(ids);
		} catch (FinderException e) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(
					Level.WARNING, 
					"Failed to get " + getEntityInterfaceClass().getName() + 
					"'s by id's: " + ids);
		}

		return Collections.emptyList();
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderUserHome#remove(java.lang.String)
	 */
	@Override
	public void remove(String courseProviderUserId) {
		if (!StringUtil.isEmpty(courseProviderUserId)) {
			CourseProviderUser user = find(courseProviderUserId);
			if (user != null) {
				user.remove();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderUserHome#remove(java.lang.String, java.lang.String)
	 */
	@Override
	public void remove(String courseProviderUserId, String courseProviderId) {
		remove(find(courseProviderUserId), 
				getCourseProviderHome().findByPrimaryKeyRecursively(courseProviderId));
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderUserHome#remove(
	 * is.idega.idegaweb.egov.course.data.CourseProviderUser, 
	 * is.idega.idegaweb.egov.course.data.CourseProvider)
	 */
	@Override
	public void remove(CourseProviderUser user, CourseProvider provider) {
		if (user != null) {
			if (provider != null) {
				user.removeSchool(provider);
			} else {
				user.removeSchools();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderUserHome#remove(is.idega.idegaweb.egov.course.data.CourseProviderUser, is.idega.idegaweb.egov.course.data.CourseProviderUserType)
	 */
	@Override
	public void removeType(CourseProviderUser user) {
		if (user != null) {
			Integer type = null;
			user.setUserType(type);

			try {
				user.store();
				ELUtil.getInstance().publishEvent(
						new CourseProviderUserUpdatedEvent(
								String.valueOf(user.getUserId()), 
								null, 
								null, 
								false));
				java.util.logging.Logger.getLogger(getClass().getName()).info(
						"Type is removed from " + user.getClass().getName() + 
						" by id: '" + user.getPrimaryKey() + "'");
			} catch (Exception e) {
				java.util.logging.Logger.getLogger(getClass().getName()).log(
						Level.WARNING, 
						"Failed to remove type for " + user.getClass().getName() + 
						"' by primary key: '" + user.getPrimaryKey() + 
						"' cause of: ", e);
			}
		}
	}

	private UserBusiness userBusiness = null;

	protected UserBusiness getUserBusiness() {
		if (this.userBusiness == null) {
			try {
				this.userBusiness = IBOLookup.getServiceInstance(
						CoreUtil.getIWContext(), UserBusiness.class);
			} catch (IBOLookupException e) {
				java.util.logging.Logger.getLogger(getClass().getName()).log(
						Level.WARNING, 
						"Failed to get " + UserBusiness.class.getName(), e);
			}
		}

		return this.userBusiness;
	}

	private CourseProviderUserTypeHome courseProviderUserTypeHome = null;

	protected CourseProviderUserTypeHome getCourseProviderUserTypeHome() {
		if (this.courseProviderUserTypeHome == null) {
			try {
				this.courseProviderUserTypeHome = (CourseProviderUserTypeHome) IDOLookup.getHome(
						CourseProviderUserType.class);
			} catch (IDOLookupException e) {
				java.util.logging.Logger.getLogger(getClass().getName()).log(
						Level.WARNING, 
						"Failed to get " + CourseProviderUserTypeHome.class.getName() + 
						" cause of: ", e);
			}
		}

		return this.courseProviderUserTypeHome;
	}

	private CourseProviderHome courseProviderHome = null;

	protected CourseProviderHome getCourseProviderHome() {
		if (this.courseProviderHome == null) {
			try {
				this.courseProviderHome = (CourseProviderHome) IDOLookup.getHome(
						CourseProvider.class);
			} catch (IDOLookupException e) {
				java.util.logging.Logger.getLogger(getClass().getName()).log(
						Level.WARNING, 
						"Failed to get " + CourseProviderHome.class.getName() + 
						" cause of: ", e);
			}
		}

		return this.courseProviderHome;
	}
}
