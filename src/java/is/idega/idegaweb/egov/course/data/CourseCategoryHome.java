package is.idega.idegaweb.egov.course.data;


import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;

public interface CourseCategoryHome extends IDOHome {

	public CourseCategory create() throws CreateException;

	public CourseCategory findByPrimaryKey(Object pk) throws FinderException;
}