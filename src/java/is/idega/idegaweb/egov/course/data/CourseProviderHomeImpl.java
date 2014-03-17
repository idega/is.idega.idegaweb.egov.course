/**
 * @(#)CourseProviderHome.java    1.0.0 3:30:30 PM
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;
import com.idega.data.IDOHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.ListUtil;
import com.idega.util.StringUtil;
import com.idega.util.expression.ELUtil;

/**
 * <p>Implementation for {@link CourseProviderHome}</p>
 * <p>You can report about problems to:
 * <a href="mailto:martynas@idega.is">Martynas Stakė</a></p>
 *
 * @version 1.0.0 Oct 23, 2013
 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
 * @param <T>
 */
public class CourseProviderHomeImpl extends IDOFactory
		implements CourseProviderHome {

	private static final long serialVersionUID = -2095902709213742089L;

	/* (non-Javadoc)
	 * @see com.idega.data.IDOFactory#getEntityInterfaceClass()
	 */
	@Override
	protected Class<? extends CourseProvider> getEntityInterfaceClass() {
		return CourseProvider.class;
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderHome#findAll()
	 */
	@Override
	public <P extends CourseProvider> Collection<P> find() {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		if (entity == null) {
			return java.util.Collections.emptyList();
		}

		Collection<?> ids = ((CourseProviderBMPBean) entity).ejbFindAll();
		try {
			return this.getEntityCollectionForPrimaryKeys(ids);
		} catch (FinderException e) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(
					Level.WARNING, "Failed to get primary keys of " +
					getEntityInterfaceClass().getSimpleName());
		}

		return java.util.Collections.emptyList();
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderHome#findAllRecursively()
	 */
	@Override
	public <P extends CourseProvider> Collection<P> findAllRecursively() {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		if (entity == null) {
			return java.util.Collections.emptyList();
		}

		Collection<Object> ids = ((CourseProviderBMPBean) entity).ejbFindAll();
		if (ListUtil.isEmpty(ids)) {
			return java.util.Collections.emptyList();
		}

		return findSubTypesByPrimaryKeysIDO(ids);
	}
	
	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderHome#findById(java.lang.String)
	 */
	@Override
	public <T extends IDOEntity> T findByPrimaryKey(String primaryKey) {
		if (StringUtil.isEmpty(primaryKey)) {
			return null;
		}

		try {
			T provider = findByPrimaryKeyIDO(primaryKey);
			return provider;
		} catch (FinderException e) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(
					Level.WARNING,
					"Failed to get " + getEntityInterfaceClass().getClass().getSimpleName() + 
					" by primary key: " + primaryKey);
		}

		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderHome#find(java.lang.String)
	 */
	@Override
	public <T extends CourseProvider> T findByPrimaryKeyRecursively(String primaryKey) {
		if (StringUtil.isEmpty(primaryKey)) {
			return null;
		}

		return findSubTypeByPrimaryKeyIDO(primaryKey);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderHome#find(java.util.Collection)
	 */
	@Override
	public <T extends CourseProvider> Collection<T> find(Collection<String> primaryKeys) {
		if (ListUtil.isEmpty(primaryKeys)) {
		return Collections.emptyList();
	}

		ArrayList<T> providers = new ArrayList<T>(primaryKeys.size());
		for (String key : primaryKeys) {
			T entity = findByPrimaryKeyRecursively(key);
			if (entity != null) {
				providers.add(entity);
			}	
		}

		return providers;
	}

	@Override
	public Collection<? extends CourseProvider> findByPrimaryKeys(Collection<Object> primaryKeys) {
		if (ListUtil.isEmpty(primaryKeys)) {
			return Collections.emptyList();
		}

		ArrayList<String> keys = new ArrayList<String>();
		for (Object primaryKey: primaryKeys) {
			keys.add(primaryKey.toString());
		}

		return find(keys);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderHome#findByTypeRecursively(java.util.Collection)
	 */
	@Override
	public <P extends CourseProvider> Collection<P> findByTypeRecursively(
			Collection<? extends CourseProviderType> types) {
		ArrayList<P> providers = new ArrayList<P>();
		if (!ListUtil.isEmpty(types)) {
			Collection<P> foundProviders = null;
			for (IDOHome home : getHomesForSubtypes()) {
				if (home instanceof CourseProviderHome) {
					foundProviders = ((CourseProviderHome) home).findByType(types);
					if (!ListUtil.isEmpty(foundProviders)) {
						providers.addAll(foundProviders);
					}
				}
			}
		}

		return providers;
	}
	
	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderHome#findByType(java.util.Collection)
	 */
	@Override
	public <P extends CourseProvider> Collection<P> findByType(
			Collection<? extends CourseProviderType> types) {
		if (ListUtil.isEmpty(types)) {
			return Collections.emptyList();
		}

		CourseProviderBMPBean entity = (CourseProviderBMPBean) this.idoCheckOutPooledEntity();
		if (entity == null) {
			return java.util.Collections.emptyList();
		}

		Collection<Object> primaryKeys = entity.ejbFindByType(types);
		try {
			return getEntityCollectionForPrimaryKeys(primaryKeys);
		} catch (FinderException e) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(
					Level.WARNING,
					"Failed to get " + getEntityInterfaceClass().getSimpleName() + 
					"'s by id's: '" + primaryKeys + "' cause of: ", e);
		}

		return Collections.emptyList();
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderHome#findByArea(java.util.Collection)
	 */
	@Override
	public Collection<? extends CourseProvider> findByArea(
			Collection<? extends CourseProviderArea> areas) {
		if (ListUtil.isEmpty(areas)) {
			return Collections.emptyList();
		}

		CourseProviderBMPBean entity = (CourseProviderBMPBean) this.idoCheckOutPooledEntity();
		if (entity == null) {
			return java.util.Collections.emptyList();
		}

		Collection<Object> primaryKeys = entity.ejbFindByArea(areas);
		try {
			return getEntityCollectionForPrimaryKeys(primaryKeys);
		} catch (FinderException e) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(
					Level.WARNING,
					"Failed to get " + getEntityInterfaceClass().getSimpleName() + 
					"'s by id's: '" + primaryKeys + "' cause of: ", e);
		}

		return Collections.emptyList();
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderHome#find(java.util.Collection, java.util.Collection)
	 */
	@Override
	public <P extends CourseProvider> Collection<P> find(
			Collection<? extends CourseProviderArea> areas,
			Collection<? extends CourseProviderType> types) {
		if (ListUtil.isEmpty(areas) || ListUtil.isEmpty(types)) {
			return Collections.emptyList();
		}

		CourseProviderBMPBean entity = (CourseProviderBMPBean) this.idoCheckOutPooledEntity();
		if (entity == null) {
			return java.util.Collections.emptyList();
		}

		Collection<Object> primaryKeys = entity.ejbFind(types, areas);
		try {
			return getEntityCollectionForPrimaryKeys(primaryKeys);
		} catch (FinderException e) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(
					Level.WARNING,
					"Failed to get " + getEntityInterfaceClass().getName() +
					"'s by id's: '" + primaryKeys + "' cause of: ", e);
		}

		return Collections.emptyList();
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderHome#update(is.idega.idegaweb.egov.course.data.CourseProvider, com.idega.user.data.Group, com.idega.core.location.data.PostalCode, is.idega.idegaweb.egov.course.data.CourseProviderArea)
	 */
	@Override
	public CourseProvider update(
			CourseProvider provider,
			Group group,
			PostalCode postalCode,
			CourseProviderArea providerArea,
			boolean notify) {

		String primaryKey = null;
		if (provider != null) {
			primaryKey = provider.getPrimaryKey().toString();
		}

		String name = null;
		String info = null;
		String phone = null;
		String address = null;
		if (group != null) {
			name = group.getName();
			info = group.getDescription();

			Collection<Phone> phones = group.getPhones();
			if (!ListUtil.isEmpty(phones)) {
				phone = phones.iterator().next().getNumber();
			}

			Collection<Address> addresses = null;
			try {
				addresses = group.getAddresses(null);
			} catch (Exception e) {
				java.util.logging.Logger.getLogger(getClass().getName()).log(
						Level.WARNING,
						"Failed to find adresses for group: '" + name +
						"' cause of: ", e);
			}

			if (!ListUtil.isEmpty(addresses)) {
				address = addresses.iterator().next().getStreetAddress();
			}
		}

		String communeId = null;
		String zipArea = null;
		String zipCode = null;
		if (postalCode != null) {
			communeId = postalCode.getCommuneID();
			zipArea = postalCode.getName();
			zipCode = postalCode.getPostalCode();
		}

		/* TODO Think, what to do with these ones */
		String organizationNumber = null;
		String providerId = null;
		String webPage = null;
		String courseProviderAreaId = null;

		return update(primaryKey, name, providerId, communeId, phone,
				webPage, info, organizationNumber , zipArea, zipCode,
				address, courseProviderAreaId, null, null, notify);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderHome#update(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Boolean, java.lang.Boolean, boolean)
	 */
	public CourseProvider update(String primaryKey, String name,
			String providerId, String communeId, String phone, String webPage,
			String info, String organizationNumber, String zipArea,
			String zipCode, String address, String courseProviderAreaId,
			Boolean hasPreCare, Boolean hasPostCare, boolean notify) {
		CourseProvider courseProvider = null;
		if (!StringUtil.isEmpty(primaryKey)) {
			courseProvider = findByPrimaryKey(primaryKey);
		}

		return update(courseProvider, name, providerId, communeId, phone, 
				webPage, info, organizationNumber, zipArea, zipCode, address, 
				courseProviderAreaId, hasPreCare, hasPostCare, notify);
	}


	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderHome#update(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Boolean, java.lang.Boolean)
	 */
	@Override
	public CourseProvider update(CourseProvider courseProvider, String name,
			String providerId, String communeId, String phone, String webPage,
			String info, String organizationNumber, String zipArea,
			String zipCode, String address, String courseProviderAreaId,
			Boolean hasPreCare, Boolean hasPostCare, boolean notify) {
		if (courseProvider == null) {
			try {
				courseProvider = createEntity();
				Logger.getLogger(getClass().getName()).info(
						"Created " + courseProvider.getClass().getSimpleName());
			} catch (CreateException e) {
				Logger.getLogger(getClass().getName()).log(
						Level.WARNING,
						"Failed to create " + getEntityInterfaceClass().getSimpleName() +
						" cause of: ", e);
				return null;
			}
		}

		if(!StringUtil.isEmpty(name)) {
			courseProvider.setSchoolName(name);
		}

		if (!StringUtil.isEmpty(providerId)) {
			courseProvider.setProviderStringId(providerId);
		}

		if (!StringUtil.isEmpty(communeId)) {
			courseProvider.setCommunePK(communeId);
		}

		if (!StringUtil.isEmpty(phone)) {
			courseProvider.setSchoolPhone(phone);
		}

		if (!StringUtil.isEmpty(webPage)) {
			courseProvider.setSchoolWebPage(webPage);
		}

		if (!StringUtil.isEmpty(info)) {
			courseProvider.setSchoolInfo(info);
		}

		if (!StringUtil.isEmpty(organizationNumber)) {
			courseProvider.setOrganizationNumber(organizationNumber);
		}

		if (!StringUtil.isEmpty(zipCode)) {
			courseProvider.setSchoolZipCode(zipCode);
		}

		if (!StringUtil.isEmpty(zipArea)) {
			courseProvider.setSchoolZipArea(zipArea);
		}

		if (!StringUtil.isEmpty(address)) {
			courseProvider.setSchoolAddress(address);
		}

		if (!StringUtil.isEmpty(courseProviderAreaId)) {
			courseProvider.setSchoolAreaId(Integer.valueOf(courseProviderAreaId));
		}

		if (hasPreCare != null) {
			courseProvider.setHasPreCare(hasPreCare);
		}

		if (hasPostCare != null) {
			courseProvider.setHasPostCare(hasPostCare);
		}

		try {
			courseProvider.store();
			java.util.logging.Logger.getLogger(getClass().getName()).info(
					getEntityInterfaceClass().getSimpleName() +
					" by id: " + courseProvider.getPrimaryKey().toString() +
					" stored!");
		} catch (Exception e) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(
					Level.WARNING,
					"Failed to update " + getEntityInterfaceClass().getSimpleName() +
					" cause of: ", e);
			return null;
		}

		if (notify) {
			ELUtil.getInstance().publishEvent(new CourseProviderUpdatedEvent(
					courseProvider.getPrimaryKey().toString(), false));
		}

		return courseProvider;
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderHome#remove(is.idega.idegaweb.egov.course.data.CourseProvider)
	 */
	@Override
	public void remove(CourseProvider provider) {
		if (provider != null) {
			/* Removing relations first, TODO could be optimized a little... */
			Collection<? extends CourseProviderUser> users = getCourseProviderUserHome()
					.findBySchoolRecursively(provider);
			if (!ListUtil.isEmpty(users)) {
				for (CourseProviderUser user : users) {
					getCourseProviderUserHome().remove(user, provider);
				}
			}

			provider.remove();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderHome#remove(
	 * java.lang.String)
	 */
	@Override
	public void remove(String primaryKey) {
		remove((CourseProvider) findByPrimaryKey(primaryKey));
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.SocialServiceCenterEntityHome#findByHandlers(java.util.Collection)
	 */
	@Override
	public <T extends CourseProvider> Collection<T> findByHandlers(Collection<User> users) {
		Collection<? extends CourseProviderUser> userEntities = getCourseProviderUserHome().findByUsers(users);
		if (ListUtil.isEmpty(userEntities)) {
			return Collections.emptyList();
		}

		return findByHandlerEntities(userEntities);
	};

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.SocialServiceCenterEntityHome#findByHandlers(java.util.Collection)
	 */
	@Override
	public <T extends CourseProvider> Collection<T> findByHandlerEntities(
			Collection<? extends CourseProviderUser> handlers) {
		if (ListUtil.isEmpty(handlers)) {
			return Collections.emptyList();
		}

		CourseProviderBMPBean entity = (CourseProviderBMPBean) this.idoCheckOutPooledEntity();
		if (entity == null) {
			return Collections.emptyList();
		}

		Collection<Object> primaryKeys = entity.ejbFindByHandlers(handlers);
		if (ListUtil.isEmpty(primaryKeys)) {
			return Collections.emptyList();
		}

		ArrayList<String> ids = new ArrayList<String>(primaryKeys.size());
		for (Object primaryKey : primaryKeys) {
			ids.add(primaryKey.toString());
		}
		
		return find(ids);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderHome#findAllBySchoolGroup(com.idega.user.data.Group)
	 */
	@Override
	public <T extends CourseProvider> Collection<T> findAllBySchoolGroup(
			Collection<Group> schoolGroups) {
		if (schoolGroups == null) {
			return Collections.emptyList();
		}

		Set<? extends IDOHome> subtypes = getHomesForSubtypes();
		if (ListUtil.isEmpty(subtypes)) {
			return Collections.emptyList();
		}

		Collection<T> providersOfSubType = null;
		ArrayList<T> providers = new ArrayList<T>();
		for (IDOHome home : subtypes) {
			providersOfSubType = ((CourseProviderHome) home)
					.findAllBySchoolGroup(schoolGroups);
			if (!ListUtil.isEmpty(providersOfSubType)) {
				providers.addAll(providersOfSubType);
			}
		}

		return providers;
	}

	private CourseProviderUserHome courseProviderUserHome = null;

	protected CourseProviderUserHome getCourseProviderUserHome() {
		if (this.courseProviderUserHome == null) {
			try {
				this.courseProviderUserHome = (CourseProviderUserHome) IDOLookup.getHome(CourseProviderUser.class);
			} catch (IDOLookupException e) {
				java.util.logging.Logger.getLogger(getClass().getName()).log(
						Level.WARNING,
						"Failed to get " + CourseProviderUserHome.class.getName() +
						" cause of: ", e);
			}
		}

		return this.courseProviderUserHome;
	}
}
