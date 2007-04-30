package is.idega.idegaweb.egov.course.business;

import is.idega.idegaweb.egov.course.data.ApplicationHolder;
import is.idega.idegaweb.egov.course.data.Course;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.idega.business.IBOSessionBean;
import com.idega.user.data.User;

public class CourseApplicationSessionBean extends IBOSessionBean implements CourseApplicationSession {

	private Map applications;

	private Map getMap() {
		if (applications == null) {
			applications = new HashMap();
		}

		return applications;
	}

	public Map getApplications() {
		return getMap();
	}

	public Collection getUserApplications(User user) {
		return (Collection) getMap().get(user);
	}

	public void addApplication(User user, ApplicationHolder holder) {
		if (getMap().containsKey(user)) {
			Collection applications = (Collection) getMap().get(user);
			if (applications.contains(holder)) {
				applications.remove(holder);
			}
			applications.add(holder);
			getMap().put(user, applications);
		}
		else {
			Collection applications = new ArrayList();
			applications.add(holder);
			getMap().put(user, applications);
		}
	}

	public void removeApplication(User user, ApplicationHolder holder) {
		if (getMap().containsKey(user)) {
			Collection applications = (Collection) getMap().get(user);
			applications.remove(holder);
			getMap().put(user, applications);
		}
	}

	public boolean contains(User user, Course course) {
		if (getMap().containsKey(user)) {
			Collection applications = (Collection) getMap().get(user);

			ApplicationHolder holder = new ApplicationHolder();
			holder.setUser(user);
			holder.setCourse(course);

			return applications.contains(holder);
		}
		return false;
	}

	public void clear() {
		applications = null;
	}
}