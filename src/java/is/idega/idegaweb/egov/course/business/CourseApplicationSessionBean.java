package is.idega.idegaweb.egov.course.business;

import is.idega.idegaweb.egov.course.data.ApplicationHolder;
import is.idega.idegaweb.egov.course.data.Course;

import java.util.ArrayList;
import java.util.Collection;

import com.idega.business.IBOSessionBean;
import com.idega.user.data.User;

public class CourseApplicationSessionBean extends IBOSessionBean implements CourseApplicationSession {

	private ArrayList applications;

	private ArrayList getList() {
		if (applications == null) {
			applications = new ArrayList();
		}
		
		return applications;
	}
	
	public Collection getApplications() {
		return getList();
	}

	public void addApplication(ApplicationHolder holder) {
		if (getList().contains(holder)) {
			getList().remove(holder);
		}
		getList().add(holder);
	}
	
	public void removedApplication(ApplicationHolder holder) {
		getList().remove(holder);
	}
	
	public boolean contains(User user, Course course) {
		ApplicationHolder holder = new ApplicationHolder();
		holder.setUser(user);
		holder.setCourse(course);
		
		return getList().contains(holder);
	}
	
}
