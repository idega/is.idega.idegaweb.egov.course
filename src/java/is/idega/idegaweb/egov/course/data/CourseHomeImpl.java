package is.idega.idegaweb.egov.course.data;


import com.idega.data.IDOException;
import com.idega.block.school.data.School;
import com.idega.data.IDORelationshipException;
import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.block.school.data.SchoolType;
import javax.ejb.FinderException;
import java.sql.Date;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class CourseHomeImpl extends IDOFactory implements CourseHome {

	public Class getEntityInterfaceClass() {
		return Course.class;
	}

	public Course create() throws CreateException {
		return (Course) super.createIDO();
	}

	public Course findByPrimaryKey(Object pk) throws FinderException {
		return (Course) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException, IDORelationshipException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CourseBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByProvider(School provider) throws FinderException, IDORelationshipException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CourseBMPBean) entity).ejbFindAllByProvider(provider);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByBirthYear(int birthYear) throws FinderException, IDORelationshipException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CourseBMPBean) entity).ejbFindAllByBirthYear(birthYear);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAll(Object providerPK, Object schoolTypePK, Object courseTypePK, int birthYear) throws FinderException, IDORelationshipException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CourseBMPBean) entity).ejbFindAll(providerPK, schoolTypePK, courseTypePK, birthYear);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAll(Collection providers, Object schoolTypePK, Object courseTypePK) throws FinderException, IDORelationshipException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CourseBMPBean) entity).ejbFindAll(providers, schoolTypePK, courseTypePK);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByProviderAndSchoolTypeAndCourseType(School provider, SchoolType type, CourseType courseType, Date fromDate, Date toDate) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CourseBMPBean) entity).ejbFindAllByProviderAndSchoolTypeAndCourseType(provider, type, courseType, fromDate, toDate);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public int getCountBySchoolTypeAndBirthYear(Object schoolTypePK, int birthYear, Date fromDate) throws IDOException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((CourseBMPBean) entity).ejbHomeGetCountBySchoolTypeAndBirthYear(schoolTypePK, birthYear, fromDate);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getCountByProviderAndSchoolTypeAndCourseType(School provider, SchoolType type, CourseType courseType, Date fromDate, Date toDate) throws IDOException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((CourseBMPBean) entity).ejbHomeGetCountByProviderAndSchoolTypeAndCourseType(provider, type, courseType, fromDate, toDate);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}
}