/*
 * $Id$ Created on Mar 27, 2007
 * 
 * Copyright (C) 2007 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to license terms.
 */
package is.idega.idegaweb.egov.course.presentation;

import is.idega.idegaweb.egov.citizen.presentation.CitizenFinder;
import is.idega.idegaweb.egov.course.CourseConstants;
import is.idega.idegaweb.egov.course.business.CourseBusiness;
import is.idega.idegaweb.egov.course.business.CourseSession;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.IWContext;
import com.idega.user.data.User;

public class CourseParticipantsFinder extends CitizenFinder {

	protected Collection filterResults(IWContext iwc, Collection users) {
		try {
			Collection participants = new ArrayList();
			Collection providers = getSession(iwc).getSchoolsForUser();

			Iterator iter = users.iterator();
			while (iter.hasNext()) {
				User user = (User) iter.next();
				if (getBusiness(iwc).isRegisteredAtProviders(user, providers)) {
					participants.add(user);
				}
			}

			return participants;
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	protected boolean isOfAge(IWContext iwc, User user) {
		return true;
	}

	protected String getHeading(IWContext iwc) {
		IWResourceBundle iwrb = iwc.getIWMainApplication().getBundle(CourseConstants.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc.getCurrentLocale());
		return iwrb.getLocalizedString("student_finder", "Student finder");
	}

	private CourseBusiness getBusiness(IWApplicationContext iwac) {
		try {
			return (CourseBusiness) IBOLookup.getServiceInstance(iwac, CourseBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	private CourseSession getSession(IWUserContext iwuc) {
		try {
			return (CourseSession) IBOLookup.getSessionInstance(iwuc, CourseSession.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}
}