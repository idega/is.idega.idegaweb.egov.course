/**
 * @(#)CourseProviderUsersViewerBean.java    1.0.0 10:34:19 AM
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
package is.idega.idegaweb.egov.course.presentation.bean;

import is.idega.idegaweb.egov.course.business.UserDWR;
import is.idega.idegaweb.egov.course.data.CourseProvider;
import is.idega.idegaweb.egov.course.data.CourseProviderHome;
import is.idega.idegaweb.egov.course.data.CourseProviderUser;
import is.idega.idegaweb.egov.course.data.CourseProviderUserHome;
import is.idega.idegaweb.egov.course.data.CourseProviderUserType;
import is.idega.idegaweb.egov.course.data.CourseProviderUserTypeHome;
import is.idega.idegaweb.egov.course.presentation.CourseProviderUserEditor;
import is.idega.idegaweb.egov.course.presentation.CourseProviderUsersViewer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;

import javax.ejb.FinderException;

import com.idega.builder.business.BuilderLogic;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.EmailHome;
import com.idega.core.contact.data.Phone;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.presentation.IWContext;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;
import com.idega.util.CoreConstants;
import com.idega.util.CoreUtil;
import com.idega.util.ListUtil;
import com.idega.util.StringUtil;

/**
 * <p>JSF managed bean for {@link CourseProviderUsersViewer}</p>
 * <p>You can report about problems to:
 * <a href="mailto:martynas@idega.is">Martynas Stakė</a></p>
 *
 * @version 1.0.0 Oct 28, 2013
 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
 */
public class CourseProviderUsersViewerBean {

	private CourseProviderViewerBean courseProviderViewerBean = null;

	public CourseProviderViewerBean getCourseProviderViewerBean() {
		return courseProviderViewerBean;
	}

	public void setCourseProviderViewerBean(
			CourseProviderViewerBean courseProviderViewerBean) {
		this.courseProviderViewerBean = courseProviderViewerBean;
	}

	public List<CourseProviderUsersAreaGroup> getGroupedUsers() {
		List<CourseProviderUserBean> users = getCourseProviderUsers();
		if (ListUtil.isEmpty(users)) {
			return Collections.emptyList();
		}

		// FIXME Move to recursive search later as for providers...
		Collection<? extends CourseProviderUserType> userTypes = getCourseProviderUserTypeHome().find();

		List<CourseProviderBean> providers = getCourseProviderViewerBean()
				.getCourseProviders();

		/* Users with providers */
		ArrayList<CourseProviderUsersAreaGroup> groupedUsers = new ArrayList<CourseProviderUsersAreaGroup>(providers.size());
		CourseProviderUsersAreaGroup group = null;
		for (CourseProviderBean provider: providers) {
			group = new CourseProviderUsersAreaGroup(provider, users, userTypes);
			if (!ListUtil.isEmpty(group.getUsers())) {
				groupedUsers.add(group);
			}
		}

		/* Users without providers */
		group = new CourseProviderUsersAreaGroup(null, users, userTypes);
		if (!ListUtil.isEmpty(group.getUsers())) {
			groupedUsers.add(group);
		}

		return groupedUsers;
	}

	/**
	 * 
	 * @return {@link CourseProviderUser}s filtered by visibility of 
	 * {@link CourseProvider}s for current {@link User} or 
	 * {@link Collections#emptyList()} on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public Collection<? extends CourseProviderUser> getCourseProviderUserEntities() {
		if (getCourseProviderViewerBean().hasFullAccessRights()) {
			if (CourseProviderUsersViewerBean.class.equals(this.getClass())) {
				return getCourseProviderUserHome().findAllRecursively();
			} else {
				return getCourseProviderUserHome().find();
			}
		} 

		return getCourseProviderUserHome().find(
				getCourseProviderViewerBean().getCourseProviderEntities());
	}

	/**
	 * 
	 * @return all visible {@link CourseProviderUser}s for this {@link User}
	 * or {@link Collections#emptyList()} on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public List<CourseProviderUserBean> getCourseProviderUsers() {
		ArrayList<CourseProviderUserBean> beans = new ArrayList<CourseProviderUserBean>();
		for (CourseProviderUser courseProvider : getCourseProviderUserEntities()) {
			beans.add(new CourseProviderUserBean(courseProvider));
		}

		return beans;
	}

	public String getEditorLink() {
		return BuilderLogic.getInstance().getUriToObject(CourseProviderUserEditor.class);
	}

	public String getHomepageLink() {
		BuilderLogic bl = BuilderLogic.getInstance();
		IWContext iwc = CoreUtil.getIWContext();
		if (iwc == null || !iwc.isLoggedOn()) {
			return null;
		}

		String uri = CoreConstants.PAGES_URI_PREFIX;
		com.idega.core.builder.data.ICPage homePage = bl.getUsersHomePage(iwc.getCurrentUser());
		if (homePage != null)
			uri = uri + homePage.getDefaultPageURI();

		return uri;
	}

	/**
	 * 
	 * <p>Removed relation between {@link CourseProviderUser} 
	 * and {@link CourseProvider} or {@link CourseProviderUser} itself.</p>
	 * @param courseProviderUserId is {@link CourseProviderUser#getPrimaryKey()}
	 * to remove, not <code>null</code>;
	 * @param courseProviderId is {@link CourseProvider#getPrimaryKey()} of
	 * provider to remove relation with or <code>null</code> if 
	 * {@link CourseProviderUser} should be removed instead of relation;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public void remove(String courseProviderUserId, String courseProviderId) {
		if (!StringUtil.isEmpty(courseProviderId) && !StringUtil.isEmpty(courseProviderUserId)) {
			getCourseProviderUserHome().remove(
					courseProviderUserId,
					courseProviderId);
		} else if (!StringUtil.isEmpty(courseProviderUserId)) {
			getCourseProviderUserHome().remove(courseProviderUserId);
		}
	}

	/**
	 * 
	 * <p>Prepares information about {@link User} for jQuery autocomplete
	 * in form.</p>
	 * @param email is {@link Email} to find {@link User}s by, 
	 * not <code>null</code>;
	 * @return {@link User#getName()} and {@link User#getPersonalID()} as key 
	 * and information about {@link User} as value or 
	 * {@link Collections#emptyMap()} on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public Map<String, UserDWR> getAutocompleteMails(String email) {
		if (StringUtil.isEmpty(email) || email.length() < 3) {
			return Collections.emptyMap();
		}

		Collection<User> users = null;
		try {
			users = getUserHome().findUsersByEmail(email, false, true);
		} catch (FinderException e) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(
					Level.WARNING, 
					"Failed to get users by part of email address: '" + email  + "'");
		}

		if (ListUtil.isEmpty(users)) {
			return Collections.emptyMap();
		}
		
		Map<String, UserDWR> addresses = new TreeMap<String, UserDWR>();
		for (User user : users) {
			StringBuilder info = new StringBuilder();
			String name = user.getName();
			if (!StringUtil.isEmpty(name)) {
				info.append(name).append(CoreConstants.SPACE);
			}

			String personalId = user.getPersonalID();
			if (!StringUtil.isEmpty(personalId)) {
				info.append(personalId);
			}

			if (info.length() < 1) {
				continue;
			}			
			
			UserDWR userBean = new UserDWR();
			userBean.setUserEmail(getEmailAddress(user));
			userBean.setUserName(name);
			userBean.setUserHomePhone(getPhoneNumber(user));
			userBean.setUserPK(user.getPrimaryKey().toString());
			
			addresses.put(
					info.toString(), 
					userBean);
		}

		return addresses;
	}

	/**
	 * 
	 * @param user to get {@link Phone} for, not <code>null</code>;
	 * @return parsed home {@link Phone#getNumber()} for {@link User} or 
	 * <code>null</code> on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	protected String getPhoneNumber(User user) {
		if (user == null) {
			return null;
		}

		Phone phone = null;
		try {
			phone = user.getUsersHomePhone();
		} catch (Exception e) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(
					Level.WARNING, 
					"Failed to get phone for user: " + user.getName());
		}

		if (phone == null) {
			return null;
		}

		return phone.getNumber();
	}

	/**
	 * 
	 * <p>Parses {@link Email} for {@link User}.</p>
	 * @param user to get {@link Email} for, not <code>null</code>;
	 * @return queried {@link Email#getEmailAddress()} for {@link User}
	 * or <code>null</code> on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	protected String getEmailAddress(User user) {
		if (user == null) {
			return null;
		}

		Email email = null;
		try {
			email = user.getUsersEmail();
		} catch (Exception e) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(
					Level.WARNING, 
					"Failed to get email for user: " + user.getName());
		}

		if (email == null) {
			return null;
		}

		return email.getEmailAddress();
	}

	/*
	 * EJB homes and business logic
	 */
	
	private UserHome userHome = null;

	protected UserHome getUserHome() {
		if (this.userHome == null) {
			try {
				this.userHome = (UserHome) IDOLookup.getHome(User.class);
			} catch (IDOLookupException e) {
				java.util.logging.Logger.getLogger(getClass().getName()).log(
						Level.WARNING, 
						"Failed to get: " + UserHome.class + 
						" cause of: ", e);
			}
		}

		return this.userHome;
	}

	private EmailHome emailHome = null;

	protected EmailHome getEmailHome() {
		if (this.emailHome == null) {
			try {
				this.emailHome = (EmailHome) IDOLookup.getHome(Email.class);
			} catch (IDOLookupException e) {
				java.util.logging.Logger.getLogger(getClass().getName()).log(
						Level.WARNING, 
						"Failed to get " + EmailHome.class.getName() + 
						" cause of: ", e);
			}
		}

		return this.emailHome;
	}
	
	private CourseProviderUserHome courseProviderUserHome = null;

	protected CourseProviderUserHome getCourseProviderUserHome() {
		if (this.courseProviderUserHome == null) {
			try {
				this.courseProviderUserHome = (CourseProviderUserHome) IDOLookup
						.getHome(CourseProviderUser.class);
			} catch (IDOLookupException e) {
				java.util.logging.Logger.getLogger(getClass().getName()).log(
						Level.WARNING, 
						"Failed to get " + CourseProviderUserHome.class.getName() + 
						" cause of: ", e);
			}
		}

		return courseProviderUserHome;
	}

	private CourseProviderHome courseProviderHome = null;

	protected CourseProviderHome getCourseProviderHome() {
		if (this.courseProviderHome == null) {
			try {
				this.courseProviderHome = (CourseProviderHome) IDOLookup.getHome(CourseProvider.class);
			} catch (IDOLookupException e) {
				java.util.logging.Logger.getLogger(getClass().getName()).log(
						Level.WARNING, 
						"Failed to get " + CourseProviderHome.class.getName() + 
						" cause of: ", e);
			}
		}

		return this.courseProviderHome;
	}
	
	private CourseProviderUserTypeHome courseProviderUserTypeHome = null;

	protected CourseProviderUserTypeHome getCourseProviderUserTypeHome() {
		if (this.courseProviderUserTypeHome == null) {
			try {
				this.courseProviderUserTypeHome = (CourseProviderUserTypeHome) IDOLookup
						.getHome(CourseProviderUserType.class);
			} catch (IDOLookupException e) {
				java.util.logging.Logger.getLogger(getClass().getName()).log(
						Level.WARNING, 
						"Failed to get " + CourseProviderUserTypeHome.class.getName() + 
						" cause of: ", e);
			}
		}

		return this.courseProviderUserTypeHome;
	}
}
