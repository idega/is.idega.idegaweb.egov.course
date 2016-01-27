package is.idega.idegaweb.egov.course.data;


import java.sql.Date;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolType;
import com.idega.data.IDOEntity;
import com.idega.data.IDOException;
import com.idega.data.IDOFactory;
import com.idega.data.IDORelationshipException;

public class CourseHomeImpl extends IDOFactory implements CourseHome {

	@Override
	public Class<Course> getEntityInterfaceClass() {
		return Course.class;
	}

	@Override
	public Course create() throws CreateException {
		return (Course) super.createIDO();
	}

	@Override
	public Course findByPrimaryKey(Object pk) throws FinderException {
		return (Course) super.findByPrimaryKeyIDO(pk);
	}

	@Override
	public Collection findAll() throws FinderException, IDORelationshipException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CourseBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	@Override
	public Collection findAllByProvider(School provider) throws FinderException, IDORelationshipException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CourseBMPBean) entity).ejbFindAllByProvider(provider);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	@Override
	public Collection findAllByBirthYear(int birthYear) throws FinderException, IDORelationshipException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CourseBMPBean) entity).ejbFindAllByBirthYear(birthYear);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	@Override
	public Collection findAll(Object providerPK, Object schoolTypePK, Object courseTypePK, int birthYear, Date fromDate, Date toDate) throws FinderException, IDORelationshipException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CourseBMPBean) entity).ejbFindAll(providerPK, schoolTypePK, courseTypePK, birthYear, fromDate, toDate);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	@Override
	public Collection findAll(Object providerPK, Object schoolTypePK, Object courseTypePK, int birthYear) throws FinderException, IDORelationshipException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CourseBMPBean) entity).ejbFindAll(providerPK, schoolTypePK, courseTypePK, birthYear);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	@Override
	public Collection findAll(Collection providers, Object schoolTypePK, Object courseTypePK) throws FinderException, IDORelationshipException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CourseBMPBean) entity).ejbFindAll(providers, schoolTypePK, courseTypePK);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	@Override
	public Collection findAllByProviderAndSchoolTypeAndCourseType(School provider, SchoolType type, CourseType courseType, Date fromDate, Date toDate) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CourseBMPBean) entity).ejbFindAllByProviderAndSchoolTypeAndCourseType(provider, type, courseType, fromDate, toDate);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	@Override
	public int getCountBySchoolTypeAndBirthYear(Object schoolTypePK, int birthYear, Date fromDate) throws IDOException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((CourseBMPBean) entity).ejbHomeGetCountBySchoolTypeAndBirthYear(schoolTypePK, birthYear, fromDate);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	@Override
	public int getCountByCourseTypeAndBirthYear(Object courseTypePK, int birthYear, Date fromDate) throws IDOException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((CourseBMPBean) entity).ejbHomeGetCountByCourseTypeAndBirthYear(courseTypePK, birthYear, fromDate);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	@Override
	public int getCountBySchoolAndCourseTypeAndBirthYear(Object schoolPK, Object courseTypePK, int birthYear, Date fromDate) throws IDOException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((CourseBMPBean) entity).ejbHomeGetCountBySchoolAndCourseTypeAndBirthYear(schoolPK, courseTypePK, birthYear, fromDate);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	@Override
	public int getCountBySchoolAndBirthYear(Object schoolPK, int birthYear, Date fromDate) throws IDOException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((CourseBMPBean) entity).ejbHomeGetCountBySchoolAndBirthYear(schoolPK, birthYear, fromDate);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	@Override
	public int getCountByProviderAndSchoolTypeAndCourseType(School provider, SchoolType type, CourseType courseType, Date fromDate, Date toDate) throws IDOException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((CourseBMPBean) entity).ejbHomeGetCountByProviderAndSchoolTypeAndCourseType(provider, type, courseType, fromDate, toDate);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	@Override
	public int getHighestCourseNumber() throws IDOException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((CourseBMPBean) entity).ejbHomeGetHighestCourseNumber();
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	@Override
	public Collection findAllWithNoCourseNumber() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CourseBMPBean) entity).ejbFindAllWithNoCourseNumber();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	@Override
	public Collection findAllByTypes(Collection<String> typesIds) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CourseBMPBean) entity).ejbFindAllByTypes(typesIds);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	@Override
	public Collection<Course> findAllByUser(String user) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CourseBMPBean) entity).ejbFindAllByUser(user);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	@Override
	public java.util.Collection<Course> findAllByGroupsIdsAndDates(java.util.Collection<Integer> groupsIds, Date periodFrom, Date periodTo) throws javax.ejb.FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection<Integer> ids = ((CourseBMPBean)entity).ejbFindAllByGroupsIdsAndDates(groupsIds, periodFrom, periodTo);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}