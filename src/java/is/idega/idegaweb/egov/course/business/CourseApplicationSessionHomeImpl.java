package is.idega.idegaweb.egov.course.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHomeImpl;

public class CourseApplicationSessionHomeImpl extends IBOHomeImpl implements CourseApplicationSessionHome {

	public Class getBeanInterfaceClass() {
		return CourseApplicationSession.class;
	}

	public CourseApplicationSession create() throws CreateException {
		return (CourseApplicationSession) super.createIBO();
	}
}