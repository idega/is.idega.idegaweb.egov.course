package is.idega.idegaweb.egov.course.data;


import java.sql.Date;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolType;
import com.idega.data.IDOException;
import com.idega.data.IDOHome;
import com.idega.data.IDORelationshipException;

public interface CourseHome extends IDOHome {

	public Course create() throws CreateException;

	public Course findByPrimaryKey(Object pk) throws FinderException;

	public Collection findAll() throws FinderException, IDORelationshipException;

	public Collection findAllByProvider(School provider) throws FinderException, IDORelationshipException;

	public Collection findAllByBirthYear(int birthYear) throws FinderException, IDORelationshipException;

	public Collection findAll(Object providerPK, Object schoolTypePK, Object courseTypePK, int birthYear, Date fromDate, Date toDate) throws FinderException, IDORelationshipException;

	public Collection findAll(Object providerPK, Object schoolTypePK, Object courseTypePK, int birthYear) throws FinderException, IDORelationshipException;

	public Collection findAll(Collection providers, Object schoolTypePK, Object courseTypePK) throws FinderException, IDORelationshipException;

	public Collection findAllByProviderAndSchoolTypeAndCourseType(School provider, SchoolType type, CourseType courseType, Date fromDate, Date toDate) throws FinderException;

	public int getCountBySchoolTypeAndBirthYear(Object schoolTypePK, int birthYear, Date fromDate) throws IDOException;

	public int getCountByCourseTypeAndBirthYear(Object courseTypePK, int birthYear, Date fromDate) throws IDOException;

	public int getCountBySchoolAndCourseTypeAndBirthYear(Object schoolPK, Object courseTypePK, int birthYear, Date fromDate) throws IDOException;

	public int getCountBySchoolAndBirthYear(Object schoolPK, int birthYear, Date fromDate) throws IDOException;

	public int getCountByProviderAndSchoolTypeAndCourseType(School provider, SchoolType type, CourseType courseType, Date fromDate, Date toDate) throws IDOException;

	public int getHighestCourseNumber() throws IDOException;

	public Collection findAllWithNoCourseNumber() throws FinderException;

	public Collection findAllByTypes(Collection<String> typesIds) throws FinderException;

	public Collection<Course> findAllByUser(String user) throws FinderException;

	public java.util.Collection<Course> findAllByGroupsIdsAndDates(java.util.Collection<Integer> groupsIds, Date periodFrom, Date periodTo, boolean findTemplates) throws javax.ejb.FinderException;

	public java.util.Collection<Course> findAllByCriteria(Collection<Integer> groupsIds,
														Collection<Integer> templateIds,
														java.util.Date periodFrom,
														java.util.Date periodTo,
														Integer birthYear,
														String sortBy,
														String nameOrNumber,
														Boolean openForRegistration,
														boolean findTemplates) throws javax.ejb.FinderException;

	public java.util.Collection<Course> findAllByTemplateIds(Collection<Integer> templateIds) throws javax.ejb.FinderException;


}