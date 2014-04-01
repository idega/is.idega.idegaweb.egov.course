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

	/**
	 * 
	 * @param user to search by, not <code>null</code>;
	 * @param providers to search by, skipped if <code>null</code>;
	 * @return number of {@link CourseChoice}s found by criteria
	 * or -1 on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public int getCountByUserAndProviders(User user, 
			Collection<CourseProvider> providers);

	public int getCountByProviderAndSchoolTypeAndGender(CourseProvider provider,
			CourseProviderType type, Gender gender, Date fromDate, Date toDate)
			throws IDOException;

	/**
	 * 
	 * @param course to search by, not <code>null</code>;
	 * @param gender to search by, not <code>null</code>;
	 * @return number of {@link CourseChoice}s by criteria or 0 on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	int getCountByCourseAndGender(Course course, Gender gender);

	/**
	 * 
	 * @param course to search by, not <code>null</code>;
	 * @param gender to search by, not <code>null</code>;
	 * @return number of {@link CourseChoice}s by criteria or 0 on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	int getCountByCourseAndGender(Collection<Course> course, Gender gender);

	public int getCountByUserAndCourse(User user, Course course)
			throws IDOException;
}