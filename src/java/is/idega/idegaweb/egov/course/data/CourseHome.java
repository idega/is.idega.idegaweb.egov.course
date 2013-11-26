package is.idega.idegaweb.egov.course.data;


import java.sql.Date;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.IDOException;
import com.idega.data.IDOHome;
import com.idega.data.IDORelationshipException;

public interface CourseHome extends IDOHome {

	public Course create() throws CreateException;

	public Course findByPrimaryKey(Object pk) throws FinderException;

	public Collection<Course> findAll() throws FinderException, IDORelationshipException;

	public Collection<Course> findAllByProvider(CourseProvider provider) throws FinderException, IDORelationshipException;

	public Collection<Course> findAllByBirthYear(int birthYear) throws FinderException, IDORelationshipException;

	public Collection<Course> findAll(Object providerPK, Object schoolTypePK, Object courseTypePK, int birthYear, Date fromDate, Date toDate) throws FinderException, IDORelationshipException;

	public Collection<Course> findAll(Object providerPK, Object schoolTypePK, Object courseTypePK, int birthYear) throws FinderException, IDORelationshipException;

	public Collection<Course> findAll(Collection<CourseProvider> providers, Object schoolTypePK, Object courseTypePK) throws FinderException, IDORelationshipException;

	public Collection<Course> findAllByProviderAndSchoolTypeAndCourseType(CourseProvider provider, CourseProviderType type, CourseType courseType, Date fromDate, Date toDate) throws FinderException;

	public int getCountBySchoolTypeAndBirthYear(Object schoolTypePK, int birthYear, Date fromDate) throws IDOException;

	public int getCountByCourseTypeAndBirthYear(Object courseTypePK, int birthYear, Date fromDate) throws IDOException;

	public int getCountBySchoolAndCourseTypeAndBirthYear(Object schoolPK, Object courseTypePK, int birthYear, Date fromDate) throws IDOException;

	public int getCountBySchoolAndBirthYear(Object schoolPK, int birthYear, Date fromDate) throws IDOException;

	public int getCountByProviderAndSchoolTypeAndCourseType(CourseProvider provider, CourseProviderType type, CourseType courseType, Date fromDate, Date toDate) throws IDOException;

	public int getHighestCourseNumber() throws IDOException;

	public Collection<Course> findAllWithNoCourseNumber() throws FinderException;

	public Collection<Course> findAllByTypes(Collection<String> typesIds) throws FinderException;

	public Collection<Course> findAllByUser(String user) throws FinderException;
}