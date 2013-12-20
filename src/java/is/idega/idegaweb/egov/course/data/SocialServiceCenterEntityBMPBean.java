/**
 * @(#)SocialServiceCenterEntityBPMBean.java    1.0.0 2:32:07 PM
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

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.core.contact.data.Phone;
import com.idega.core.contact.data.PhoneType;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.AddressHome;
import com.idega.core.location.data.Commune;
import com.idega.core.location.data.CommuneHome;
import com.idega.core.location.data.PostalCode;
import com.idega.core.location.data.PostalCodeHome;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDOQuery;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.CoreUtil;
import com.idega.util.ListUtil;
import com.idega.util.StringUtil;

/**
 * <p>You can report about problems to: 
 * <a href="mailto:martynas@idega.is">Martynas Stakė</a></p>
 *
 * @version 1.0.0 Dec 9, 2013
 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
 */
public class SocialServiceCenterEntityBMPBean extends CourseProviderBMPBean
		implements SocialServiceCenterEntity {

	private static final long serialVersionUID = -255842922090955990L;

	public static final String TABLE_NAME = "COU_CENTER";

	public static final String COLUMN_MANAGING_GROUP = "MANAGING_GROUP";
	public static final String COLUMN_CREATION_DATE = "CREATION_DATE";

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	@Override
	public String getEntityName() {
		return TABLE_NAME;
	}
	
	@Override
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_ORGANIZATION_NUMBER, "Organisation number", String.class, 20);
		addAttribute(COLUMN_CREATION_DATE, "Date created", Date.class);
		addAttribute(TERMINATION_DATE, "Termination date", Date.class);
		addAttribute(COLUMN_WEB_PAGE, "Web page", String.class, 500);
		addAttribute(COLUMN_PROVIDER_STRING_ID, "Extra provider id", String.class, 40);

		/* Group of service center managers */
		addOneToOneRelationship(COLUMN_MANAGING_GROUP, Group.class);

		/* Areas, by postal codes, to be services by this center */
		addManyToManyRelationShip(PostalCode.class);

		/* Means than school is located in this area */
		addManyToOneRelationship(COLUMN_SCHOOL_AREA, CourseProviderArea.class);

		/* Relation for handlers of group */
		addManyToManyRelationShip(SocialServiceCenterHandlerEntity.class);
	}

	@Override
	public String getTableName() {
		return "COU_CENTER";
	}

	public void setCreationDate(Date date) {
		setColumn(COLUMN_CREATION_DATE, date);
	}

	@Override
	public void setHandlers(Collection<User> handlers) {
		removeHandlers();
		addHandlers(handlers);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.SocialServiceCenterEntity#setHandlers(java.util.Collection)
	 */
	@Override
	public void setHandlerEntities(
			Collection<SocialServiceCenterHandlerEntity> handlers) {
		removeHandlers();
		addHandlerEntities(handlers);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.SocialServiceCenterEntity#addHandlers(java.util.Collection)
	 */
	@Override
	public void addHandlerEntities(
			Collection<SocialServiceCenterHandlerEntity> handlers) {
		if (!ListUtil.isEmpty(handlers)) {
			for (SocialServiceCenterHandlerEntity handler : handlers) {
				addHandler(handler);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.SocialServiceCenterEntity#addHandler(is.idega.idegaweb.egov.course.data.SocialServiceCenterHandlerEntity)
	 */
	@Override
	public void addHandler(SocialServiceCenterHandlerEntity entity) {
		Group managingGroup = getManagingGroup();
		if (entity != null && managingGroup != null) {
			try {
				idoAddTo(entity);
				Logger.getLogger(getClass().getName()).info(
						entity.getClass().getSimpleName() + " added to " + 
						this.getClass().getSimpleName());
			} catch (IDOAddRelationshipException e) {
				Logger.getLogger(getClass().getName()).log(Level.WARNING, 
						"Failed to add " + entity.getClass().getSimpleName() + 
						" to " + this.getClass().getSimpleName() + " cause of: ", e);
			}

			managingGroup.addGroup(entity.getUser());

			try {
				managingGroup.store();
				Logger.getLogger(getClass().getName()).info(
						"Handler by id: '" + entity.getUserId() + 
						"' added to manager group ");
			} catch (Exception e) {
				Logger.getLogger(getClass().getName()).log(Level.WARNING, 
						"Failed to add handler by id: '" + entity.getUserId() + 
						"' to manager group, cause of: ", e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.SocialServiceCenterEntity#addHandler(com.idega.user.data.User)
	 */
	@Override
	public void addHandler(User handler) {
		if (handler != null) {
			CourseProviderUser centerHandler = getSocialServiceCenterHandlerEntityHome().update(null, handler, null, null);
			if (centerHandler instanceof SocialServiceCenterHandlerEntity) {
				addHandler((SocialServiceCenterHandlerEntity) centerHandler);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.SocialServiceCenterEntity#addHandlers(java.util.Collection)
	 */
	@Override
	public void addHandlers(Collection<User> handlers) {
		if (ListUtil.isEmpty(handlers)) {
			for (User handler : handlers) {
				addHandler(handler);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.SocialServiceCenterEntity#removeHandlers()
	 */
	@Override
	public void removeHandlers() {
		for (SocialServiceCenterHandlerEntity handler : getHandlers()) {
			removeHandler(handler);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.SocialServiceCenterEntity#removeHandler(is.idega.idegaweb.egov.course.data.SocialServiceCenterHandlerEntity)
	 */
	@Override
	public void removeHandler(SocialServiceCenterHandlerEntity entity) {
		Group managingGroup = getManagingGroup();
		if (entity != null && managingGroup != null) {
			try {
				idoRemoveFrom(entity);
				Logger.getLogger(getClass().getName()).info(
						entity.getClass().getSimpleName() + " removed from " + 
						this.getClass().getSimpleName());
			} catch (IDORemoveRelationshipException e) {
				Logger.getLogger(getClass().getName()).log(Level.WARNING, 
						"Failed to remove " + entity.getClass().getSimpleName() + 
						" to " + this.getClass().getSimpleName() + " cause of: ", e);
			}

			managingGroup.removeGroup(entity.getUser());

			try {
				managingGroup.store();
				Logger.getLogger(getClass().getName()).info(
						"Handler by id: '" + entity.getUserId() + 
						"' removed from manager group ");
			} catch (Exception e) {
				Logger.getLogger(getClass().getName()).log(Level.WARNING, 
						"Failed to remove handler by id: '" + entity.getUserId() + 
						"' from manager group, cause of: ", e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.SocialServiceCenterEntity#getHandlers()
	 */
	@Override
	public Collection<SocialServiceCenterHandlerEntity> getHandlers() {
		try {
			return idoGetRelatedEntities(SocialServiceCenterHandlerEntity.class);
		} catch (IDORelationshipException e) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING, 
					"Failed to get related handlers, cause of: ", e);
		}

		return Collections.emptyList();
	}

	/* (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.SocialServiceCenterEntity#getManagingGroup()
	 */
	@Override
	public Group getManagingGroup() {
		return (Group) getColumnValue(COLUMN_MANAGING_GROUP);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.SocialServiceCenterEntity#setManagingGroup(com.idega.user.data.Group)
	 */
	@Override
	public void setManagingGroup(Group group) {
		if (group != null) {
			setColumn(COLUMN_MANAGING_GROUP, group);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.SocialServiceCenterEntity#getServicedAreas()
	 */
	@Override
	public Collection<PostalCode> getServicedAreas() {
		try {
			return idoGetRelatedEntities(PostalCode.class);
		} catch (IDORelationshipException e) {
			getLogger().log(
					Level.WARNING, 
					"Failed to get " + PostalCode.class.getSimpleName() + 
					" for " + this.getInterfaceClass().getSimpleName() + 
					" by id: '" + this.getPrimaryKey().toString() + 
					"' cause of: ", e);
		}

		return Collections.emptyList();
	}

	public void addServicedAreas(Collection<PostalCode> servicedAreas) {
		if (!ListUtil.isEmpty(servicedAreas)) {
			for (PostalCode servicedArea : servicedAreas) {
				addServicedArea(servicedArea);
			}
		}
	}
	
	public void addServicedArea(PostalCode servicedArea) {
		if (servicedArea != null) {
			try {
				idoAddTo(servicedArea);
				getLogger().info(servicedArea.getClass().getSimpleName() + 
						" by id: '" + servicedArea.getPrimaryKey().toString() + 
						" successfully added to " + getInterfaceClass().getSimpleName() +
						" by id: '" + getPrimaryKey() + "'");
			} catch (IDOAddRelationshipException e) {
				getLogger().log(Level.WARNING, 
						"Failed to add " + servicedArea.getClass().getSimpleName() + 
						" by id: '" + servicedArea.getPrimaryKey() + 
						"' cause of: ", e);
			}
		}
	}

	public void removeServicedArea(PostalCode servicedArea) {
		if (servicedArea != null) {
			try {
				idoRemoveFrom(servicedArea);
				getLogger().info(
						"Relation " + servicedArea.getClass().getSimpleName() + 
						" by id: '" + servicedArea.getPrimaryKey() + 
						"' from " + getInterfaceClass().getSimpleName() + 
						" by id: '" + servicedArea.getPrimaryKey() + 
						" removed!");
			} catch (IDORemoveRelationshipException e) {
				getLogger().log(Level.WARNING,
						"Failed to remove " + servicedArea.getClass().getSimpleName() + 
						" by id: '" + servicedArea.getPrimaryKey() + 
						"' from " + getInterfaceClass().getSimpleName() + 
						" by id: '" + servicedArea.getPrimaryKey() + 
						"' cause of: ", e);
			}
		}
	}

	public void removeServicedAreas() {
		try {
			idoRemoveFrom(PostalCode.class);
			getLogger().info(
					"All " + PostalCode.class + 
					" relations removed from " + getInterfaceClass().getSimpleName() + 
					" by id: '" + getPrimaryKey() + "'");
		} catch (IDORemoveRelationshipException e) {
			getLogger().log(
					Level.WARNING, 
					"Failed to remove all " + PostalCode.class + "'s from " + 
					getInterfaceClass().getSimpleName() + " cause of: ", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.SocialServiceCenterEntity#setServicedAreas(java.util.Collection)
	 */
	@Override
	public void setServicedAreas(Collection<PostalCode> postalCodes) {
		if (!ListUtil.isEmpty(postalCodes)) {
			if (!ListUtil.isEmpty(getServicedAreas())) {
				removeServicedAreas();
			}

			addServicedAreas(postalCodes);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderBMPBean#getCommune()
	 */
	@Override
	public Commune getCommune() {
		return getAddress().getCommune();
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderBMPBean#getCommuneId()
	 */
	@Override
	public int getCommuneId() {
		return getAddress().getCommuneID();
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderBMPBean#getCommunePK()
	 */
	@Override
	public Object getCommunePK() {
		return Integer.valueOf(getAddress().getCommuneID());
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderBMPBean#setCommunePK(java.lang.Object)
	 */
	@Override
	public void setCommunePK(Object pk) {
		if (pk != null) {
			getAddress().setCommuneID(Integer.valueOf(pk.toString()));
		}
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderBMPBean#getSchoolName()
	 */
	@Override
	public String getSchoolName() {		
		Group group = getManagingGroup();
		if (group != null) {
			return group.getName();
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderBMPBean#setSchoolName(java.lang.String)
	 */
	@Override
	public void setSchoolName(String name) {
		if (!StringUtil.isEmpty(name)) {
			Group group = getManagingGroup();
			if (group != null) {
				group.setName(name);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderBMPBean#getSchoolAddress()
	 */
	@Override
	public String getSchoolAddress() {
		Address address = getAddress();
		if (address != null) {
			return address.getStreetAddress();
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderBMPBean#setSchoolAddress(java.lang.String)
	 */
	@Override
	public void setSchoolAddress(String streetAddress) {
		if (!StringUtil.isEmpty(streetAddress)) {
			try {
				getGroupBusiness().updateGroupMainAddressOrCreateIfDoesNotExist(
						getManagingGroup() != null ? Integer.valueOf(getManagingGroup().getPrimaryKey().toString()) : null, 
								streetAddress, null, null, null, null, null);
			} catch (Exception e) {
				Logger.getLogger(getClass().getName()).log(Level.WARNING, 
						"Failed to update main address of group cause of: ", e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderBMPBean#setSchoolZipCode(java.lang.String)
	 */
	@Override
	public void setSchoolZipCode(String zipcode) {
		if (!StringUtil.isEmpty(zipcode)) {
			Address address = getAddress();
			if (address != null) {
				List<PostalCode> postalCodes = getPostalCodeHome().update(null, zipcode, null);
				if (!ListUtil.isEmpty(postalCodes)) {
					address.setPostalCode(postalCodes.iterator().next());
					address.store();
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderBMPBean#getSchoolZipCode()
	 */
	@Override
	public String getSchoolZipCode() {
		return getPostalCode().getPostalCode();
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderBMPBean#getSchoolZipArea()
	 */
	@Override
	public String getSchoolZipArea() {
		return getPostalCode().getName();
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderBMPBean#setSchoolZipArea(java.lang.String)
	 */
	@Override
	public void setSchoolZipArea(String ziparea) {
		throw new UnsupportedOperationException(
				"Postal codes are usually set via national register!");
	}
	
	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderBMPBean#hasPostCare()
	 */
	@Override
	public boolean hasPostCare() {
		throw new UnsupportedOperationException(
				"Social service centers do not provide post care.");
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderBMPBean#setHasPostCare(boolean)
	 */
	@Override
	public void setHasPostCare(boolean hasPostCare) {
		throw new UnsupportedOperationException(
				"Social service centers do not provide post care.");
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderBMPBean#hasPreCare()
	 */
	@Override
	public boolean hasPreCare() {
		throw new UnsupportedOperationException(
				"Social service centers do not provide pre care.");
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderBMPBean#setHasPreCare(boolean)
	 */
	@Override
	public void setHasPreCare(boolean hasPreCare) {
		throw new UnsupportedOperationException(
				"Social service centers do not provide pre care.");
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderBMPBean#getSchoolPhone()
	 */
	@Override
	public String getSchoolPhone() {
		Phone phone = getPhone();
		if (phone != null) {
			return phone.getNumber();
		}

		return null;
	}

	public Phone getPhone() {
		try {
			return getGroupBusiness().getGroupPhone(
					getManagingGroup(), 
					PhoneType.HOME_PHONE_ID);
		} catch (RemoteException e) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(
					Level.WARNING, 
					"Failed to get social service center group, cause of: ", e);
		}

		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderBMPBean#setSchoolPhone(java.lang.String)
	 */
	@Override
	public void setSchoolPhone(String phone) {
		try {
			getGroupBusiness().updateGroupPhone(
					getManagingGroup(), PhoneType.HOME_PHONE_ID, phone);
		} catch (Exception e) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(
					Level.WARNING, 
					"Failed to update social service center phone cause of: ", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderBMPBean#getSchoolInfo()
	 */
	@Override
	public String getSchoolInfo() {
		Group group = getManagingGroup();
		if (group != null) {
			return group.getDescription();
		}
	
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderBMPBean#setSchoolInfo(java.lang.String)
	 */
	@Override
	public void setSchoolInfo(String info) {
		Group group = getManagingGroup();
		if (group != null) {
			group.setDescription(info);
			group.store();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.idega.data.GenericEntity#store()
	 */
	@Override
	public void store() {
		try {
			setCreationDate(getCurrentDate());
			super.store();
		} catch (Exception e) {
			getLogger().log(Level.WARNING, 
					"Failed to store " + getInterfaceClass() + 
					" by id: " + getPrimaryKey() +
					" cause of: ", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.SocialServiceCenterEntity#getPostalCode()
	 */
	@Override
	public PostalCode getPostalCode() {
		Address address = getAddress();
		if (address != null) {
			return address.getPostalCode();
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.SocialServiceCenterEntity#getAddress()
	 */
	@Override
	public Address getAddress() {
		try {
			return getGroupBusiness().getGroupMainAddress(getManagingGroup());
		} catch (Exception e) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(
					Level.WARNING, 
					"Failed to get group main address cause of: ", e);
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderBMPBean#remove()
	 */
	public void remove() {
		removeHandlers();
		removeServicedAreas();
		super.remove();
	};

	/**
	 * 
	 * @param groupId is {@link Group#getPrimaryKey()}, not <code>null</code>;
	 * @return primary key for {@link SocialServiceCenterEntity} by given criteria or <code>null</code>
	 * on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public Collection<Object> ejbFindByManagingGroupIds(Collection<String> groupIds) {
		if (ListUtil.isEmpty(groupIds)) {
			return Collections.emptyList();
		}

		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhere(COLUMN_MANAGING_GROUP).appendInForStringCollectionWithSingleQuotes(groupIds);

		try {
			return idoFindPKsByQuery(sql);
		} catch (FinderException e) {
			getLogger().log(Level.WARNING, 
					"Failed to get primary keys for '" + this.getClass().getName() + 
					"'' by query: '" + sql.toString() + "'");
		}

		return Collections.emptyList();
	}

	/**
	 * 
	 * @param postalCodes to search by, not <code>null</code>;
	 * @return {@link Collection} of {@link SocialServiceCenterEntity#getPrimaryKey()}
	 * by given criteria;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public Collection<Object> ejbFindPostalCodeEntities(
			Collection<PostalCode> postalCodes) {
		if (ListUtil.isEmpty(postalCodes)) {
			return Collections.emptyList();
		}

		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendJoinOn(postalCodes);

		try {
			return idoFindPKsByQuery(sql);
		} catch (FinderException e) {
			getLogger().log(Level.WARNING, 
					"Failed to get primary keys for '" + this.getClass().getName() + 
					"'' by query: '" + sql.toString() + "'");
		}

		return Collections.emptyList();
	}

	/**
	 * 
	 * @param handlers to search by, not <code>null</code>;
	 * @return {@link Collection} of {@link SocialServiceCenterEntity#getPrimaryKey()}
	 * or {@link Collections#emptyList()} on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public Collection<Object> ejbFindByHandlers(
			Collection<? extends SocialServiceCenterHandlerEntity> handlers) {
		if (ListUtil.isEmpty(handlers)) {
			return Collections.emptyList();
		}

		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendJoinOn(handlers);

		try {
			return idoFindPKsByQuery(sql);
		} catch (FinderException e) {
			getLogger().log(Level.WARNING, 
					"Failed to get primary keys for '" + this.getClass().getName() + 
					"'' by query: '" + sql.toString() + "'");
		}

		return Collections.emptyList();
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseProviderBMPBean#ejbFindAll()
	 */
	@Override
	public Collection<Object> ejbFindAll() {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		try {
			return idoFindPKsByQuery(sql);
		} catch (FinderException e) {
			getLogger().log(Level.WARNING, 
					"Failed to get primary keys for '" + this.getClass().getName() + 
					"'' by query: '" + sql.toString() + "'");
		}

		return Collections.emptyList();
	}

	private PostalCodeHome postalCodeHome = null;

	protected PostalCodeHome getPostalCodeHome() {
		if (this.postalCodeHome == null) {
			try {
				this.postalCodeHome = (PostalCodeHome) IDOLookup.getHome(PostalCode.class);
			} catch (IDOLookupException e) {
				java.util.logging.Logger.getLogger(getClass().getName()).log(
						Level.WARNING, 
						"Failed to get " + PostalCodeHome.class.getSimpleName() + 
						" cause of: ", e);
			}
		}

		return this.postalCodeHome;
	}

	private AddressHome addressHome = null;

	protected AddressHome getAddressHome() {
		if (this.addressHome == null) {
			try {
				this.addressHome = (AddressHome) IDOLookup.getHome(Address.class);
			} catch (IDOLookupException e) {
				getLogger().log(Level.WARNING, 
						"Failed to get " + AddressHome.class.getSimpleName() + 
						" cause of: ", e);
			}
		}

		return this.addressHome;
	}

	private CommuneHome communeHome = null;

	protected CommuneHome getCommuneHome() {
		if (this.communeHome == null) {
			try {
				this.communeHome = (CommuneHome) IDOLookup.getHome(Commune.class);
			} catch (IDOLookupException e) {
				java.util.logging.Logger.getLogger(getClass().getName()).log(
						Level.WARNING, 
						"Failed to get " + CommuneHome.class.getSimpleName() + 
						" cause of: ", e);
			}
		}

		return this.communeHome;
	}

	private GroupBusiness groupBusiness = null;

	protected GroupBusiness getGroupBusiness() {
		if (this.groupBusiness == null) {
			try {
				this.groupBusiness = IBOLookup.getServiceInstance(
						CoreUtil.getIWContext(), GroupBusiness.class);
			} catch (IBOLookupException e) {
				java.util.logging.Logger.getLogger(getClass().getName()).log(
						Level.WARNING, 
						"Failed to get " + GroupBusiness.class.getSimpleName() + 
						" cause of: ", e);
			}
		}

		return this.groupBusiness;
	}

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
}
