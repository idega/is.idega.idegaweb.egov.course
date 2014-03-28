package is.idega.idegaweb.egov.course.data;


import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.IDOException;
import com.idega.data.IDOHome;
import com.idega.data.IDORelationshipException;
import com.idega.user.data.Group;
import com.idega.user.data.User;

public interface CourseHome extends IDOHome {

	public Course create() throws CreateException;

	/**
	 * 
	 * @param pk is {@link Course#getPrimaryKey()} to search by, 
	 * not <code>null</code>;
	 * @return {@link Course} found or <code>null</code> on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public Course findByPrimaryKey(Object pk);

	/**
	 * 
	 * @return all {@link Course}s or {@link Collections#emptyList()} on
	 * failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public Collection<Course> findAll();

	/**
	 * 
	 * @param provider to search by, not <code>null</code>;
	 * @return entities by criteria or {@link Collections#emptyList()}
	 * on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public Collection<Course> findAllByProvider(CourseProvider provider);

	/**
	 * 
	 * @param birthYear is between {@link Course#getBirthyearFrom()} and
	 * {@link Course#getBirthyearTo()}, skipped if less that 0;
	 * @return entities by criteria or {@link Collections#emptyList()}
	 * on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public Collection<Course> findAllByBirthYear(int birthYear);

	/**
	 * 
	 * @param providerPK is {@link CourseProvider#getPrimaryKey()}, 
	 * skipped if <code>null</code>;
	 * @param schoolTypePK is {@link CourseType#getPrimaryKey()}, 
	 * skipped if <code>null</code>;
	 * @param courseTypePK is {@link CourseType#getPrimaryKey()}, 
	 * skipped if <code>null</code>;
	 * @param birthYear is between {@link Course#getBirthyearFrom()} and
	 * {@link Course#getBirthyearTo()}, skipped if less that 0;
	 * @param fromDate is {@link Course#getStartDate()} to filter by,
	 * skipped if <code>null</code>;
	 * @param toDate is {@link Course#getStartDate()} to filter by,
	 * skipped if <code>null</code>;
	 * @return entities by criteria or {@link Collections#emptyList()}
	 * on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public Collection<Course> findAll(
			String providerPK, 
			String schoolTypePK, 
			String courseTypePK, 
			int birthYear, 
			Date fromDate, 
			Date toDate);

	/**
	 * 
	 * @param providerPK is {@link CourseProvider#getPrimaryKey()}, 
	 * skipped if <code>null</code>;
	 * @param schoolTypePK is {@link CourseType#getPrimaryKey()}, 
	 * skipped if <code>null</code>;
	 * @param courseTypePK is {@link CourseType#getPrimaryKey()}, 
	 * skipped if <code>null</code>;
	 * @param birthYear is between {@link Course#getBirthyearFrom()} and
	 * {@link Course#getBirthyearTo()}, skipped if less that 0;
	 * @param fromDate is {@link Course#getStartDate()} to filter by,
	 * skipped if <code>null</code>;
	 * @param toDate is {@link Course#getStartDate()} to filter by,
	 * skipped if <code>null</code>;
	 * @return {@link Collection} of {@link Course#getPrimaryKey()} by criteria 
	 * or {@link Collections#emptyList()} on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public Collection<Object> findAllPrimaryKeys(
			String providerPK, 
			String schoolTypePK, 
			String courseTypePK, 
			int birthYear, 
			Date fromDate, 
			Date toDate);

	/**
	 * 
	 * @param providerPK is {@link CourseProvider#getPrimaryKey()}, 
	 * skipped if <code>null</code>;
	 * @param schoolTypePK is {@link CourseType#getPrimaryKey()}, 
	 * skipped if <code>null</code>;
	 * @param courseTypePK is {@link CourseType#getPrimaryKey()}, 
	 * skipped if <code>null</code>;
	 * @param birthYear is between {@link Course#getBirthyearFrom()} and
	 * {@link Course#getBirthyearTo()}, skipped if less that 0;
	 * @return {@link Collection} of {@link Course}s or
	 * {@link Collections#emptyList()} on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public Collection<Course> findAll(
			String providerPK, 
			String schoolTypePK, 
			String courseTypePK, 
			int birthYear);

	public Collection<Course> findAll(
			Collection<CourseProvider> providers, 
			Object schoolTypePK, Object courseTypePK) throws FinderException, IDORelationshipException;

	/**
	 * 
	 * <p>Finds all entities by following criteria:</p>
	 * @param provider to filter by, skipped if <code>null</code>;
	 * @param type to filter by, skipped if <code>null</code>;
	 * @param courseType to filter by, skipped if <code>null</code>;
	 * @param fromDate is floor for course start date, 
	 * skipped if <code>null</code>;
	 * @param toDate is ceiling for course start date, 
	 * skipped if <code>null</code>;
	 * @return found entities or {@link Collections#emptyList()} on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public Collection<Course> findAllByProviderAndSchoolTypeAndCourseType(
			CourseProvider provider, 
			CourseProviderType type, 
			CourseType courseType, 
			Date fromDate, 
			Date toDate);

	public int getCountBySchoolTypeAndBirthYear(Object schoolTypePK, int birthYear, Date fromDate) throws IDOException;

	public int getCountByCourseTypeAndBirthYear(Object courseTypePK, int birthYear, Date fromDate) throws IDOException;

	public int getCountBySchoolAndCourseTypeAndBirthYear(Object schoolPK, Object courseTypePK, int birthYear, Date fromDate) throws IDOException;

	public int getCountBySchoolAndBirthYear(Object schoolPK, int birthYear, Date fromDate) throws IDOException;

	/**
	 *
	 * <p>Finds all primary keys by following criteria and counts number of 
	 * results.</p>
	 * @param provider to filter by, skipped if <code>null</code>;
	 * @param type to filter by, skipped if <code>null</code>;
	 * @param courseType to filter by, skipped if <code>null</code>;
	 * @param fromDate is floor for course start date,
	 * skipped if <code>null</code>;
	 * @param toDate is ceiling for course start date,
	 * skipped if <code>null</code>;
	 * @return count of primary keys found data source or 0
	 * on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public int getCountByProviderAndSchoolTypeAndCourseType(
			CourseProvider provider, CourseProviderType type, 
			CourseType courseType, Date fromDate, Date toDate);

	public int getHighestCourseNumber() throws IDOException;

	public Collection<Course> findAllWithNoCourseNumber() throws FinderException;

	public Collection<Course> findAllByTypes(Collection<String> typesIds) throws FinderException;

	public Collection<Course> findAllByUser(String user) throws FinderException;

	/**
	 * 
	 * <p>All {@link Course}s by following criteria:</p>
	 * @param courseProviders to filter by, skipped if <code>null</code>;
	 * @param couserProviderTypes to filter by, skipped if <code>null</code>;
	 * @param courseTypes to filter by, skipped if <code>null</code>;
	 * @param birthDateFrom is floor of age of course attender, 
	 * skipped if <code>null</code>;
	 * @param birthDateTo is ceiling of age of course attender, 
	 * skipped if <code>null</code>;
	 * @param fromDate is floor for course start date, 
	 * skipped if <code>null</code>;
	 * @param toDate is ceiling for course start date, 
	 * skipped if <code>null</code>;
	 * @param isPrivate tells if {@link Course}s can be viewed by all, 
	 * skipped if <code>null</code>;
	 * @param groupsWithAccess is {@link Group}, which can view private 
	 * {@link Course}s, skipped if <code>null</code>;
	 * @return entities or {@link Collections#emptyList()} on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	Collection<Course> findAll(
			Collection<? extends CourseProvider> courseProviders,
			Collection<? extends CourseProviderType> couserProviderTypes,
			Collection<? extends CourseType> courseTypes, Date birthDateFrom,
			Date birthDateTo, Date fromDate, Date toDate, Boolean isPrivate,
			Collection<Group> groupsWithAccess);

	/**
	 * 
	 * <p>All {@link Course}s by following criteria:</p>
	 * @param courseProviders to filter by, skipped if <code>null</code>;
	 * @param couserProviderTypes to filter by, skipped if <code>null</code>;
	 * @param courseTypes to filter by, skipped if <code>null</code>;
	 * @param birthDateFrom is floor of age of course attender, 
	 * skipped if <code>null</code>;
	 * @param birthDateTo is ceiling of age of course attender, 
	 * skipped if <code>null</code>;
	 * @param fromDate is floor for course start date, 
	 * skipped if <code>null</code>;
	 * @param toDate is ceiling for course start date, 
	 * skipped if <code>null</code>;
	 * @param isPrivate tells if {@link Course}s can be viewed by all, 
	 * skipped if <code>null</code>;
	 * @param groupsWithAccess is {@link Group}, which can view private 
	 * {@link Course}s, skipped if <code>null</code>;
	 * @param notChild <code>true</code> if {@link ChildCourse}s
	 * should be excluded;
	 * @return entities or {@link Collections#emptyList()} on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	Collection<Course> findAll(
			Collection<? extends CourseProvider> courseProviders,
			Collection<? extends CourseProviderType> couserProviderTypes,
			Collection<? extends CourseType> courseTypes, Date birthDateFrom,
			Date birthDateTo, Date fromDate, Date toDate, Boolean isPrivate,
			Collection<Group> groupsWithAccess, boolean notChild);

	/**
	 * 
	 * <p>Removes entity.</p>
	 * @param primaryKey is {@link Course#getPrimaryKey()}, not null;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	void remove(String primaryKey);

	/**
	 * 
	 * <p>Removes entity.</p>
	 * @param course is entity to remove, not <code>null</code>;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	void remove(Course course);

	/**
	 * 
	 * <p>Creates, updates {@link Course}</p>
	 * @param course to update, new one created if <code>null</code>;
	 * @param courseNumber
	 * @param name is {@link Course#getName()}, skipped if <code>null</code>;
	 * @param handler is {@link Course#getUser()}, skipped if <code>null</code>;
	 * @param courseType to assign, skipped if <code>null</code>;
	 * @param provider to assign, skipped if <code>null</code>;
	 * @param coursePrice to assign, skipped if <code>null</code>;
	 * @param startDate when {@link Course} should be available for attending, 
	 * skipped if <code>null</code>;
	 * @param endDate when course ends, skipped if <code>null</code>;
	 * @param accountingKey
	 * @param birthYearFrom is floor of age for attendants, 
	 * skipped if <code>null</code>;
	 * @param birthYearTo is ceiling of age for attendants, 
	 * skipped if <code>null</code>;
	 * @param maxPer is maximum number of attendants, 
	 * skipped if <code>null</code>;
	 * @param price same as coursePrice, but without type, 
	 * skipped if <code>null</code>;
	 * @param cost
	 * @param openForRegistration
	 * @param registrationEnd
	 * @param hasPreCare
	 * @param hasPostCare
	 * @param isPrivate tells if all {@link User}s can take or manage this 
	 * course or only certain groups, skipped if <code>null</code>;
	 * @param groupsWithAccess tells which {@link Group} {@link User}s can
	 * attend/manage course, skipped if <code>null</code>. Works only with
	 * isPrivate property;
	 * @return created/updated entity or <code>null</code> on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	Course update(Course course, int courseNumber, String name, User handler,
			CourseType courseType, CourseProvider provider,
			CoursePrice coursePrice, java.util.Date startDate,
			java.util.Date endDate, String accountingKey,
			Integer birthYearFrom, Integer birthYearTo, Integer maxPer,
			Float price, Float cost, Boolean openForRegistration,
			java.util.Date registrationEnd, Boolean hasPreCare,
			Boolean hasPostCare, Boolean isPrivate,
			Collection<Group> groupsWithAccess);

	/**
	 * 
	 * <p>Creates, updates {@link Course}</p>
	 * @param primaryKey is {@link Course#getPrimaryKey()}, new {@link Course}
	 * will be created if <code>null</code>;
	 * @param courseNumber
	 * @param name is {@link Course#getName()}, skipped if <code>null</code>;
	 * @param handlerPersonalId is {@link User#getPersonalID()} of handler of
	 * this {@link Course}, skipped if null;
	 * @param courseTypePrimaryKey is {@link CourseType#getPrimaryKey()}, 
	 * skipped if <code>null</code>;
	 * @param providerPrimaryKey is {@link CourseProvider#getPrimaryKey()}, 
	 * skipped if <code>null</code>;
	 * @param coursePricePrimaryKey is {@link CoursePrice#getPrimaryKey()},
	 * skipped if <code>null</code>;
	 * @param startDate when {@link Course} should be available for attending, 
	 * skipped if <code>null</code>;
	 * @param endDate when course ends, skipped if <code>null</code>;
	 * @param accountingKey
	 * @param birthYearFrom is floor of age for attendants, 
	 * skipped if <code>null</code>;
	 * @param birthYearTo is ceiling of age for attendants, 
	 * skipped if <code>null</code>;
	 * @param maxPer is maximum number of attendants, 
	 * skipped if <code>null</code>;
	 * @param price same as coursePrice, but without type, 
	 * skipped if <code>null</code>;
	 * @param cost
	 * @param openForRegistration
	 * @param registrationEnd
	 * @param hasPreCare
	 * @param hasPostCare
	 * @param isPrivate tells if all {@link User}s can take or manage this 
	 * course or only certain groups, skipped if <code>null</code>;
	 * @param groupsWithAccess is {@link Collection} of {@link Group#getPrimaryKey()}.
	 * Tells which {@link Group} {@link User}s can
	 * attend/manage course, skipped if <code>null</code>. Works only with
	 * isPrivate property;
	 * @return created/updated entity or <code>null</code> on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	Course update(String primaryKey, int courseNumber, String name,
			String handlerPersonalId, String courseTypePrimaryKey,
			String providerPrimaryKey, String coursePricePrimaryKey,
			java.util.Date startDate, java.util.Date endDate, String accountingKey,
			Integer birthYearFrom, Integer birthYearTo, Integer maxPer,
			Float price, Float cost, Boolean openForRegistration,
			java.util.Date registrationEnd, Boolean hasPreCare,
			Boolean hasPostCare, Boolean isPrivate,
			Collection<String> groupsWithAccess);

	/**
	 * 
	 * <p>Creates, updates {@link Course}</p>
	 * @param course to update, new one created if <code>null</code>;
	 * @param name is {@link Course#getName()}, skipped if <code>null</code>;
	 * @param courseHandlerPersonalId is {@link User#getPersonalID()} of handler of
	 * this {@link Course}, skipped if null;
	 * @param center to assign, skipped if <code>null</code>;
	 * when {@link Course} should be available for attending, 
	 * skipped if <code>null</code>;
	 * @param courseTypeId is {@link CourseType#getPrimaryKey()}, 
	 * skipped if <code>null</code>;
	 * @param endDate when course ends, skipped if <code>null</code>;
	 * @param accountingKey
	 * @param birthYearFrom is floor of age for attendants, 
	 * skipped if <code>null</code>;
	 * @param birthYearTo is ceiling of age for attendants, 
	 * skipped if <code>null</code>;
	 * @param maximumParticipantsNumber is maximum number of attendants, 
	 * skipped if <code>null</code>;
	 * @param registrationEndDate
	 * @param accessGroups tells which {@link Group} {@link User}s can
	 * attend/manage course, skipped if <code>null</code>;
	 * @return created/updated entity or <code>null</code> on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	Course update(Course course, String name,
			String courseHandlerPersonalId, CourseProvider center,
			String courseTypeId, java.util.Date startDate,
			java.util.Date endDate, String accountingKey, Integer birthYearFrom,
			Integer birthYearTo,
			Integer maximumParticipantsNumber, 
			java.util.Date registrationEndDate,
			ArrayList<Group> accessGroups);
}