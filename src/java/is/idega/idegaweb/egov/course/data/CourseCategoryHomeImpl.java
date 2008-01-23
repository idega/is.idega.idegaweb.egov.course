package is.idega.idegaweb.egov.course.data;


import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOFactory;

public class CourseCategoryHomeImpl extends IDOFactory implements CourseCategoryHome {

	public Class getEntityInterfaceClass() {
		return CourseCategory.class;
	}

	public CourseCategory create() throws CreateException {
		return (CourseCategory) super.createIDO();
	}

	public CourseCategory findByPrimaryKey(Object pk) throws FinderException {
		return (CourseCategory) super.findByPrimaryKeyIDO(pk);
	}
}