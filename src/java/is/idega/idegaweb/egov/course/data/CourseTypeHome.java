package is.idega.idegaweb.egov.course.data;


import com.idega.data.IDORelationshipException;
import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;

public interface CourseTypeHome extends IDOHome {

	public CourseType create() throws CreateException;

	public CourseType findByPrimaryKey(Object pk) throws FinderException;

	public Collection findAll() throws FinderException;

	public Collection findAllBySchoolType(Object schoolTypePK) throws FinderException, IDORelationshipException;
}