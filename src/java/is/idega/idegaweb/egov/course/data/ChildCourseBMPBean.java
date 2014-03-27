package is.idega.idegaweb.egov.course.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.query.Column;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;

public class ChildCourseBMPBean extends CourseBMPBean implements ChildCourse {

	private static final long serialVersionUID = 7425610935661180112L;

	private static final String PARENT_COURSE_ID = "parent_course_id";

	@Override
	public void initializeAttributes() {
		super.initializeAttributes();
		addOneToOneRelationship(PARENT_COURSE_ID, Course.class);
	}

	@Override
	public Course getParentCourse() {
		return (Course) getColumnValue(PARENT_COURSE_ID);
	}

	@Override
	public void setParentCourse(Course parentCourse) {
		setColumn(PARENT_COURSE_ID, parentCourse);
	}

	public Collection<Integer> ejbFindChildCourses(Course parentCourse) throws FinderException {
		Table table = new Table(this);
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new Column(table, getIDColumnName()));
		query.addCriteria(new MatchCriteria(table.getColumn(PARENT_COURSE_ID), MatchCriteria.EQUALS, parentCourse.getPrimaryKey()));
		return this.idoFindPKsByQuery(query);
	}

}