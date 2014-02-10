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

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
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

	public Collection<CourseProvider> getSchoolsForUser() throws RemoteException {
		Collection<CourseProvider> schools = new ArrayList<CourseProvider>();
		if (isSchoolProvider()) {
			schools.add(getProvider());
		}
		else {
			if (this.getAccessController().hasRole(CourseConstants.SUPER_ADMINISTRATOR_ROLE_KEY, getUserContext())) {
				schools.addAll(getCourseBusiness().getProviders());
			}
			else if (this.getAccessController().hasRole(CourseConstants.ADMINISTRATOR_ROLE_KEY, getUserContext())) {
				schools.addAll(getCourseBusiness().getProvidersForUser(getUserContext().getCurrentUser()));
			}
		}

		return schools;
	}

	public boolean getIsAllProvidersSelected() {
		return this.allProvidersSelected;
	}
	
	public void setIsAllProvidersSelected(boolean selected) {
		this.allProvidersSelected = selected;
	}
	
	private CourseBusiness getCourseBusiness() {
		try {
			return (CourseBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), CourseBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	private CourseProviderBusiness getSchoolBusiness() {
		try {
			return (CourseProviderBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), CourseProviderBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	private CourseProviderUserBusiness getSchoolUserBusiness() {
		try {
			return (CourseProviderUserBusiness) IBOLookup.getServiceInstance(this.getIWApplicationContext(), CourseProviderUserBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}
}