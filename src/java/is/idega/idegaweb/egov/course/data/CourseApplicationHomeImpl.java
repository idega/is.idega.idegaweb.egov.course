package is.idega.idegaweb.egov.course.data;


import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOFactory;

public class CourseApplicationHomeImpl extends IDOFactory implements CourseApplicationHome {

	public Class getEntityInterfaceClass() {
		return CourseApplication.class;
	}

	public CourseApplication create() throws CreateException {
		return (CourseApplication) super.createIDO();
	}

	public CourseApplication findByPrimaryKey(Object pk) throws FinderException {
		return (CourseApplication) super.findByPrimaryKeyIDO(pk);
	}
}