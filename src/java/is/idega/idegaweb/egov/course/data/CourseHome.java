package is.idega.idegaweb.egov.course.data;


import com.idega.data.IDORelationshipException;
import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;

public interface CourseHome extends IDOHome {
	public Course create() throws CreateException;

	public Course findByPrimaryKey(Object pk) throws FinderException;

	public Collection findAll() throws FinderException,
			IDORelationshipException;

	public Collection findAll(Integer schoolTypeID, Integer courseTypeID,
			int birthYear) throws FinderException, IDORelationshipException;
}