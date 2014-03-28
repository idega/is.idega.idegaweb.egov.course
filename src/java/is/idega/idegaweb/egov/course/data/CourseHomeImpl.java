package is.idega.idegaweb.egov.course.data;


import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.IDOEntity;
import com.idega.data.IDOException;
import com.idega.data.IDOFactory;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDOStoreException;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;
import com.idega.util.ListUtil;
import com.idega.util.StringUtil;

public class CourseHomeImpl extends IDOFactory implements CourseHome {

	private static final long serialVersionUID = -4752370157281953886L;

	@Override
	public Class<Course> getEntityInterfaceClass() {
		return Course.class;
	}

	@Override
	public Course create() throws CreateException {
		return (Course) super.createIDO();
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseHome#findByPrimaryKey(java.lang.Object)
	 */
	@Override
	public Course findByPrimaryKey(Object pk) {
		if (pk == null || pk.toString().isEmpty()) {
			return null;
		}

		try {
			return (Course) super.findByPrimaryKeyIDO(pk);
		} catch (FinderException e) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(
					Level.WARNING,
					"Failed to get " + getEntityInterfaceClass().getSimpleName() +
					" by primary key: '" + pk + "'");
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseHome#findAll()
	 */
	@Override
	public Collection<Course> findAll() {
		return findAll(null, null, null, null, null, null, null, null, null);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseHome#findAllByProvider(is.idega.idegaweb.egov.course.data.CourseProvider)
	 */
	@Override
	public Collection<Course> findAllByProvider(CourseProvider provider) {
		if (provider != null) {
			return findAll(Arrays.asList(provider), null, null, null, null,
					null, null, null, null);
		}

		return Collections.emptyList();
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseHome#findAllByBirthYear(int)
	 */
	@Override
	public Collection<Course> findAllByBirthYear(int birthYear) {
		return findAll(null, null, null, birthYear, null, null);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseHome#findAllPrimaryKeys(java.lang.String, java.lang.String, java.lang.String, int, java.sql.Date, java.sql.Date)
	 */
	@Override
	public Collection<Object> findAllPrimaryKeys(String providerPK,
			String schoolTypePK, String courseTypePK, int birthYear,
			Date fromDate, Date toDate) {
		CourseBMPBean entity = (CourseBMPBean) this.idoCheckOutPooledEntity();
		if (entity == null) {
			return Collections.emptyList();
		}

		CourseProvider provider = getCourseProviderHome().findByPrimaryKey(providerPK);
		CourseProviderType providerType = getCourseProviderTypeHome().find(schoolTypePK);
		CourseType courseType = getCourseTypeHome().findByPrimaryKey(courseTypePK);

		Date birthDate = null;
		if (birthYear > 0) {
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, birthYear);
			birthDate = new Date(calendar.getTimeInMillis());
		}

		// TODO needs parameter to exclude selection from child courses
		return entity.ejbFindAll(
				provider != null ? Arrays.asList(provider) : null,
				providerType != null ? Arrays.asList(providerType) : null,
				courseType != null ? Arrays.asList(courseType) : null,
				birthDate, birthDate,
				fromDate, toDate,
				null, null);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseHome#findAll(java.lang.Object, java.lang.Object, java.lang.Object, int, java.sql.Date, java.sql.Date)
	 */
	@Override
	public Collection<Course> findAll(
			String providerPK,
			String schoolTypePK,
			String courseTypePK,
			int birthYear,
			Date fromDate,
			Date toDate) {
		Collection<Object> ids = findAllPrimaryKeys(providerPK, schoolTypePK,
				courseTypePK, birthYear, fromDate, toDate);
		if (ListUtil.isEmpty(ids)) {
			return Collections.emptyList();
		}

		try {
			return this.getEntityCollectionForPrimaryKeys(ids);
		} catch (FinderException e) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING,
					"Failed to get " + Course.class.getSimpleName() +
					" by ids: '" + ids + "'");
		}

		return Collections.emptyList();
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseHome#findAll(java.lang.String, java.lang.String, java.lang.String, int)
	 */
	@Override
	public Collection<Course> findAll(String providerPK, String schoolTypePK, String courseTypePK, int birthYear) {
		return findAll(providerPK, schoolTypePK, courseTypePK, birthYear, null, null);
	}

	@Override
	public Collection<Course> findAll(Collection providers, Object schoolTypePK, Object courseTypePK) throws FinderException, IDORelationshipException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection <?>ids = ((CourseBMPBean) entity).ejbFindAll(providers, schoolTypePK, courseTypePK);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	@Override
	public Collection<Course> findAll(
			Collection<? extends CourseProvider> courseProviders,
			Collection<? extends CourseProviderType> couserProviderTypes,
			Collection<? extends CourseType> courseTypes,
			Date birthDateFrom,
			Date birthDateTo,
			Date fromDate,
			Date toDate,
			Boolean isPrivate,
			Collection<Group> groupsWithAccess) {
		return findAll(
				courseProviders, 
				couserProviderTypes, 
				courseTypes, 
				birthDateFrom, 
				birthDateTo, 
				fromDate, 
				toDate, 
				isPrivate, 
				groupsWithAccess, 
				Boolean.FALSE);
//		FIXME Do or do not exclude child courses?
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseHome#findAll(java.util.Collection, java.util.Collection, java.util.Collection, java.sql.Date, java.sql.Date, java.sql.Date, java.sql.Date, java.lang.Boolean, java.util.Collection, boolean)
	 */
	@Override
	public Collection<Course> findAll(
			Collection<? extends CourseProvider> courseProviders,
			Collection<? extends CourseProviderType> couserProviderTypes,
			Collection<? extends CourseType> courseTypes,
			Date birthDateFrom,
			Date birthDateTo,
			Date fromDate,
			Date toDate,
			Boolean isPrivate,
			Collection<Group> groupsWithAccess,
			boolean notChild) {
		CourseBMPBean entity = (CourseBMPBean) idoCheckOutPooledEntity();
		Collection<Object> ids = entity.ejbFindAll(
				courseProviders,
				couserProviderTypes,
				courseTypes,
				birthDateFrom,
				birthDateTo,
				fromDate,
				toDate,
				isPrivate,
				groupsWithAccess);
		if (ListUtil.isEmpty(ids)) {
			return Collections.emptyList();
		}

		try {
			return getEntityCollectionForPrimaryKeys(ids);
		} catch (FinderException e) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(
					Level.WARNING,
					"Failed to get " + getEntityInterfaceClass().getSimpleName() +
					"'s by id's: '" + ids + "' cause of: ", e);
		}

		return Collections.emptyList();
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseHome#findAllByProviderAndSchoolTypeAndCourseType(is.idega.idegaweb.egov.course.data.CourseProvider, is.idega.idegaweb.egov.course.data.CourseProviderType, is.idega.idegaweb.egov.course.data.CourseType, java.sql.Date, java.sql.Date)
	 */
	@Override
	public Collection<Course> findAllByProviderAndSchoolTypeAndCourseType(
			CourseProvider provider, CourseProviderType type,
			CourseType courseType, Date fromDate, Date toDate) {
		return findAll(
				provider != null ? Arrays.asList(provider) : null,
				type != null ? Arrays.asList(type) : null,
				courseType != null ? Arrays.asList(courseType) : null,
				null,
				null,
				fromDate,
				toDate,
				null,
				null);
	}

	@Override
	public int getCountBySchoolTypeAndBirthYear(Object schoolTypePK, int birthYear, Date fromDate) throws IDOException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((CourseBMPBean) entity).ejbHomeGetCountBySchoolTypeAndBirthYear(schoolTypePK, birthYear, fromDate);
		return theReturn;
	}

	@Override
	public int getCountByCourseTypeAndBirthYear(Object courseTypePK, int birthYear, Date fromDate) throws IDOException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((CourseBMPBean) entity).ejbHomeGetCountByCourseTypeAndBirthYear(courseTypePK, birthYear, fromDate);
		return theReturn;
	}

	@Override
	public int getCountBySchoolAndCourseTypeAndBirthYear(Object schoolPK, Object courseTypePK, int birthYear, Date fromDate) throws IDOException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((CourseBMPBean) entity).ejbHomeGetCountBySchoolAndCourseTypeAndBirthYear(schoolPK, courseTypePK, birthYear, fromDate);
		return theReturn;
	}

	@Override
	public int getCountBySchoolAndBirthYear(Object schoolPK, int birthYear, Date fromDate) throws IDOException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((CourseBMPBean) entity).ejbHomeGetCountBySchoolAndBirthYear(schoolPK, birthYear, fromDate);
		return theReturn;
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseHome#getCountByProviderAndSchoolTypeAndCourseType(is.idega.idegaweb.egov.course.data.CourseProvider, is.idega.idegaweb.egov.course.data.CourseProviderType, is.idega.idegaweb.egov.course.data.CourseType, java.sql.Date, java.sql.Date)
	 */
	@Override
	public int getCountByProviderAndSchoolTypeAndCourseType(
			CourseProvider provider,
			CourseProviderType type,
			CourseType courseType,
			Date fromDate,
			Date toDate) {
		Collection<Course> courses = findAllByProviderAndSchoolTypeAndCourseType(
				provider, type, courseType, fromDate, toDate);
		if (ListUtil.isEmpty(courses)) {
			return 0;
		}

		return courses.size();
	}

	@Override
	public int getHighestCourseNumber() throws IDOException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((CourseBMPBean) entity).ejbHomeGetHighestCourseNumber();
		return theReturn;
	}

	@Override
	public Collection<Course> findAllWithNoCourseNumber() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection<?> ids = ((CourseBMPBean) entity).ejbFindAllWithNoCourseNumber();
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	@Override
	public Collection<Course> findAllByTypes(Collection<String> typesIds) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection<?> ids = ((CourseBMPBean) entity).ejbFindAllByTypes(typesIds);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	@Override
	public Collection<Course> findAllByUser(String user) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection<?> ids = ((CourseBMPBean) entity).ejbFindAllByUser(user);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseHome#remove(java.lang.String)
	 */
	@Override
	public void remove(String primaryKey) {
		remove(findByPrimaryKey(primaryKey));
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseHome#remove(is.idega.idegaweb.egov.course.data.Course)
	 */
	@Override
	public void remove(Course course) {
		if (course != null) {
			try {
				course.removeGroupsWithAccess();
				course.remove();
				Logger.getLogger(getClass().getName()).log(Level.INFO,
						"Removed " + getEntityInterfaceClass().getSimpleName() +
						" by id: '" + course.getPrimaryKey() + "'");
			} catch (Exception e) {
				Logger.getLogger(getClass().getName()).log(Level.WARNING,
						"Failed to " + getEntityInterfaceClass().getSimpleName() +
						" by id: '" + course.getPrimaryKey() +
						"' cause of: ", e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseHome#update(is.idega.idegaweb.egov.course.data.Course, int, java.lang.String, com.idega.user.data.User, is.idega.idegaweb.egov.course.data.CourseType, is.idega.idegaweb.egov.course.data.CourseProvider, is.idega.idegaweb.egov.course.data.CoursePrice, java.util.Date, java.util.Date, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Float, java.lang.Float, java.lang.Boolean, java.util.Date, java.lang.Boolean, java.lang.Boolean, java.lang.Boolean, java.util.Collection)
	 */
	@Override
	public Course update(
			Course course,
			int courseNumber,
			String name,
			User handler,
			CourseType courseType,
			CourseProvider provider,
			CoursePrice coursePrice,
			java.util.Date startDate,
			java.util.Date endDate,
			String accountingKey,
			Integer birthYearFrom,
			Integer birthYearTo,
			Integer maxPer,
			Float price,
			Float cost,
			Boolean openForRegistration,
			java.util.Date registrationEnd,
			Boolean hasPreCare,
			Boolean hasPostCare,
			Boolean isPrivate,
			Collection<Group> groupsWithAccess) {

		if (course == null) {
			try {
				course = create();
			} catch (CreateException e) {
				java.util.logging.Logger.getLogger(getClass().getName()).log(
						Level.WARNING,
						"Failed to create new entity for " +
						getEntityInterfaceClass().getSimpleName() +
						" cause of: ", e);
			}
		}

		if (courseNumber > 0) {
			course.setCourseNumber(courseNumber);
		} else {
			course.setCourseNumber(getNextCourseNumber());
		}

		if (!StringUtil.isEmpty(name)) {
			course.setName(name);
		}

		if (courseType != null) {
			course.setCourseType(courseType);
		}

		if (provider != null) {
			course.setProvider(provider);
		}

		if (handler != null) {
			course.setUser(handler.getPersonalID());
		}

		if (coursePrice != null) {
			course.setPrice(coursePrice);
		} else {
			if (price != null && price >= 0) {
				course.setCoursePrice(price);
			}

			if (cost != null && cost >= 0) {
				course.setCourseCost(cost);
			}
		}

		if (startDate != null) {
			course.setStartDate(new Timestamp(startDate.getTime()));
		}

		if (endDate != null) {
			course.setEndDate(new Timestamp(endDate.getTime()));
		}

		if (registrationEnd != null) {
			course.setRegistrationEnd(new Timestamp(registrationEnd.getTime()));
		}

		if (accountingKey != null) {
			course.setAccountingKey(accountingKey);
		}

		if (birthYearFrom != null && birthYearFrom > 1800) {
			course.setBirthyearFrom(birthYearFrom);
		}

		if (birthYearTo != null && birthYearTo > 1800) {
			course.setBirthyearTo(birthYearTo);
		}

		if (maxPer != null && maxPer >= 0) {
			course.setMax(maxPer);
		}

		if (openForRegistration != null) {
			course.setOpenForRegistration(openForRegistration);
		}

		if (hasPostCare != null) {
			course.setHasPostCare(hasPostCare);
		}

		if (hasPreCare != null) {
			course.setHasPreCare(hasPreCare);
		}

		if (isPrivate != null) {
			course.setPrivate(isPrivate);
		}

		try {
			course.store();
			if (!ListUtil.isEmpty(groupsWithAccess)) {
				course.setGroupsWithAccess(groupsWithAccess);
			}

			if (isPrivate != null && !isPrivate) {
				course.removeGroupsWithAccess();
			}
		} catch (IDOStoreException e) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(
					Level.WARNING,
					"Failed to store " + getEntityInterfaceClass().getSimpleName() +
					" by id: '" + course.getPrimaryKey() + "' " +
					"cause of: ", e);
			return null;
		}

		return course;
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseHome#update(java.lang.String, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, com.idega.util.IWTimestamp, com.idega.util.IWTimestamp, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Float, java.lang.Float, java.lang.Boolean, java.util.Date, java.lang.Boolean, java.lang.Boolean, java.lang.Boolean, java.util.Collection)
	 */
	@Override
	public Course update(
			String primaryKey,
			int courseNumber,
			String name,
			String handlerPersonalId,
			String courseTypePrimaryKey,
			String providerPrimaryKey,
			String coursePricePrimaryKey,
			java.util.Date startDate,
			java.util.Date endDate,
			String accountingKey,
			Integer birthYearFrom,
			Integer birthYearTo,
			Integer maxPer,
			Float price,
			Float cost,
			Boolean openForRegistration,
			java.util.Date registrationEnd,
			Boolean hasPreCare,
			Boolean hasPostCare,
			Boolean isPrivate,
			Collection<String> groupsWithAccessPrimaryKeys) {

		User handler = null;
		if (!StringUtil.isEmpty(handlerPersonalId)) {
			try {
				handler = getUserHome().findByPersonalID(handlerPersonalId);
			} catch (FinderException e) {
				java.util.logging.Logger.getLogger(getClass().getName()).warning(
						"Failed to get " + User.class.getSimpleName() +
						" by personal id: '" + handlerPersonalId + "'");
			}
		}

		return update(
				findByPrimaryKey(primaryKey), courseNumber, name, handler,
				getCourseTypeHome().findByPrimaryKey(courseTypePrimaryKey),
				getCourseProvider().findByPrimaryKeyRecursively(providerPrimaryKey),
				getCoursePriceHome().findByPrimaryKey(coursePricePrimaryKey),
				startDate, endDate, accountingKey, birthYearFrom, birthYearTo,
				maxPer, price, cost, openForRegistration, registrationEnd,
				hasPreCare, hasPostCare, isPrivate,
				getGroupHome().findGroups(groupsWithAccessPrimaryKeys));
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseHome#update(is.idega.idegaweb.egov.course.data.Course, java.lang.String, java.lang.String, is.idega.idegaweb.egov.course.data.CourseProvider, java.util.Date, java.util.Date, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.util.Date, java.util.ArrayList)
	 */
	@Override
	public Course update(Course course, String name,
			String courseHandlerPersonalId, CourseProvider center,
			String courseTypeId, java.util.Date startDate,
			java.util.Date endDate, String accountingKey, Integer birthYearFrom,
			Integer birthYearTo,
			Integer maximumParticipantsNumber, java.util.Date registrationEndDate, ArrayList<Group> accessGroups) {

		User handler = null;
		if (!StringUtil.isEmpty(courseHandlerPersonalId)) {
			try {
				handler = getUserHome().findByPersonalID(courseHandlerPersonalId);
			} catch (FinderException e) {
				java.util.logging.Logger.getLogger(getClass().getName()).warning(
						"Failed to get " + User.class.getSimpleName() +
						" by personal id: '" + courseHandlerPersonalId + "'");
			}
		}

		return update(course, -1, name, handler,
				getCourseTypeHome().findByPrimaryKey(courseTypeId), center, null,
				startDate, endDate, accountingKey, birthYearFrom, birthYearTo,
				maximumParticipantsNumber, null, null, null, registrationEndDate,
				null, null, true, accessGroups);
	}

	private GroupHome groupHome = null;

	protected GroupHome getGroupHome() {
		if (this.groupHome == null) {
			try {
				this.groupHome = (GroupHome) IDOLookup.getHome(Group.class);
			} catch (IDOLookupException e) {
				java.util.logging.Logger.getLogger(getClass().getName()).log(
						Level.WARNING,
						"Failed to get " + GroupHome.class +
						" cause of: ", e);
			}
		}

		return this.groupHome;
	}

	private CoursePriceHome coursePriceHome = null;

	protected CoursePriceHome getCoursePriceHome() {
		if (this.coursePriceHome == null) {
			try {
				this.coursePriceHome = (CoursePriceHome) IDOLookup.getHome(CoursePrice.class);
			} catch (IDOLookupException e) {
				java.util.logging.Logger.getLogger(getClass().getName()).log(
						Level.WARNING,
						"Failed to get " + CoursePriceHome.class.getSimpleName() +
						" cause of: ", e);
			}
		}

		return this.coursePriceHome;
	}

	private CourseProviderHome courseProviderHome = null;

	protected CourseProviderHome getCourseProvider() {
		if (this.courseProviderHome == null) {
			try {
				this.courseProviderHome = (CourseProviderHome) IDOLookup.getHome(CourseProvider.class);
			} catch (IDOLookupException e) {
				java.util.logging.Logger.getLogger(getClass().getName()).log(
						Level.WARNING,
						"Failed to get " + CourseProvider.class.getSimpleName() +
						" cause of: ", e);
			}
		}

		return this.courseProviderHome;
	}

	private CourseTypeHome courseTypeHome = null;

	protected CourseTypeHome getCourseTypeHome() {
		if (this.courseTypeHome == null) {
			try {
				this.courseTypeHome = (CourseTypeHome) IDOLookup.getHome(CourseType.class);
			} catch (IDOLookupException e) {
				java.util.logging.Logger.getLogger(getClass().getName()).log(
						Level.WARNING,
						"Failed to get " + CourseTypeHome.class.getSimpleName() +
						" cause of: ", e);
			}
		}

		return this.courseTypeHome;
	}

	private UserHome userHome = null;

	protected UserHome getUserHome() {
		if (this.userHome == null) {
			try {
				this.userHome = (UserHome) IDOLookup.getHome(User.class);
			} catch (IDOLookupException e) {
				Logger.getLogger(getClass().getName()).log(
						Level.WARNING,
						"Failed to get " + UserHome.class.getSimpleName() +
						" cause of: ", e);
			}
		}

		return this.userHome;
	}

	private CourseProviderTypeHome courseProviderTypeHome = null;

	protected CourseProviderTypeHome getCourseProviderTypeHome() {
		if (this.courseProviderTypeHome == null) {
			try {
				this.courseProviderTypeHome = (CourseProviderTypeHome) IDOLookup
						.getHome(CourseProviderType.class);
			} catch (IDOLookupException e) {
				Logger.getLogger(getClass().getName()).log(Level.WARNING,
						"failed to get " + CourseProviderTypeHome.class.getSimpleName() +
						" cause of: ", e);
			}
		}

		return this.courseProviderTypeHome;
	}

	protected CourseProviderHome getCourseProviderHome() {
		if (this.courseProviderHome == null) {
			try {
				this.courseProviderHome = (CourseProviderHome) IDOLookup.getHome(
						CourseProvider.class);
			} catch (IDOLookupException e) {
				Logger.getLogger(getClass().getName()).log(Level.WARNING,
						"Failed to get " + CourseProviderHome.class.getSimpleName() +
						" cause of: ", e);
			}
		}

		return this.courseProviderHome;
	}

	public int getNextCourseNumber() {
		try {
			return getHighestCourseNumber() + 1;
		} catch (IDOException ie) {
			return 1;
		}
	}
}