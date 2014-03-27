package is.idega.idegaweb.egov.course.data;

public interface ChildCourse extends Course {

	public Course getParentCourse();

	public void setParentCourse(Course parentCourse);

}