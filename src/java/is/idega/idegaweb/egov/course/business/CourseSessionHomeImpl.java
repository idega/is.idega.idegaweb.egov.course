package is.idega.idegaweb.egov.course.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHomeImpl;

public class CourseSessionHomeImpl extends IBOHomeImpl implements
		CourseSessionHome {
	public Class getBeanInterfaceClass() {
		return CourseSession.class;
	}

	public CourseSession create() throws CreateException {
		return (CourseSession) super.createIBO();
	}
}