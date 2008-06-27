package is.idega.idegaweb.egov.course.data;


import com.idega.data.IDOException;
import com.idega.block.school.data.School;
import com.idega.data.IDORelationshipException;
import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import com.idega.block.school.data.SchoolType;
import javax.ejb.FinderException;
import java.sql.Date;

public interface CourseHome extends IDOHome {

	public Course create() throws CreateException;

	public Course findByPrimaryKey(Object pk) throws FinderException;

	public Collection findAll() throws FinderException, IDORelationshipException;

	public Collection findAllByProvider(School provider) throws FinderException, IDORelationshipException;

	public Collection findAllByBirthYear(int birthYear) throws FinderException, IDORelationshipException;

	public Collection findAll(Object providerPK, Object schoolTypePK, Object courseTypePK, int birthYear) throws FinderException, IDORelationshipException;
	
	public Collection findAll(Object providerPK, Object schoolTypePK, Object courseTypePK, int birthYear, Date fromDate, Date toDate) throws FinderException, IDORelationshipException;

	public Collection findAll(Collection providers, Object schoolTypePK, Object courseTypePK) throws FinderException, IDORelationshipException;

	public Collection findAllByProviderAndSchoolTypeAndCourseType(School provider, SchoolType type, CourseType courseType, Date fromDate, Date toDate) throws FinderException;

	public int getCountBySchoolTypeAndBirthYear(Object schoolTypePK, int birthYear, Date fromDate) throws IDOException;

	public int getCountByCourseTypeAndBirthYear(Object courseTypePK, int birthYear, Date fromDate) throws IDOException;

	public int getCountByProviderAndSchoolTypeAndCourseType(School provider, SchoolType type, CourseType courseType, Date fromDate, Date toDate) throws IDOException;
}