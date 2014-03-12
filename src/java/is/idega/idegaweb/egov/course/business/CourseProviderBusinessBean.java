/**
 * @(#)CourseProviderBusinessBean.java    1.0.0 2:45:57 PM
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
package is.idega.idegaweb.egov.course.business;

import is.idega.idegaweb.egov.course.CourseConstants;
import is.idega.idegaweb.egov.course.data.CourseProvider;
import is.idega.idegaweb.egov.course.data.CourseProviderArea;
import is.idega.idegaweb.egov.course.data.CourseProviderAreaHome;
import is.idega.idegaweb.egov.course.data.CourseProviderCategory;
import is.idega.idegaweb.egov.course.data.CourseProviderCategoryHome;
import is.idega.idegaweb.egov.course.data.CourseProviderHome;
import is.idega.idegaweb.egov.course.data.CourseProviderType;
import is.idega.idegaweb.egov.course.data.CourseProviderTypeHome;
import is.idega.idegaweb.egov.course.data.CourseProviderUser;
import is.idega.idegaweb.egov.course.data.CourseProviderUserHome;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.data.User;
import com.idega.util.CoreUtil;
import com.idega.util.ListUtil;
import com.idega.util.StringUtil;

/**
 * <p>Implementation for {@link CourseProviderBusiness}</p>
 * <p>You can report about problems to:
 * <a href="mailto:martynas@idega.is">Martynas Stakė</a></p>
 *
 * @version 1.0.0 Oct 23, 2013
 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
 */
public class CourseProviderBusinessBean extends IBOServiceBean implements
		CourseProviderBusiness {

	private static final long serialVersionUID = -859935297306293052L;

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.business.CourseProviderBusiness#hasFullAccessRights()
	 */
	@Override
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

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.business.CourseProviderBusiness#getAreasForCurrentUser()
	 */
	@Override
	public Collection<CourseProviderArea> getAreasForCurrentUser() {
		User user = getCurrentUser();
		if (user != null) {
			if (hasFullAccessRights()) {
				return getCourseProviderAreaHome().findAll();
			}

			return getCourseProviderAreaHome().findAllByProviders(
					getProvidersForCurrentUser()
					);
		}

		return Collections.emptyList();

	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.business.CourseProviderBusiness#getProvidersForCurrentUser()
	 */
	@Override
	public Collection<CourseProvider> getProvidersForCurrentUser() {
		User user = getCurrentUser();
		if (user != null) {
			if (hasFullAccessRights()) {
				return getCourseProviderHome().findAllRecursively();
			}

			return getProvidersForUser(user);
		}

		return Collections.emptyList();
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.business.CourseProviderBusiness#getProvidersByType(java.lang.String)
	 */
	@Override
	public Collection<CourseProvider> getProvidersByType(String typePrimaryKey) {
		Collection<CourseProvider> courseProviders = getCourseProviderHome().findAllRecursively();

		CourseProviderType type = getCourseProviderTypeHome().find(typePrimaryKey);
		if (type == null) {
			return courseProviders;
		}

		Collection<CourseProviderType> courseProviderTypes = null;
		ArrayList<CourseProvider> matchedResult = new ArrayList<CourseProvider>(
				courseProviders.size());
		for (CourseProvider courseProvider : courseProviders) {
			courseProviderTypes = courseProvider.getCourseProviderTypes();
			if (!ListUtil.isEmpty(courseProviderTypes) && courseProviderTypes.contains(type)) {
				matchedResult.add(courseProvider);
			}
		}

		return matchedResult;
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.business.CourseBusiness#getHandledCourseProviders(com.idega.user.data.User)
	 */
	@Override
	public Collection<CourseProvider> getHandledCourseProviders(User user) {
		return getProvidersForUser(user);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.business.CourseBusiness#getProvidersForUser(com.idega.user.data.User)
	 */
	@Override
	public Collection<CourseProvider> getProvidersForUser(User user) {
		if (user == null) {
			Collections.emptyList();
		}

		Collection<? extends CourseProviderUser> schoolUsers = getCourseProviderUserHome()
				.findByUsersInSubTypes(Arrays.asList(user));
		if (ListUtil.isEmpty(schoolUsers)) {
			return Collections.emptyList();
		}

		Collection<CourseProvider> courseProviders = new ArrayList<CourseProvider>();
		for (CourseProviderUser schoolUser: schoolUsers) {
			Collection<? extends CourseProvider> providers = schoolUser
					.getCourseProviders();
			if (!ListUtil.isEmpty(providers)) {
				courseProviders.addAll(providers);
			}
		}

		return courseProviders;
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.business.CourseProviderBusiness#getProvidersByUserAndType(com.idega.user.data.User, java.lang.String)
	 */
	@Override
	public Collection<CourseProvider> getProvidersByUserAndType(User user,
			String typePrimaryKey) {
		CourseProviderType type = getCourseProviderTypeHome().find(typePrimaryKey);
		if (type == null) {
			return getProvidersForUser(user);
		}

		Collection<CourseProviderType> providerTypes = null;
		Collection<CourseProvider> providersByUser = getProvidersForUser(user);
		ArrayList<CourseProvider> matchedResults = new ArrayList<CourseProvider>(
				providersByUser.size());
		for (CourseProvider provider : providersByUser) {
			providerTypes = provider.getCourseProviderTypes();
			if (!ListUtil.isEmpty(providerTypes) && providerTypes.contains(type)) {
				matchedResults.add(provider);
			}
		}

		return matchedResults;
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.business.CourseProviderBusiness#getProvidersByAreas(java.util.Collection)
	 */
	@Override
	public Map<CourseProviderArea, List<CourseProvider>> getProvidersByAreas(
			Collection<CourseProvider> courseProviders) {
		if (ListUtil.isEmpty(courseProviders)) {
			return Collections.emptyMap();
		}

		TreeMap<CourseProviderArea, List<CourseProvider>> result = null;
		result = new TreeMap<CourseProviderArea, List<CourseProvider>>();
		for (CourseProvider provider : courseProviders) {
			/* Skipping providers without areas for now... */
			CourseProviderArea area = provider.getCourseProviderArea();
			if (area != null) {
				List<CourseProvider> providersList = result.get(area);
				if (ListUtil.isEmpty(providersList)) {
					providersList = new ArrayList<CourseProvider>();
					result.put(area, providersList);
				}

				providersList.add(provider);
			}
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.business.CourseProviderBusiness#getCourseProviderTypes(java.util.Collection)
	 */
	@Override
	public Collection<CourseProviderType> getCourseProviderTypes(
			Collection<? extends CourseProvider> courseProviders) {
		if (ListUtil.isEmpty(courseProviders)) {
			return Collections.emptyList();
		}

		Collection<CourseProviderType> types = null;
		ArrayList<CourseProviderType> providerTypes = new ArrayList<CourseProviderType>();
		for (CourseProvider provider : courseProviders) {
			types = provider.getCourseProviderTypes();
			if (!ListUtil.isEmpty(types)) {
				providerTypes.addAll(types);
			}
		}

		return providerTypes;
	}

	/* (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.business.CourseProviderBusiness#getSchool(java.lang.Object)
	 */
	@Override
	public CourseProvider getSchool(Object primaryKey) {
		if (primaryKey == null) {
			return null;
		}

		CourseProvider provider = null;
		for (CourseProviderHome home : getCourseProviderHomes()) {
			provider = home.findByPrimaryKeyRecursively(primaryKey.toString());
			if (provider != null) {
				return provider;
			}
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.business.CourseProviderBusiness#findAllSchoolsByType(java.util.Collection)
	 */
	@Override
	public <P extends CourseProvider> Collection<P> findAllSchoolsByType(Collection<? extends CourseProviderType> typeIds) {
		if (ListUtil.isEmpty(typeIds)) {
			return Collections.emptyList();
		}

		ArrayList<P> courseProviders = new ArrayList<P>();
		Collection<P> providers = null;
		for (CourseProviderHome home : getCourseProviderHomes()) {
			providers = home.findByType(typeIds);
			if (!ListUtil.isEmpty(providers)) {
				courseProviders.addAll(providers);
			}
		}

		return courseProviders;
	}

	/* (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.business.CourseProviderBusiness#findAllSchoolsByAreaAndType(int, int)
	 */
	@Override
	public Collection<? extends CourseProvider> findAllSchoolsByAreaAndType(
			int area, int type) {
		return findAllSchoolsByAreaAndType(
				getCourseProviderAreaHome().find(String.valueOf(area)),
				getCourseProviderTypeHome().find(String.valueOf(type)));
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.business.CourseProviderBusiness#findAllSchoolsByAreaAndType(is.idega.idegaweb.egov.course.data.CourseProviderArea, is.idega.idegaweb.egov.course.data.CourseProviderType)
	 */
	@Override
	public <P extends CourseProvider> Collection<P> findAllSchoolsByAreaAndType(
			CourseProviderArea area, CourseProviderType type) {
		if (type == null || area == null) {
			return Collections.emptyList();
		}

		ArrayList<P> courseProviders = new ArrayList<P>();
		Collection<P> providers = null;
		for (CourseProviderHome home : getCourseProviderHomes()) {
			providers = home.find(Arrays.asList(area), Arrays.asList(type));
			if (!ListUtil.isEmpty(providers)) {
				courseProviders.addAll(providers);
			}
		}

		return courseProviders;
	}

	/* (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.business.CourseProviderBusiness#getSchoolArea(java.lang.Object)
	 */
	@Override
	public CourseProviderArea getSchoolArea(Object primaryKey) {
		if (primaryKey == null) {
			return null;
		}

		return getCourseProviderAreaHome().find(primaryKey.toString());
	}

	/* (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.business.CourseProviderBusiness#getSchoolType(java.lang.Object)
	 */
	@Override
	public CourseProviderType getSchoolType(Object primaryKey) {
		if (primaryKey == null) {
			return null;
		}

		return getCourseProviderTypeHome().find(primaryKey.toString());
	}

	/* (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.business.CourseProviderBusiness#findAllSchoolTypesInCategory(java.lang.String)
	 */
	@Override
	public <T extends CourseProviderType> Collection<T> findAllSchoolTypesInCategory(String categoryName) {
		if (StringUtil.isEmpty(categoryName)) {
			return Collections.emptyList();
		}

		CourseProviderCategory category = getCourseProviderCategoryHome()
				.findByPrimaryKeyInSubtypes(categoryName);
		if (category == null) {
			return Collections.emptyList();
		}

		return getCourseProviderTypeHome().findByCategories(Arrays.asList(category));
	}

	/* (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.business.CourseProviderBusiness#getAfterSchoolCareSchoolCategory()
	 */
	@Override
	public String getAfterSchoolCareSchoolCategory() {
		CourseProviderCategory category = getCategoryAfterSchoolCare();
		if (category != null) {
			return category.getCategory();
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.business.CourseProviderBusiness#getCategoryAfterSchoolCare()
	 */
	@Override
	public CourseProviderCategory getCategoryAfterSchoolCare() {
		return getCourseProviderCategoryHome().findByPrimaryKeyInSubtypes(
				CourseProviderCategory.CATEGORY_AFTER_SCHOOL_CARE);
	}

	/* (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.business.CourseProviderBusiness#getAllSchoolUsers(is.idega.idegaweb.egov.course.data.CourseProvider)
	 */
	@Override
	public Collection<? extends CourseProviderUser> getAllSchoolUsers(
			CourseProvider school) {
		return getCourseProviderUserHome().findBySchool(school);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.business.CourseProviderBusiness#getRootSchoolAdministratorGroup()
	 */
	@Override
	public Group getRootSchoolAdministratorGroup() {
		String groupId = getIWApplicationContext().getSystemProperties()
				.getProperty(PARAMETER_ROOT_SCHOOL_ADMINISTRATORS_GROUP);
		if (!StringUtil.isEmpty(groupId)) {
			try {
				return getGroupHome().findByPrimaryKey(groupId);
			} catch (FinderException e) {
				getLogger().log(Level.WARNING, 
						"Failed to get " + Group.class.getSimpleName() + 
						" by id: " + groupId);
			}
		} 

		List<Group> groups = getGroupBusiness().update(null, 
				"School Administrators", 
				"The Commune Root School Administrators Group.", null, null);
		if (ListUtil.isEmpty(groups)) {
			return null;
		}

		Group rootGroup = groups.iterator().next();
		getLogger().info("Commune Root school administrators group stored!");
		getIWApplicationContext().getSystemProperties().setProperty(
				PARAMETER_ROOT_SCHOOL_ADMINISTRATORS_GROUP, 
				rootGroup.getPrimaryKey().toString());
		return rootGroup;
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.business.CourseProviderBusiness#getRootHighSchoolAdministratorGroup()
	 */
	@Override
	public Group getRootHighSchoolAdministratorGroup() {
		String groupId = getIWApplicationContext().getSystemProperties()
				.getProperty(PARAMETER_ROOT_HIGH_SCHOOL_ADMINISTRATORS_GROUP);
		if (!StringUtil.isEmpty(groupId)) {
			try {
				return getGroupHome().findByPrimaryKey(groupId);
			} catch (FinderException e) {
				getLogger().log(Level.WARNING, 
						"Failed to get " + Group.class.getSimpleName() + 
						" by primary key: " + groupId);
			}
		}

		List<Group> rootGroups = getGroupBusiness().update(null, 
				"High School Administrators", 
				"The Commune Root High School Administrators Group.", 
				null, null);
		if (ListUtil.isEmpty(rootGroups)) {
			return null;
		}

		Group group = rootGroups.iterator().next();
		getLogger().info("Commune Root high school administrators group is stored!");
		getIWApplicationContext().getSystemProperties().setProperty(
				PARAMETER_ROOT_HIGH_SCHOOL_ADMINISTRATORS_GROUP, 
				group.getPrimaryKey().toString());
		return group;
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.business.CourseProviderBusiness#getRootAdultEducationAdministratorGroup()
	 */
	@Override
	public Group getRootAdultEducationAdministratorGroup() {
		String groupId = getIWApplicationContext().getSystemProperties()
				.getProperty(PARAMETER_ROOT_ADULT_EDUCATION_ADMINISTRATORS_GROUP);
		if (groupId != null) {
			try {
				return getGroupHome().findByPrimaryKey(groupId);
			} catch (FinderException e) {
				getLogger().log(Level.WARNING, 
						"Failed to get " + Group.class.getSimpleName() + 
						" by id: " + groupId);
			}
		}

		List<Group> groups = getGroupBusiness().update(null, 
				"Adult Education Administrators", 
				"The Commune Root Adult Educaiton Administrators Group.", 
				null, null);
		if (ListUtil.isEmpty(groups)) {
			return null;			
		}

		Group group = groups.iterator().next();
		getLogger().info("Commune Root Adult Education administrators group");
		getIWApplicationContext().getSystemProperties().setProperty(
				PARAMETER_ROOT_ADULT_EDUCATION_ADMINISTRATORS_GROUP, 
				group.getPrimaryKey().toString());
		return group;
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.business.CourseProviderBusiness#getRootProviderAdministratorGroup()
	 */
	@Override
	public Group getRootProviderAdministratorGroup() {
		String groupId = getIWApplicationContext().getSystemProperties()
				.getProperty(ROOT_SCHOOL_ADMINISTRATORS_GROUP);
		if (!StringUtil.isEmpty(groupId)) {
			try {
				return getGroupHome().findByPrimaryKey(groupId);
			} catch (FinderException e) {
				getLogger().log(Level.WARNING, 
						"Failed to get " + GroupHome.class.getSimpleName() + 
						" cause of: ", e);
			}
		}

		List<Group> rootGroups = getGroupBusiness().update(null, 
				"Provider Administrators", 
				"The Commune Root Provider Administrators Group.", 
				null, null);
		if (ListUtil.isEmpty(rootGroups)) {
			return null;
		}

		Group rootGroup = rootGroups.iterator().next();
		getLogger().info("Commune Root school administrators group stored!");
		getIWApplicationContext().getSystemProperties().setProperty(
				ROOT_SCHOOL_ADMINISTRATORS_GROUP, 
				rootGroup.getPrimaryKey().toString());

		return rootGroup;
	}

	private HashSet<CourseProviderHome> courseProviderHomes = null;

	private CourseProviderUserHome courseProviderUserHome = null;

	private CourseProviderCategoryHome courseProviderCategoryHome = null;

	private CourseProviderTypeHome courseProviderTypeHome = null;

	private CourseProviderAreaHome courseProviderAreaHome = null;

	private GroupHome groupHome = null;

	private GroupBusiness groupBusiness = null;

	private CourseProviderHome courseProviderHome = null;

	protected CourseProviderHome getCourseProviderHome() {
		if (this.courseProviderHome == null) {
			try {
				this.courseProviderHome = (CourseProviderHome) IDOLookup
						.getHome(CourseProvider.class);
			} catch (IDOLookupException e) {
				getLogger().log(Level.WARNING, 
						"Failed to get " + CourseProviderHome.class.getSimpleName() + 
						" cause of: ", e);
			}
		}

		return this.courseProviderHome;
	}

	protected GroupBusiness getGroupBusiness() {
		if (this.groupBusiness == null) {
			try {
				this.groupBusiness = IBOLookup.getServiceInstance(
						CoreUtil.getIWContext(), GroupBusiness.class);
			} catch (IBOLookupException e) {
				getLogger().log(Level.WARNING, 
						"Failed to get " + GroupBusiness.class.getSimpleName() + 
						" cause of: ", e);
			}
		}

		return this.groupBusiness;
	}
	
	protected GroupHome getGroupHome() {
		if (this.groupHome == null) {
			try {
				this.groupHome = (GroupHome) IDOLookup.getHome(Group.class);
			} catch (IDOLookupException e) {
				getLogger().log(Level.WARNING, 
						"Failed to get " + GroupHome.class.getSimpleName() + 
						" cause of: ", e);
			}
		}

		return this.groupHome;
	}

	protected CourseProviderAreaHome getCourseProviderAreaHome() {
		if (this.courseProviderAreaHome == null) {
			try {
				this.courseProviderAreaHome = (CourseProviderAreaHome) IDOLookup.getHome(CourseProviderArea.class);
			} catch (IDOLookupException e) {
				getLogger().log(Level.WARNING,
						"Failed to get " + CourseProviderAreaHome.class.getName() +
						" cause of: ", e);
			}
		}

		return this.courseProviderAreaHome;
	}

	protected CourseProviderTypeHome getCourseProviderTypeHome() {
		if (this.courseProviderTypeHome == null) {
			try {
				this.courseProviderTypeHome = (CourseProviderTypeHome) IDOLookup.getHome(CourseProviderType.class);
			} catch (IDOLookupException e) {
				getLogger().log(Level.WARNING,
						"Failed to get " + CourseProviderTypeHome.class.getName() +
						" cause of: ", e);
			}
		}

		return this.courseProviderTypeHome;
	}

	protected CourseProviderCategoryHome getCourseProviderCategoryHome() {
		if (this.courseProviderCategoryHome == null) {
			try {
				this.courseProviderCategoryHome = (CourseProviderCategoryHome) IDOLookup.getHome(CourseProviderCategory.class);
			} catch (IDOLookupException e) {
				getLogger().log(
						Level.WARNING,
						"Failed to get " + CourseProviderCategoryHome.class.getName() +
						" cause of: ", e);
			}
		}

		return this.courseProviderCategoryHome;
	}

	protected CourseProviderUserHome getCourseProviderUserHome() {
		if (this.courseProviderUserHome == null) {
			
			try {
				this.courseProviderUserHome = (CourseProviderUserHome) IDOLookup.getHome(CourseProviderUser.class);
			} catch (IDOLookupException e) {
				getLogger().log(
						Level.WARNING,
						"Failed to get " + CourseProviderUserHome.class.getName() +
						" cause of: ", e);
			}
		}

		return this.courseProviderUserHome;
	}

	protected HashSet<CourseProviderHome> getCourseProviderHomes() {
		if (ListUtil.isEmpty(this.courseProviderHomes)) {
			this.courseProviderHomes = new HashSet<CourseProviderHome>();
		}

		Set<Class<? extends CourseProvider>> subtypes = CoreUtil.getSubTypesOf(CourseProvider.class, true);
		if (ListUtil.isEmpty(subtypes)) {
			return this.courseProviderHomes;
		}

		CourseProviderHome courseProviderHome = null;
		for (Class<? extends CourseProvider> subtype : subtypes) {
			try {
				courseProviderHome = (CourseProviderHome) IDOLookup.getHome(subtype);
			} catch (IDOLookupException e) {
				getLogger().log(Level.WARNING,
						"Failed to get " + subtype.getSimpleName() + 
						" cause of: ", e);
			}

			if (courseProviderHome != null) {
				this.courseProviderHomes.add(courseProviderHome);
			}
		}

		return this.courseProviderHomes;
	}
}
