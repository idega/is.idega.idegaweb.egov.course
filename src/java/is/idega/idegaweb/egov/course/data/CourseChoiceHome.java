package is.idega.idegaweb.egov.course.data;


import java.sql.Date;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.IDOException;
import com.idega.data.IDOHome;
import com.idega.user.data.Gender;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

public interface CourseChoiceHome extends IDOHome {
	public CourseChoice create() throws CreateException;

	public CourseChoice findByPrimaryKey(Object pk) throws FinderException;

	public CourseChoice findByUniqueID(String uniqueID) throws FinderException;

	public Collection<CourseChoice> findAllByApplication(CourseApplication application)
			throws FinderException;

	public Collection<CourseChoice> findAllByApplication(CourseApplication application,
			Boolean waitingList) throws FinderException;

	public CourseChoice findFirstChoiceByApplication(
			CourseApplication application) throws FinderException;

	public Collection<CourseChoice> findAllByCourse(Course course, Boolean waitingList)
			throws FinderException;

	public int getCountByCourse(Course course) throws IDOException;
	public int getCountByCourse(Course course, boolean countOffers) throws IDOException;

	public Collection<CourseChoice> findAllByUser(User user) throws FinderException;

	public Collection<CourseChoice> findAllByUserAndProviders(User user, Collection<CourseProvider> providers)
			throws FinderException;

	public CourseChoice findByUserAndCourse(User user, Course course)
			throws FinderException;

	public Collection<CourseChoice> findAllByCourseAndDate(Course course,
			IWTimestamp fromDate, IWTimestamp toDate) throws FinderException;

	public int getCountByUserAndProviders(User user, Collection<?> providers)
			throws IDOException;

	public int getCountByProviderAndSchoolTypeAndGender(CourseProvider provider,
			CourseProviderType type, Gender gender, Date fromDate, Date toDate)
			throws IDOException;

	public int getCountByCourseAndGender(Course course, Gender gender)
			throws IDOException;

	public int getCountByUserAndCourse(User user, Course course)
			throws IDOException;
}