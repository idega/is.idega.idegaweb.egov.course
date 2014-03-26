package is.idega.idegaweb.egov.course.data;

import javax.ejb.CreateException;

public class ChildCourseHomeImpl extends CourseHomeImpl implements ChildCourseHome {

	private static final long serialVersionUID = -2624860953592962255L;

	@Override
	public Class getEntityInterfaceClass() {
		return ChildCourse.class;
	}

	@Override
	public ChildCourse create() throws CreateException {
		return (ChildCourse) super.createIDO();
	}

}