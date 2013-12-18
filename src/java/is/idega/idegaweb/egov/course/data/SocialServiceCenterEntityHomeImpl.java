/**
 * @(#)SocialServiceCenterEntityHomeImpl.java    1.0.0 10:39:27 AM
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.core.location.data.PostalCode;
import com.idega.core.location.data.PostalCodeHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDOStoreException;
import com.idega.idegaweb.IWMainApplication;
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.data.User;
import com.idega.util.ListUtil;
import com.idega.util.StringUtil;


/**
 * <p>Implementation for {@link SocialServiceCenterEntityHome}</p>
 * <p>You can report about problems to: 
 * <a href="mailto:martynas@idega.is">Martynas Stakė</a></p>
 *
 * @version 1.0.0 Dec 10, 2013
 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
 */
public class SocialServiceCenterEntityHomeImpl extends CourseProviderHomeImpl implements SocialServiceCenterEntityHome {

	private static final long serialVersionUID = 9091245803093216897L;

	/* (non-Javadoc)
	 * @see com.idega.data.IDOFactory#getEntityInterfaceClass()
	 */
	@Override
	protected Class<? extends SocialServiceCenterEntity> getEntityInterfaceClass() {
		return SocialServiceCenterEntity.class;
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.SocialServiceCenterEntityHome#update(is.idega.idegaweb.egov.course.data.SocialServiceCenterEntity, com.idega.user.data.Group, java.util.Collection)
	 */
	@Override
	public Collection<? extends SocialServiceCenterEntity> update(
			SocialServiceCenterEntity socialServiceCenter, 
			Group group,
			Collection<PostalCode> postalCodes,
			String providerId, 
			String organizationNumber, 
			String address, 
			String phone, 
			String webPageAddress, 
			String communeId, 
			String courseProviderAreaId) {
		Collection<? extends SocialServiceCenterEntity> entities = null;
		if (socialServiceCenter != null) {
			entities = Arrays.asList(socialServiceCenter);
		} else if (group != null) {
			entities = findByGroupId(group.getPrimaryKey().toString());
			if (ListUtil.isEmpty(entities)) {
				SocialServiceCenterEntity entity = (SocialServiceCenterEntity) update(
						null, group.getName(), null, null, null, null, null, 
						null, null, null, null, null, null, null, true);
				if (entity != null) {
					entities = Arrays.asList(entity);
				}
			}
		} else {
			return Collections.emptyList();
		}

		for (SocialServiceCenterEntity entity : entities) {
			entity.setManagingGroup(group);

			try {
				entity.store();
				Logger.getLogger(getClass().getName()).info(
						SocialServiceCenterEntity.class.getSimpleName() + 
						" by id: " + entity.getPrimaryKey().toString() + 
						" stored!");
			} catch (IDOStoreException e) {
				Logger.getLogger(getClass().getName()).log(Level.WARNING, 
						"Failed to store " + 
						SocialServiceCenterEntity.class.getSimpleName() + 
						" by id: " + entity.getPrimaryKey().toString() + 
						" cause of: ", e);
			}

			entity.setServicedAreas(postalCodes);
			entity.setHandlers(getHandlers(group));

			/* FIXME this is done due to lack of time. It should be optimized
			 * for one update per entity or one update per entities
			 * only.
			 */
			update(entity.getPrimaryKey().toString(), null, providerId, 
					communeId, phone, webPageAddress, null, organizationNumber, 
					null, null, address, courseProviderAreaId, null, null, 
					true);
		}

		return entities;
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.SocialServiceCenterEntityHome#update(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String[])
	 */
	@Override
	public ArrayList<SocialServiceCenterEntity> update(
			String groupId,
			String groupName,
			String groupDescription,
			String groupCity,
			String[] postalCodes,
			String[] roleNames, 
			String providerId, 
			String organizationNumber, 
			String address, 
			String phone, 
			String webPageAddress, 
			String communeId, 
			String courseProviderAreaId) {
		ArrayList<SocialServiceCenterEntity> updatedEntities = new ArrayList<SocialServiceCenterEntity>();

		/* Getting postal codes */
		List<PostalCode> codes = getPostalCodeHome().findUpdatedByPostalCode(
				Arrays.asList(postalCodes));

		/* Getting groups and updating */
		List<Group> groups = getGroupBusiness().update(
				groupId, groupName, groupDescription, groupCity, 
				Arrays.asList(roleNames));

		/* Updating */
		Collection<? extends SocialServiceCenterEntity> entities = null;
		for (Group group : groups) {
			entities = update(null, group, codes, providerId, 
					organizationNumber, address, phone, webPageAddress, 
					communeId, courseProviderAreaId);
			if (!ListUtil.isEmpty(entities)) {
				updatedEntities.addAll(entities);
			}
		}

		return updatedEntities;
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.SocialServiceCenterEntityHome#findByGroupId(java.lang.String)
	 */
	@Override
	public Collection<? extends SocialServiceCenterEntity> findByGroupId(String groupId) {
		if (StringUtil.isEmpty(groupId)) {
			return Collections.emptyList();
		}

		SocialServiceCenterEntityBMPBean entity = (SocialServiceCenterEntityBMPBean) this.idoCheckOutPooledEntity();
		if (entity == null) {
			return Collections.emptyList();
		}

		Collection<Object> primaryKeys = entity.ejbFindByManagingGroupId(groupId);
		if (ListUtil.isEmpty(primaryKeys)) {
			return Collections.emptyList();
		}

		return findByPrimaryKeys(primaryKeys);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderHomeImpl#findByPrimaryKeys(java.util.Collection)
	 */
	@Override
	public Collection<? extends SocialServiceCenterEntity> findByPrimaryKeys(
			Collection<Object> primaryKeys) {
		if (ListUtil.isEmpty(primaryKeys)) {
			return Collections.emptyList();
		}

		Collection<? extends CourseProvider> courseProviders = super.findByPrimaryKeys(primaryKeys);
		if (ListUtil.isEmpty(courseProviders)) {
			return Collections.emptyList();
		}

		ArrayList<SocialServiceCenterEntity> serviceCenters = new ArrayList<SocialServiceCenterEntity>(courseProviders.size());
		for (CourseProvider courseProvider : courseProviders) {
			if (courseProvider instanceof SocialServiceCenterEntity) {
				serviceCenters.add((SocialServiceCenterEntity) courseProvider);
			}
		}

		return serviceCenters;
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.SocialServiceCenterEntityHome#removeByGroupId(java.lang.String)
	 */
	@Override
	public void removeByGroupId(String groupId) {
		Collection<? extends SocialServiceCenterEntity> centers = findByGroupId(groupId);
		for (SocialServiceCenterEntity center : centers) {
			remove(center);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.SocialServiceCenterEntityHome#getUsersByPostalCodes(java.util.Collection)
	 */
	@Override
	public List<User> getUsersByPostalCodes(Collection<String> postalCodes) {
		return getHandlers(getPostalCodeHome().findByPostalCode(postalCodes));
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.SocialServiceCenterEntityHome#getHandlers(com.idega.user.data.Group)
	 */
	@Override
	public List<User> getHandlers(Group group) {
		if (group == null) {
			return Collections.emptyList();
		}

		Collection<User> users = null;
		try {
			users = getGroupBusiness().getUsersRecursive(group);
		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).warning(
					"Failed to get users for group " + group.getName());
		}

		if (ListUtil.isEmpty(users)) {
			return Collections.emptyList();
		}

		return new ArrayList<User>(users);
	}
	
	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.SocialServiceCenterEntityHome#getUsers(is.idega.idegaweb.egov.course.data.SocialServiceCenterEntity)
	 */
	@Override
	public List<User> getHandlers(SocialServiceCenterEntity serviceCenter) {
		if (serviceCenter == null) {
			return Collections.emptyList();
		}

		return getHandlers(serviceCenter.getManagingGroup());
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.SocialServiceCenterEntityHome#getUsers(java.util.Collection)
	 */
	@Override
	public List<User> getHandlers(Collection<PostalCode> postalCodes) {
		List<SocialServiceCenterEntity> serviceCenters = findByPostalCodeEntity(postalCodes);
		if (ListUtil.isEmpty(serviceCenters)) {
			return Collections.emptyList();
		}

		ArrayList<User> handlers = new ArrayList<User>();
		for (SocialServiceCenterEntity serviceCenter : serviceCenters) {
			Collection<User> users = getHandlers(serviceCenter);
			if (!ListUtil.isEmpty(users)) {
				handlers.addAll(users);
			}
		}

		return handlers;
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.SocialServiceCenterEntityHome#findByPostalCode(java.util.Collection)
	 */
	@Override
	public List<SocialServiceCenterEntity> findByPostalCode(
			Collection<String> postalCodes) {
		return findByPostalCodeEntity(getPostalCodeHome().findByPostalCode(postalCodes));
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.SocialServiceCenterEntityHome#findByPostalCodeEntity(java.util.Collection)
	 */
	@Override
	public List<SocialServiceCenterEntity> findByPostalCodeEntity(
			Collection<PostalCode> postalCodes) {
		if (ListUtil.isEmpty(postalCodes)) {
			return Collections.emptyList();
		}

		SocialServiceCenterEntityBMPBean entity = (SocialServiceCenterEntityBMPBean) this.idoCheckOutPooledEntity();
		if (entity == null) {
			return Collections.emptyList();
		}

		Collection<Object> primaryKeys = entity.ejbFindPostalCodeEntities(postalCodes);
		if (ListUtil.isEmpty(primaryKeys)) {
			return Collections.emptyList();
		}

		Collection<SocialServiceCenterEntity> postalCodeEntities = null;
		try {
			postalCodeEntities = getEntityCollectionForPrimaryKeys(primaryKeys);
		} catch (FinderException e) {
			Logger.getLogger(getClass().getName()).log(
					Level.WARNING, 
					"Failed to get " + SocialServiceCenterEntity.class.getSimpleName() + 
					"'s by id's: '" + primaryKeys + "'");
		}

		if (ListUtil.isEmpty(postalCodeEntities)) {
			return Collections.emptyList();
		}

		return new ArrayList<SocialServiceCenterEntity>(postalCodeEntities);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.SocialServiceCenterEntityHome#findByHandlers(java.util.Collection)
	 */
	@Override
	public List<SocialServiceCenterEntity> findByHandlerEntities(
			Collection<? extends SocialServiceCenterHandlerEntity> handlers) {
		if (ListUtil.isEmpty(handlers)) {
			return Collections.emptyList();
		}

		SocialServiceCenterEntityBMPBean entity = (SocialServiceCenterEntityBMPBean) this.idoCheckOutPooledEntity();
		if (entity == null) {
			return Collections.emptyList();
		}

		Collection<Object> primaryKeys = entity.ejbFindByHandlers(handlers);
		if (ListUtil.isEmpty(primaryKeys)) {
			return Collections.emptyList();
		}

		Collection<SocialServiceCenterEntity> serviceCenterEntities = null;
		try {
			serviceCenterEntities = getEntityCollectionForPrimaryKeys(primaryKeys);
		} catch (FinderException e) {
			Logger.getLogger(getClass().getName()).log(
					Level.WARNING, 
					"Failed to get " + SocialServiceCenterEntity.class.getSimpleName() + 
					"'s by id's: '" + primaryKeys + "'");
		}

		if (ListUtil.isEmpty(serviceCenterEntities)) {
			return Collections.emptyList();
		}

		return new ArrayList<SocialServiceCenterEntity>(serviceCenterEntities);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.SocialServiceCenterEntityHome#findByHandlers(java.util.Collection)
	 */
	@Override
	public java.util.List<SocialServiceCenterEntity> findByHandlers(Collection<User> users) {
		Collection<? extends CourseProviderUser> userEntities = getSocialServiceCenterHandlerEntityHome().findByUsers(users);
		if (ListUtil.isEmpty(userEntities)) {
			return Collections.emptyList();
		}

		ArrayList<SocialServiceCenterHandlerEntity> handlerEntities = new ArrayList<SocialServiceCenterHandlerEntity>(userEntities.size());
		for (CourseProviderUser userEntity : userEntities) {
			if (userEntity instanceof SocialServiceCenterHandlerEntity) {
				handlerEntities.add((SocialServiceCenterHandlerEntity) userEntity);
			}
		}

		return findByHandlerEntities(handlerEntities);
	};

	private SocialServiceCenterHandlerEntityHome socialServiceCenterHandlerEntityHome = null;

	protected SocialServiceCenterHandlerEntityHome getSocialServiceCenterHandlerEntityHome() {
		if (this.socialServiceCenterHandlerEntityHome == null) {
			try {
				this.socialServiceCenterHandlerEntityHome = (SocialServiceCenterHandlerEntityHome) IDOLookup.getHome(SocialServiceCenterHandlerEntity.class);
			} catch (IDOLookupException e) {
				Logger.getLogger(getClass().getName()).log(Level.WARNING, 
						"Failed to get " + SocialServiceCenterHandlerEntityHome.class.getSimpleName() + 
						" cause of: ", e);
			}
		}

		return this.socialServiceCenterHandlerEntityHome;
	}

	private GroupHome groupHome = null;

	protected GroupHome getGroupHome() {
		if (this.groupHome == null) {
			try {
				this.groupHome = (GroupHome) IDOLookup.getHome(Group.class);
			} catch (IDOLookupException e) {
				java.util.logging.Logger.getLogger(getClass().getName()).log(
						Level.WARNING, 
						"Failed to get " + GroupHome.class.getSimpleName() + 
						" cause of: ", e);
			}
		}

		return this.groupHome;
	}

	private GroupBusiness groupBusiness = null;

	protected GroupBusiness getGroupBusiness() {
		if (groupBusiness == null) {
			try {
				groupBusiness = IBOLookup.getServiceInstance(
						IWMainApplication.getDefaultIWApplicationContext(),
						GroupBusiness.class);
			} catch (IBOLookupException e) {
				Logger.getLogger(getClass().getName()).log(Level.WARNING, 
						"Failed to get " + GroupBusiness.class.getName() + 
						" cause of: ", e);
			}
		}

		return groupBusiness;
	}

	private PostalCodeHome postalCodeHome = null;

	protected PostalCodeHome getPostalCodeHome() {
		if (this.postalCodeHome == null) {
			try {
				this.postalCodeHome = (PostalCodeHome) IDOLookup.getHome(PostalCode.class);
			} catch (IDOLookupException e) {
				Logger.getLogger(getClass().getName()).log(
						Level.WARNING, 
						"Failed to get " + PostalCodeHome.class.getSimpleName() + 
						" cause of: ", e);
			}
		}

		return this.postalCodeHome;
	}
}
