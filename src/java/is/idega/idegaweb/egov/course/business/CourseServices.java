package is.idega.idegaweb.egov.course.business;

import is.idega.idegaweb.egov.course.data.Course;

import java.util.Collection;
import java.util.Collections;

/**
 * Proxy to {@link CourseBusiness} and additional methods. Spring bean.
 * 
 * @author <a href="mailto:valdas@idega.com">Valdas Žemaitis</a>
 * @version $Revision: 1.0 $
 *
 * Last modified: $Date: 2009.10.01 16:25:41 $ by: $Author: valdas $
 */
public interface CourseServices {

	/**
	 * 
	 * @return all {@link Course}s in data source or 
	 * {@link Collections#emptyList()} on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public Collection<Course> getAllAvailableCourses();
	
	public Collection<Course> getCoursesByType(String courseType);
	
	public Course getCourseById(String courseId);
}