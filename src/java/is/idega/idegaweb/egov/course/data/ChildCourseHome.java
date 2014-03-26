package is.idega.idegaweb.egov.course.data;

import javax.ejb.CreateException;

public interface ChildCourseHome extends CourseHome {

	@Override
	public ChildCourse create() throws CreateException;

}