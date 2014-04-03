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
import is.idega.idegaweb.egov.course.data.CourseProvider;
import is.idega.idegaweb.egov.course.data.CourseProviderType;
import is.idega.idegaweb.egov.course.data.CourseProviderTypeHome;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.IWContext;
import com.idega.user.data.User;
import com.idega.util.StringUtil;

public class CourseParticipantsFinder extends CitizenFinder {

	private String courseProviderTypeId = null;
	
	public String getCourseProviderTypeId() {
		return courseProviderTypeId;
	}

	public void setCourseProviderTypeId(String courseProviderTypeId) {
		this.courseProviderTypeId = courseProviderTypeId;
	}

	public CourseProviderType getType() {
		if (!StringUtil.isEmpty(getCourseProviderTypeId())) {
			return getCourseProviderTypeHome().find(getCourseProviderTypeId());
		}

		return null;
	}

	@Override
	protected Collection<User> filterResults(IWContext iwc, Collection<User> users) {
		Collection<CourseProvider> providers = getSession(iwc).getSchoolsForUser(getType());

		Collection<User> participants = new ArrayList<User>();
		for (Iterator<User> iter = users.iterator(); iter.hasNext();) {
			User user = iter.next();
			if (getBusiness(iwc).isRegisteredAtProviders(user, providers)) {
				participants.add(user);
			}
		}

		return participants;
	}

	@Override
	protected boolean isOfAge(IWContext iwc, User user) {
		return true;
	}

	@Override
	protected String getHeading(IWContext iwc) {
		IWResourceBundle iwrb = iwc.getIWMainApplication().getBundle(CourseConstants.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc.getCurrentLocale());
		return iwrb.getLocalizedString("student_finder", "Student finder");
	}

	private CourseBusiness getBusiness(IWApplicationContext iwac) {
		try {
			return IBOLookup.getServiceInstance(iwac, CourseBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	private CourseSession getSession(IWUserContext iwuc) {
		try {
			return IBOLookup.getSessionInstance(iwuc, CourseSession.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	private CourseProviderTypeHome courseProviderTypeHome = null;

	protected CourseProviderTypeHome getCourseProviderTypeHome() {
		if (this.courseProviderTypeHome == null) {
			try {
				this.courseProviderTypeHome = (CourseProviderTypeHome) IDOLookup
						.getHome(CourseProviderType.class);
			} catch (IDOLookupException e) {
				getLogger().log(Level.WARNING, 
						"Failed to get " + CourseProviderTypeHome.class.getSimpleName() + 
						" cause of: ", e);
			}
		}

		return this.courseProviderTypeHome;
	}
}