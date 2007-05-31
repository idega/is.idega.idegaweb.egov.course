package is.idega.idegaweb.egov.course.business;

import is.idega.idegaweb.egov.course.data.ApplicationHolder;
import is.idega.idegaweb.egov.course.data.Course;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ejb.EJBException;
import javax.servlet.http.HttpSessionBindingEvent;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOSessionBean;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWMainApplication;
import com.idega.user.data.User;

public class CourseApplicationSessionBean extends IBOSessionBean implements CourseApplicationSession {

	private Map applications;

	public void valueBound(HttpSessionBindingEvent arg0) {
	}

	public void valueUnbound(HttpSessionBindingEvent arg0) {
		IWMainApplication iwma = IWMainApplication.getIWMainApplication(arg0.getSession().getServletContext());
		IWApplicationContext iwac = iwma.getIWApplicationContext();
		clear(iwac);
	}

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

		try {
			getCourseBusiness(getIWApplicationContext()).reserveCourse(holder.getCourse());
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void removeApplication(User user, ApplicationHolder holder) {
		if (getMap().containsKey(user)) {
			Collection applications = (Collection) getMap().get(user);
			applications.remove(holder);
			getMap().put(user, applications);
		}

		try {
			getCourseBusiness(getIWApplicationContext()).removeReservation(holder.getCourse());
		}
		catch (RemoteException e) {
			e.printStackTrace();
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

	public void ejbRemove() {
		clear(getIWApplicationContext());
		super.ejbRemove();
	}

	public void ejbPassivate() throws EJBException, RemoteException {
		clear(getIWApplicationContext());
		super.ejbPassivate();
	}

	public void clear(IWApplicationContext iwac) {
		if (applications != null) {
			System.out.println("Removing reserved courses...");
			Iterator iter = applications.values().iterator();
			while (iter.hasNext()) {
				Collection holders = (Collection) iter.next();
				Iterator iterator = holders.iterator();
				while (iterator.hasNext()) {
					ApplicationHolder holder = (ApplicationHolder) iterator.next();
					try {
						System.out.println("Removing course: " + holder.getCourse().getName());
						getCourseBusiness(iwac).removeReservation(holder.getCourse());
					}
					catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			}
			System.out.println("Done removing reserved courses...");
		}

		try {
			getCourseBusiness(iwac).printReservations();
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}

		applications = null;
	}

	private CourseBusiness getCourseBusiness(IWApplicationContext iwac) {
		try {
			return (CourseBusiness) IBOLookup.getServiceInstance(iwac, CourseBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}
}