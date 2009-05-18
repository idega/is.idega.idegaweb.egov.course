package is.idega.idegaweb.egov.course.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHomeImpl;

public class CourseBusinessHomeImpl extends IBOHomeImpl implements
		CourseBusinessHome {
	public Class getBeanInterfaceClass() {
		return CourseBusiness.class;
	}

	public CourseBusiness create() throws CreateException {
		return (CourseBusiness) super.createIBO();
	}
}