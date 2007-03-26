package is.idega.idegaweb.egov.course.data;


import com.idega.data.IDORelationshipException;
import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import com.idega.block.school.data.SchoolType;
import javax.ejb.FinderException;

public interface CoursePriceHome extends IDOHome {
	public CoursePrice create() throws CreateException;

	public CoursePrice findByPrimaryKey(Object pk) throws FinderException;

	public Collection findAll() throws FinderException,
			IDORelationshipException;

	public Collection findAll(SchoolType sType, CourseType cType)
			throws FinderException, IDORelationshipException;
}