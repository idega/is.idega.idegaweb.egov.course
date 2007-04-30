package is.idega.idegaweb.egov.course.data;


import com.idega.data.IDORelationshipException;
import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;
import java.sql.Date;
import com.idega.block.school.data.SchoolArea;

public interface CoursePriceHome extends IDOHome {

	public CoursePrice create() throws CreateException;

	public CoursePrice findByPrimaryKey(Object pk) throws FinderException;

	public Collection findAll() throws FinderException, IDORelationshipException;

	public Collection findAll(Date fromDate, Date toDate) throws FinderException, IDORelationshipException;

	public Collection findAll(SchoolArea area, CourseType cType) throws FinderException, IDORelationshipException;

	public Collection findAll(SchoolArea area, CourseType cType, Date fromDate, Date toDate) throws FinderException, IDORelationshipException;
}