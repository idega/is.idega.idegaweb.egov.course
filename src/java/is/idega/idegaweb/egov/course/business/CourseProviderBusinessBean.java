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

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;

import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
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

	/* (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.business.CourseProviderBusiness#getSchool(java.lang.Object)
	 */
	@Override
	public CourseProvider getSchool(Object primaryKey) {
		if (primaryKey == null) {
			return null;
		}

		return getCourseProviderHome().find(primaryKey.toString());
	}

	/* (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.business.CourseProviderBusiness#findAllSchoolsByType(java.util.Collection)
	 */
	@Override
	public Collection<? extends CourseProvider> findAllSchoolsByType(
			Collection<? extends CourseProviderType> typeIds) {
		if (ListUtil.isEmpty(typeIds)) {
			return Collections.emptyList();
		}

		return getCourseProviderHome().findByType(typeIds);
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

		return getCourseProviderHome().find(
				Arrays.asList(area),
				Arrays.asList(type));
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

		CourseProviderCategory category = getCourseProviderCategoryHome().findByCategory(categoryName);
		if (category == null) {
			return Collections.emptyList();
		}

		return getCourseProviderTypeHome().findByCategories(Arrays.asList(category));
	}

	/* (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.business.CourseProviderBusiness#getAfterSchoolCareSchoolCategory()
	 */
	@Override
	public String getAfterSchoolCareSchoolCategory() throws RemoteException {
//		throw new UnsupportedOperationException();
//		FIXME this is not supported for this
		return null;
	}

	/* (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.business.CourseProviderBusiness#getCategoryAfterSchoolCare()
	 */
	@Override
	public CourseProviderCategory getCategoryAfterSchoolCare()
			throws RemoteException {
//		throw new UnsupportedOperationException();
//		FIXME this is not supported for this
		return null;
	}

	/* (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.business.CourseProviderBusiness#getAllSchoolUsers(is.idega.idegaweb.egov.course.data.CourseProvider)
	 */
	@Override
	public Collection<? extends CourseProviderUser> getAllSchoolUsers(
			CourseProvider school) {
		return getCourseProviderUserHome().findBySchool(school);
	}

	private CourseProviderHome courseProviderHome = null;

	private CourseProviderUserHome courseProviderUserHome = null;

	private CourseProviderCategoryHome courseProviderCategoryHome = null;

	private CourseProviderTypeHome courseProviderTypeHome = null;

	private CourseProviderAreaHome courseProviderAreaHome = null;

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

	protected CourseProviderHome getCourseProviderHome() {
		if (this.courseProviderHome == null) {
			try {
				this.courseProviderHome = (CourseProviderHome) IDOLookup.getHome(CourseProvider.class);
			} catch (IDOLookupException e) {
				getLogger().log(Level.WARNING,
						"Failed to get " + CourseProviderHome.class.getName() +
						" cause of: ", e);
			}
		}

		return this.courseProviderHome;
	}
}
