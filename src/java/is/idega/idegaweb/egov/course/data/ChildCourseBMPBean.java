package is.idega.idegaweb.egov.course.data;

public class ChildCourseBMPBean extends CourseBMPBean {

	private static final long serialVersionUID = 7425610935661180112L;

	private static final String PARENT_COURSE_ID = "parent_course_id";

	@Override
	public void initializeAttributes() {
		super.initializeAttributes();
		addOneToOneRelationship(PARENT_COURSE_ID, Course.class);
	}

}