package is.idega.idegaweb.egov.course.data;


import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;

public interface CourseApplicationHome extends IDOHome {

	public CourseApplication create() throws CreateException;

	public CourseApplication findByPrimaryKey(Object pk) throws FinderException;
}