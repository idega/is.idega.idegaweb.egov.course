/**
 * @(#)CourseProviderViewerBean.java    1.0.0 10:34:19 AM
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

import is.idega.idegaweb.egov.course.CourseConstants;
import is.idega.idegaweb.egov.course.data.CourseProvider;
import is.idega.idegaweb.egov.course.data.CourseProviderArea;
import is.idega.idegaweb.egov.course.data.CourseProviderAreaHome;
import is.idega.idegaweb.egov.course.data.CourseProviderCategory;
import is.idega.idegaweb.egov.course.data.CourseProviderCategoryHome;
import is.idega.idegaweb.egov.course.data.CourseProviderHome;
import is.idega.idegaweb.egov.course.presentation.CourseProviderEditor;
import is.idega.idegaweb.egov.course.presentation.CourseProvidersViewer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.FinderException;

import com.idega.builder.business.BuilderLogic;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.core.accesscontrol.business.AccessController;
import com.idega.core.location.data.Commune;
import com.idega.core.location.data.CommuneHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.presentation.IWContext;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;
import com.idega.util.CoreConstants;
import com.idega.util.CoreUtil;
import com.idega.util.ListUtil;
import com.idega.util.StringUtil;

/**
 * <p>JSF managed bean for {@link CourseProvidersViewer}</p>
 * <p>You can report about problems to:
 * <a href="mailto:martynas@idega.is">Martynas Stakė</a></p>
 *
 * @version 1.0.0 Oct 28, 2013
 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
 */
public class CourseProviderViewerBean {
	
	public static final String COURSE_PROVIDER_CATEGORY_ID = "course_provider_category";
	
	public Map<String, String> getCourseProviderAreas() {
		String categoryId = CoreUtil.getIWContext()
				.getParameter(COURSE_PROVIDER_CATEGORY_ID);

		CourseProviderCategory courseProviderCategory = null;
		if (!StringUtil.isEmpty(categoryId)) {
			courseProviderCategory = getCourseProviderCategoryHome().find(categoryId);
		}

		Collection<CourseProviderArea> areas = getCourseProviderAreaHome()
				.findAllSchoolAreas(courseProviderCategory);
		if (ListUtil.isEmpty(areas)) {
			return Collections.emptyMap();
		}

		TreeMap<String, String> areasMap = new TreeMap<String, String>();
		for (CourseProviderArea area: areas) {
			areasMap.put(area.getName(), area.getPrimaryKey().toString());
		}

		return areasMap;
	}

	public Map<String, String> getMunicipalities() {
		Collection<Commune> communes = null;
		try {
			communes = getCommuneHome().findAllCommunes();
		} catch (FinderException e) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(
					Level.WARNING, "No communes found!");
		}

		if (ListUtil.isEmpty(communes)) {
			return Collections.emptyMap();
		}

		TreeMap<String, String> communesMap = new TreeMap<String, String>();
		for (Commune commune : communes) {
			communesMap.put(commune.getCommuneName(), commune.getPrimaryKey().toString());
		}

		return communesMap;
	}

	public Map<String, String> getCourseProvidersMap() {
		Collection<CourseProviderBean> providers = getCourseProviders();
		if (ListUtil.isEmpty(providers)) {
			return Collections.emptyMap();
		}

		TreeMap<String, String> providersMap = new TreeMap<String, String>();
		for (CourseProviderBean provider : providers) {
			providersMap.put(
					provider.getName(), 
					provider.getId());
		}

		return providersMap;
	}
	
	public Collection<CourseProvider> getCourseProviderEntities() {
		if (hasFullAccessRights()) {
			return getCourseProviderHome().findAllRecursively();
		} else {
			return getCourseProviderHome().findByHandlers(
					Arrays.asList(getCurrentUser()));
		}
	}

	public List<CourseProviderBean> getCourseProviders() {
		ArrayList<CourseProviderBean> beans = new ArrayList<CourseProviderBean>();
		for (CourseProvider courseProvider : getCourseProviderEntities()) {
			beans.add(new CourseProviderBean(courseProvider));
		}

		return beans;
	}

	public String getEditorLink() {
		return BuilderLogic.getInstance().getUriToObject(CourseProviderEditor.class);
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

	public void remove(String courseProviderId) {
		getCourseProviderHome().remove(getCourseProviderHome().findByPrimaryKeyRecursively(courseProviderId));		
	}

	public boolean hasFullAccessRights() {
		User user = getCurrentUser();
		if (user != null) {
			/*
			 * Courses super administrator
			 */
			if (getAccessController().hasRole(
					user, CourseConstants.SUPER_ADMINISTRATOR_ROLE_KEY)) {
				return Boolean.TRUE;
			}

			/*
			 * Traditional super administrator
			 */
			User administrator = null;
			try {
				administrator = getAccessController().getAdministratorUser();
			} catch (Exception e) {
				Logger.getLogger(getClass().getName()).log(Level.WARNING, 
						"Failed to get administrator cause of: ", e);
			}

			if (user.equals(administrator)) {
				return Boolean.TRUE;
			}
		}

		return Boolean.FALSE;
	}
	
	private AccessController accessController = null;

	protected AccessController getAccessController() {
		if (this.accessController == null) {
			this.accessController = CoreUtil.getIWContext().getAccessController();
		}

		return this.accessController;
	}

	protected User getCurrentUser() {
		return CoreUtil.getIWContext().getCurrentUser();
	}

	private UserBusiness userBusiness = null;

	protected UserBusiness getUserBusiness() {
		if (this.userBusiness == null) {
			try {
				this.userBusiness = IBOLookup.getServiceInstance(
						CoreUtil.getIWContext(), 
						UserBusiness.class);
			} catch (IBOLookupException e) {
				Logger.getLogger(getClass().getName()).log(Level.WARNING, 
						"Failed to get " + UserBusiness.class.getSimpleName()
						+ " cause of: ", e);
			}
		}

		return this.userBusiness;
	}

	private CourseProviderHome courseProviderHome = null;

	protected CourseProviderHome getCourseProviderHome() {
		if (this.courseProviderHome == null) {
			try {
				this.courseProviderHome = (CourseProviderHome) IDOLookup
						.getHome(CourseProvider.class);
			} catch (IDOLookupException e) {
				java.util.logging.Logger.getLogger(getClass().getName()).log(
						Level.WARNING, 
						"Failed to get " + CourseProviderHome.class.getName() + 
						" cause of: ", e);
			}
		}

		return courseProviderHome;
	}

	private CommuneHome communeHome = null;

	protected CommuneHome getCommuneHome() {
		if (this.communeHome == null) {
			try {
				this.communeHome = (CommuneHome) IDOLookup.getHome(Commune.class);
			} catch (IDOLookupException e) {
				java.util.logging.Logger.getLogger(getClass().getName()).log(
						Level.WARNING, 
						"Failed to get " + CommuneHome.class.getName() + 
						" cause of: ", e);
			}
		}

		return this.communeHome;
	}

	private CourseProviderCategoryHome courseProviderCategory = null;

	protected CourseProviderCategoryHome getCourseProviderCategoryHome() {
		if (this.courseProviderCategory == null) {
			try {
				this.courseProviderCategory = (CourseProviderCategoryHome) IDOLookup
						.getHome(CourseProviderCategory.class);
			} catch (IDOLookupException e) {
				java.util.logging.Logger.getLogger(getClass().getName()).log(
						Level.WARNING, "Failed to get " + 
								CourseProviderCategoryHome.class.getName() + 
								" cause of: ", e);
			}
		}

		return this.courseProviderCategory;
	}

	private CourseProviderAreaHome courseProviderAreaHome = null;

	protected CourseProviderAreaHome getCourseProviderAreaHome() {
		if (this.courseProviderAreaHome == null) {
			try {
				this.courseProviderAreaHome = (CourseProviderAreaHome) IDOLookup
						.getHome(CourseProviderArea.class);
			} catch (IDOLookupException e) {
				java.util.logging.Logger.getLogger(getClass().getName()).log(
						Level.WARNING, 
						"Failed to get " + CourseProviderAreaHome.class.getName() + 
						" cause of: ", e);
			}
		}

		return this.courseProviderAreaHome;
	}
}
