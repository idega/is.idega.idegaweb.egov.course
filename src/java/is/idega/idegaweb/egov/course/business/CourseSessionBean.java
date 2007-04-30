/*
 * $Id$ Created on Aug 10, 2005
 * 
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to license terms.
 */
package is.idega.idegaweb.egov.course.business;

import is.idega.idegaweb.egov.course.CourseConstants;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.business.SchoolUserBusiness;
import com.idega.block.school.data.School;
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

	private Object iUserPK;
	private School iProvider;
	private Boolean iSchoolProvider;

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

	public School getProvider() throws RemoteException {
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

	private School getSchoolIDFromUser(User user) throws RemoteException {
		if (user != null) {
			try {
				School school = getSchoolUserBusiness().getFirstManagingChildCareForUser(user);
				if (school != null) {
					this.iProvider = school;
				}
			}
			catch (FinderException fe) {
				// No school found for user
			}
		}
		return this.iProvider;
	}

	public void setProvider(School provider) {
		this.iProvider = provider;
	}

	public void setProviderPK(Object providerPK) {
		if (providerPK != null) {
			try {
				setProvider(getSchoolBusiness().getSchool(providerPK));
			}
			catch (RemoteException re) {
				throw new IBORuntimeException(re);
			}
		}
		else {
			setProvider(null);
		}
	}

	public Collection getSchoolsForUser() throws RemoteException {
		Collection schools = new ArrayList();
		if (isSchoolProvider()) {
			schools.add(getProvider());
		}
		else {
			if (this.getAccessController().hasRole(CourseConstants.ADMINISTRATOR_ROLE_KEY, getUserContext())) {
				schools.addAll(getCourseBusiness().getProvidersForUser(getUserContext().getCurrentUser()));
			}
			else if (this.getAccessController().hasRole(CourseConstants.SUPER_ADMINISTRATOR_ROLE_KEY, getUserContext())) {
				schools.addAll(getCourseBusiness().getProviders());
			}
		}

		return schools;
	}

	private CourseBusiness getCourseBusiness() {
		try {
			return (CourseBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), CourseBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	private SchoolBusiness getSchoolBusiness() {
		try {
			return (SchoolBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), SchoolBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	private SchoolUserBusiness getSchoolUserBusiness() {
		try {
			return (SchoolUserBusiness) IBOLookup.getServiceInstance(this.getIWApplicationContext(), SchoolUserBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}
}