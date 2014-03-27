package is.idega.idegaweb.egov.course.data;

import java.util.Collection;
import java.util.Collections;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.util.ListUtil;

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

	@Override
	public Collection<ChildCourse> findChildCourses(Course parentCourse) throws FinderException {
		ChildCourseBMPBean entity = (ChildCourseBMPBean) idoCheckOutPooledEntity();
		Collection<Integer> ids = entity.ejbFindChildCourses(parentCourse);
		if (ListUtil.isEmpty(ids)) {
			return Collections.emptyList();
		}
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

}