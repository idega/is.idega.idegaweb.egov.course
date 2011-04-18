package is.idega.idegaweb.egov.course.data;


import com.idega.data.IDOException;
import com.idega.block.school.data.School;
import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import com.idega.block.school.data.SchoolType;
import com.idega.util.IWTimestamp;
import javax.ejb.FinderException;
import java.sql.Date;
import com.idega.user.data.User;
import com.idega.user.data.Gender;

public interface CourseChoiceHome extends IDOHome {
	public CourseChoice create() throws CreateException;

	public CourseChoice findByPrimaryKey(Object pk) throws FinderException;

	public CourseChoice findByUniqueID(String uniqueID) throws FinderException;

	public Collection findAllByApplication(CourseApplication application)
			throws FinderException;

	public Collection findAllByApplication(CourseApplication application,
			Boolean waitingList) throws FinderException;

	public CourseChoice findFirstChoiceByApplication(
			CourseApplication application) throws FinderException;

	public Collection findAllByCourse(Course course, Boolean waitingList)
			throws FinderException;

	public int getCountByCourse(Course course) throws IDOException;

	public Collection findAllByUser(User user) throws FinderException;

	public Collection findAllByUserAndProviders(User user, Collection providers)
			throws FinderException;

	public CourseChoice findByUserAndCourse(User user, Course course)
			throws FinderException;

	public Collection findAllByCourseAndDate(Course course,
			IWTimestamp fromDate, IWTimestamp toDate) throws FinderException;

	public int getCountByUserAndProviders(User user, Collection providers)
			throws IDOException;

	public int getCountByProviderAndSchoolTypeAndGender(School provider,
			SchoolType type, Gender gender, Date fromDate, Date toDate)
			throws IDOException;

	public int getCountByCourseAndGender(Course course, Gender gender)
			throws IDOException;

	public int getCountByUserAndCourse(User user, Course course)
			throws IDOException;
}