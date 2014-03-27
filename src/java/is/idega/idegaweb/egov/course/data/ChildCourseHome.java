package is.idega.idegaweb.egov.course.data;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface ChildCourseHome extends CourseHome {

	@Override
	public ChildCourse create() throws CreateException;

	public Collection<ChildCourse> findChildCourses(Course parentCourse) throws FinderException;

}