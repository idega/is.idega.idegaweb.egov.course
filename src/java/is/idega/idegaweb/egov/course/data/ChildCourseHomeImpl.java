package is.idega.idegaweb.egov.course.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDOStoreException;
import com.idega.user.data.Group;
import com.idega.util.ListUtil;

public class ChildCourseHomeImpl extends CourseHomeImpl implements ChildCourseHome {

	private static final long serialVersionUID = -2624860953592962255L;

	@Override
	public Class getEntityInterfaceClass() {
		return ChildCourse.class;
	}

	@Override
	public ChildCourse create() throws CreateException {
		return (ChildCourse) super.createIDO();
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.ChildCourseHome#findChildCourses(is.idega.idegaweb.egov.course.data.Course, java.lang.String)
	 */
	@Override
	public Collection<ChildCourse> findChildCourses(
			Course parentCourse,
			String courseProviderId) {
		if (parentCourse == null) {
			return Collections.emptyList();
		}

		ChildCourseBMPBean entity = (ChildCourseBMPBean) idoCheckOutPooledEntity();
		if (entity == null) {
			return Collections.emptyList();
		}

		Collection<String> ids = entity.ejbFindChildCourses(
				parentCourse, courseProviderId);
		if (ListUtil.isEmpty(ids)) {
			return Collections.emptyList();
		}

		try {
			return getEntityCollectionForPrimaryKeys(ids);
		} catch (FinderException e) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING, 
					"Failed to get " + getEntityInterfaceClass().getSimpleName() + 
					"'s by primary keys: '" + ids + "' cause of:", e);
		}

		return Collections.emptyList();
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.ChildCourseHome#findChildCourses(is.idega.idegaweb.egov.course.data.Course)
	 */
	@Override
	public Collection<ChildCourse> findChildCourses(Course parentCourse) {
		return findChildCourses(parentCourse, null);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.ChildCourseHome#update(is.idega.idegaweb.egov.course.data.Course, java.lang.String, java.lang.String, java.util.Collection, java.lang.String, java.util.Date, java.util.Date, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.util.Date, java.util.ArrayList)
	 */
	@Override
	public Collection<ChildCourse> update(
			Course parentCourse, 
			String name,
			String courseHandlerPersonalId, 
			Collection<? extends CourseProvider> centers,
			String courseTypeId, 
			java.util.Date startDate,
			java.util.Date endDate, 
			String accountingKey, 
			Integer birthYearFrom,
			Integer birthYearTo,
			Integer maximumParticipantsNumber, 
			java.util.Date registrationEndDate, 
			ArrayList<Group> accessGroups) {
		
		/*
		 * Updating parent course first
		 */
		parentCourse = getCourseHome().update(parentCourse, name, 
				courseHandlerPersonalId, null,
				courseTypeId, startDate, endDate, accountingKey, birthYearFrom,
				birthYearTo, maximumParticipantsNumber, registrationEndDate, 
				accessGroups);

		/*
		 * Updating child courses
		 */
		Collection<ChildCourse> updatedChildCourses = new ArrayList<ChildCourse>();
		if (parentCourse != null && !ListUtil.isEmpty(centers)) {
			Collection<ChildCourse> foundChildCourses = findChildCourses(parentCourse);
			for (ChildCourse childCourse: foundChildCourses) {
				CourseProvider childCourseProvider = childCourse.getProvider();
				if (centers.contains(childCourseProvider)) {
					
					/*
					 * Updating existing child course
					 */
					ChildCourse updatedChildCourse = update(
							childCourse, parentCourse, name, courseHandlerPersonalId, 
							null, courseTypeId, startDate, endDate, 
							accountingKey, birthYearFrom, birthYearTo, 
							maximumParticipantsNumber, registrationEndDate, 
							accessGroups);
					if (updatedChildCourse != null) {
						updatedChildCourses.add(updatedChildCourse);

						/*
						 * Removing used course provider
						 */
						centers.remove(childCourseProvider);
					}
				} else {

					/*
					 * Removing child course, which no longer used
					 */
					remove(childCourse);
				}
			}

			/*
			 * Creating new child course for course providers
			 */
			for (CourseProvider provider : centers) {
				ChildCourse newChildCourse = update(null, parentCourse, name, 
						courseHandlerPersonalId, provider, courseTypeId, 
						startDate, endDate, accountingKey, birthYearFrom, 
						birthYearTo, maximumParticipantsNumber, 
						registrationEndDate, accessGroups);
				if (newChildCourse != null) {
					updatedChildCourses.add(newChildCourse);
				}
			}
		}			

		return updatedChildCourses;
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.ChildCourseHome#update(is.idega.idegaweb.egov.course.data.ChildCourse, is.idega.idegaweb.egov.course.data.Course, java.lang.String, java.lang.String, is.idega.idegaweb.egov.course.data.CourseProvider, java.lang.String, java.util.Date, java.util.Date, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.util.Date, java.util.ArrayList)
	 */
	@Override
	public ChildCourse update(
			ChildCourse childCourse,
			Course parentCourse, 
			String name,
			String courseHandlerPersonalId, 
			CourseProvider center,
			String courseTypeId, 
			Date startDate, 
			Date endDate,
			String accountingKey, 
			Integer birthYearFrom, 
			Integer birthYearTo,
			Integer maximumParticipantsNumber, 
			Date registrationEndDate,
			ArrayList<Group> accessGroups) {

		Course course = super.update(childCourse, name, courseHandlerPersonalId, center,
				courseTypeId, startDate, endDate, accountingKey, birthYearFrom,
				birthYearTo, maximumParticipantsNumber, registrationEndDate,
				accessGroups);
		if (course instanceof ChildCourse) {
			childCourse = (ChildCourse) course;
			if (parentCourse != null) {
				childCourse.setParentCourse(parentCourse);
				
				try {
					childCourse.store();
					Logger.getLogger(getClass().getName()).log(Level.WARNING,
							Course.class.getSimpleName() + 
							" by id: '" + parentCourse.getPrimaryKey() + 
							"' is added to " + ChildCourse.class.getSimpleName() + 
							" by primary key: '" + childCourse.getPrimaryKey() + "'");
				} catch (IDOStoreException e) {
					Logger.getLogger(getClass().getName()).log(Level.WARNING,
							"Failed to add " + Course.class.getSimpleName() + 
							" by id: '" + parentCourse.getPrimaryKey() + 
							"' to " + ChildCourse.class.getSimpleName() + 
							" by primary key: '" + childCourse.getPrimaryKey() + "' " +
							"cause of: ", e);
					return null;
				}
			}
		}

		return childCourse;
	}

	private CourseHome courseHome = null;

	protected CourseHome getCourseHome() {
		if (this.courseHome == null) {
			try {
				this.courseHome = (CourseHome) IDOLookup.getHome(Course.class);
			} catch (IDOLookupException e) {
				Logger.getLogger(getClass().getName()).log(Level.WARNING, 
						"Failed to get " + CourseHome.class.getSimpleName() + 
						" cause of: ", e);
			}
		}

		return this.courseHome;
	}
}