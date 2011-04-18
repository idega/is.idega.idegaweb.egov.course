package is.idega.idegaweb.egov.course.data;


import com.idega.data.IDOException;
import com.idega.block.school.data.School;
import javax.ejb.CreateException;
import com.idega.user.data.User;
import java.sql.Date;
import com.idega.user.data.Gender;
import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.util.IWTimestamp;
import com.idega.block.school.data.SchoolType;
import com.idega.data.IDOFactory;
import com.idega.data.IDOEntity;

public class CourseChoiceHomeImpl extends IDOFactory implements
		CourseChoiceHome {
	public Class getEntityInterfaceClass() {
		return CourseChoice.class;
	}

	public CourseChoice create() throws CreateException {
		return (CourseChoice) super.createIDO();
	}

	public CourseChoice findByPrimaryKey(Object pk) throws FinderException {
		return (CourseChoice) super.findByPrimaryKeyIDO(pk);
	}

	public CourseChoice findByUniqueID(String uniqueID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((CourseChoiceBMPBean) entity).ejbFindByUniqueID(uniqueID);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findAllByApplication(CourseApplication application)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CourseChoiceBMPBean) entity)
				.ejbFindAllByApplication(application);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByApplication(CourseApplication application,
			Boolean waitingList) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CourseChoiceBMPBean) entity)
				.ejbFindAllByApplication(application, waitingList);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public CourseChoice findFirstChoiceByApplication(
			CourseApplication application) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((CourseChoiceBMPBean) entity)
				.ejbFindFirstChoiceByApplication(application);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findAllByCourse(Course course, Boolean waitingList)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CourseChoiceBMPBean) entity).ejbFindAllByCourse(
				course, waitingList);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public int getCountByCourse(Course course) throws IDOException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((CourseChoiceBMPBean) entity)
				.ejbHomeGetCountByCourse(course);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public Collection findAllByUser(User user) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CourseChoiceBMPBean) entity).ejbFindAllByUser(user);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByUserAndProviders(User user, Collection providers)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CourseChoiceBMPBean) entity)
				.ejbFindAllByUserAndProviders(user, providers);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public CourseChoice findByUserAndCourse(User user, Course course)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((CourseChoiceBMPBean) entity).ejbFindByUserAndCourse(user,
				course);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findAllByCourseAndDate(Course course,
			IWTimestamp fromDate, IWTimestamp toDate) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CourseChoiceBMPBean) entity)
				.ejbFindAllByCourseAndDate(course, fromDate, toDate);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public int getCountByUserAndProviders(User user, Collection providers)
			throws IDOException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((CourseChoiceBMPBean) entity)
				.ejbHomeGetCountByUserAndProviders(user, providers);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getCountByProviderAndSchoolTypeAndGender(School provider,
			SchoolType type, Gender gender, Date fromDate, Date toDate)
			throws IDOException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((CourseChoiceBMPBean) entity)
				.ejbHomeGetCountByProviderAndSchoolTypeAndGender(provider,
						type, gender, fromDate, toDate);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getCountByCourseAndGender(Course course, Gender gender)
			throws IDOException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((CourseChoiceBMPBean) entity)
				.ejbHomeGetCountByCourseAndGender(course, gender);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getCountByUserAndCourse(User user, Course course)
			throws IDOException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((CourseChoiceBMPBean) entity)
				.ejbHomeGetCountByUserAndCourse(user, course);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}
}