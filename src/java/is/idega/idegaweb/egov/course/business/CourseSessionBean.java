/*
 * $Id$ Created on Aug 10, 2005
 * 
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to license terms.
 */
package is.idega.idegaweb.egov.course.business;

import is.idega.idegaweb.egov.course.CourseConstants;
import is.idega.idegaweb.egov.course.data.CourseProvider;
import is.idega.idegaweb.egov.course.data.CourseProviderType;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Level;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBOSessionBean;
import com.idega.user.data.User;

/**
 * Last modified: $Date$ by $Author$
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision$
 */
public class CourseSessionBean extends IBOSessionBean implements CourseSession {

	private static final long serialVersionUID = -5612636090287766679L;
	private Object iUserPK;
	private CourseProvider iProvider;
	private Boolean iSchoolProvider;
	private boolean allProvidersSelected;

	public boolean isSchoolProvider() throws RemoteException {
		if (iSchoolProvider == null) {
			if (getProvider() != null) {
				iSchoolProvider = new Boolean(true);
			}
			else {
				iSchoolProvider = new Boolean(false);
			}
		}
		
		return iSchoolProvider.booleanValue();
	}

	public CourseProvider getProvider() throws RemoteException {
		if (getUserContext().isLoggedOn()) {
			User user = getUserContext().getCurrentUser();
			Object userID = user.getPrimaryKey();

			if (this.iUserPK != null && this.iUserPK.equals(userID)) {
				if (this.iProvider != null) {
					return this.iProvider;
				}
				else {
					return getSchoolIDFromUser(user);
				}
			}
			else {
				this.iUserPK = userID;
				return getSchoolIDFromUser(user);
			}
		}
		else {
			return null;
		}
	}

	private CourseProvider getSchoolIDFromUser(User user) {
		if (user != null) {
			CourseProvider school = getSchoolUserBusiness()
					.getFirstManagingChildCareForUser(user);
			if (school == null) {
				school = getSchoolUserBusiness()
						.getFirstManagingSchoolForUser(user);
			}

			if (school != null) {
				this.iProvider = school;
			}
		}

		return this.iProvider;
	}

	public void setProvider(CourseProvider provider) {
		this.iProvider = provider;
	}

	public void setProviderPK(Object providerPK) {
		if (providerPK != null) {
			setProvider(getSchoolBusiness().getSchool(providerPK));
		} else {
			setProvider(null);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.business.CourseSession#getSchoolsForUser()
	 */
	public Collection<CourseProvider> getSchoolsForUser() {
		return getSchoolsForUser(null);
	}

	@Override
	public Collection<CourseProvider> getSchoolsForUser(CourseProviderType type) {
		/*
		 * For school logic only...
		 */
		if (getAccessController().hasRole(
				CourseConstants.SUPER_ADMINISTRATOR_ROLE_KEY, 
				getUserContext())) {
			if (type != null) {
				return getSchoolBusiness().findAllSchoolsByType(Arrays.asList(type));
			}

			return getCourseBusiness().getProviders();
		}

		/*
		 * Everything else...
		 */
		return getSchoolBusiness().getProvidersForUser(	getCurrentUser());
	}

	public boolean getIsAllProvidersSelected() {
		return this.allProvidersSelected;
	}

	public void setIsAllProvidersSelected(boolean selected) {
		this.allProvidersSelected = selected;
	}

	private CourseBusiness courseBusiness = null;

	protected CourseBusiness getCourseBusiness() {
		if (this.courseBusiness == null) {
			try {
				this.courseBusiness = (CourseBusiness) IBOLookup
						.getServiceInstance(getIWApplicationContext(), 
								CourseBusiness.class);
			} catch (IBOLookupException ile) {
				getLogger().log(Level.WARNING, 
						"Failed to get " + CourseBusiness.class.getSimpleName() + 
						" cause of: ", ile);
			}
		}

		return this.courseBusiness;
	}

	private CourseProviderBusiness courseProviderBusiness = null;

	protected CourseProviderBusiness getSchoolBusiness() {
		if (this.courseProviderBusiness == null) {
			try {
				this.courseProviderBusiness = (CourseProviderBusiness) IBOLookup
						.getServiceInstance(getIWApplicationContext(), 
								CourseProviderBusiness.class);
			} catch (IBOLookupException ile) {
				getLogger().log(Level.WARNING, 
						"Failed to get " + CourseProviderBusiness.class.getSimpleName() + 
						" cause of: ", ile);
			}
		}

		return this.courseProviderBusiness;
	}

	private CourseProviderUserBusiness courseProviderUserBusiness = null;

	protected CourseProviderUserBusiness getSchoolUserBusiness() {
		if (this.courseProviderUserBusiness == null) {
			try {
				this.courseProviderUserBusiness = (CourseProviderUserBusiness) IBOLookup
						.getServiceInstance(getIWApplicationContext(), 
								CourseProviderUserBusiness.class);
			} catch (IBOLookupException ile) {
				getLogger().log(Level.WARNING, 
						"Failed to get " + CourseProviderUserBusiness.class.getSimpleName() + 
						" cause of: ", ile);
			}
		}

		return this.courseProviderUserBusiness;
	}
}