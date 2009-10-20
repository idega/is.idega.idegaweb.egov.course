package is.idega.idegaweb.egov.course.business.impl;

import is.idega.idegaweb.egov.course.business.CourseBusiness;
import is.idega.idegaweb.egov.course.business.CourseServices;
import is.idega.idegaweb.egov.course.data.Course;
import is.idega.idegaweb.egov.course.data.CourseType;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWMainApplication;
import com.idega.util.StringUtil;

@Service
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class CourseServicesImpl implements CourseServices {

	private static final Logger LOGGER = Logger.getLogger(CourseServicesImpl.class.getName());
	
	public Collection<Course> getCoursesByType(String courseType) {
		if (StringUtil.isEmpty(courseType)) {
			LOGGER.warning("Course type is undefined!");
		}
		
		CourseBusiness courseBusiness = getCourseBusiness();
		if (courseBusiness == null) {
			return null;
		}
		
		CourseType type = null;
		try {
			type = courseBusiness.getCourseTypeHome().findByName(courseType);
		} catch(Exception e) {
			LOGGER.log(Level.WARNING, "Error getting courses type by " + courseType, e);
		}
		if (type == null) {
			return null;
		}
		
		try {
			return courseBusiness.getCoursesByTypes(Collections.singleton(type.getPrimaryKey().toString()));
		} catch (RemoteException e) {
			LOGGER.log(Level.WARNING, "No courses found by type: " + type.getAbbreviation(), e);
		}
		
		return null;
	}
	
	private CourseBusiness getCourseBusiness() {
		try {
			return IBOLookup.getServiceInstance(IWMainApplication.getDefaultIWApplicationContext(), CourseBusiness.class);
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Error getting " + CourseBusiness.class);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public Collection<Course> getAllAvailableCourses() {
		CourseBusiness courseBusiness = getCourseBusiness();
		if (courseBusiness == null) {
			return null;
		}
		
		try {
			return courseBusiness.getAllCourses();
		} catch (RemoteException e) {
			LOGGER.log(Level.WARNING, "Error getting all courses", e);
		}
		
		return null;
	}

}
