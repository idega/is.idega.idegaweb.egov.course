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

	private Map<User, Collection<ApplicationHolder>> applications;

	@Override
	public void valueBound(HttpSessionBindingEvent arg0) {
	}

	@Override
	public void valueUnbound(HttpSessionBindingEvent arg0) {
		IWMainApplication iwma = IWMainApplication.getIWMainApplication(arg0.getSession().getServletContext());
		IWApplicationContext iwac = iwma.getIWApplicationContext();
		clear(iwac);
	}

	private Map<User, Collection<ApplicationHolder>> getMap() {
		if (applications == null) {
			applications = new HashMap<User, Collection<ApplicationHolder>>();
		}

		return applications;
	}

	@Override
	public Map<User, Collection<ApplicationHolder>> getApplications() {
		return getMap();
	}

	@Override
	public Collection<ApplicationHolder> getUserApplications(User user) {
		return getMap().get(user);
	}

	@Override
	public void addApplication(User user, ApplicationHolder holder) {
		if (getMap().containsKey(user)) {
			Collection<ApplicationHolder> applications = getMap().get(user);
			if (applications.contains(holder)) {
				for (ApplicationHolder object: applications) {
					if (object.equals(holder)) {
						holder.setOnWaitingList(object.isOnWaitingList());
					}
				}
				applications.remove(holder);

				try {
					getCourseBusiness(getIWApplicationContext()).removeReservation(holder.getCourse());
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
			applications.add(holder);
			getMap().put(user, applications);
		} else {
			Collection<ApplicationHolder> applications = new ArrayList<ApplicationHolder>();
			applications.add(holder);
			getMap().put(user, applications);
		}

		try {
			getCourseBusiness(getIWApplicationContext()).reserveCourse(holder.getCourse(), user);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void removeApplication(User user, ApplicationHolder holder) {
		if (getMap().containsKey(user)) {
			Collection<ApplicationHolder> applications = getMap().get(user);
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

	@Override
	public boolean contains(User user, Course course) {
		if (getMap().containsKey(user)) {
			Collection<ApplicationHolder> applications = getMap().get(user);

			ApplicationHolder holder = new ApplicationHolder();
			holder.setUser(user);
			holder.setCourse(course);

			return applications.contains(holder);
		}
		return false;
	}

	@Override
	public void ejbRemove() {
		clear(getIWApplicationContext());
		super.ejbRemove();
	}

	@Override
	public void ejbPassivate() throws EJBException, RemoteException {
		clear(getIWApplicationContext());
		super.ejbPassivate();
	}

	@Override
	public void clear(IWApplicationContext iwac) {
		if (applications != null) {
			// System.out.println("Removing reserved courses...");
			Iterator<Collection<ApplicationHolder>> iter = applications.values().iterator();
			while (iter.hasNext()) {
				Collection<ApplicationHolder> holders = iter.next();
				Iterator<ApplicationHolder> iterator = holders.iterator();
				while (iterator.hasNext()) {
					ApplicationHolder holder = iterator.next();
					try {
						// System.out.println("Removing course: " + holder.getCourse().getName());
						getCourseBusiness(iwac).removeReservation(holder.getCourse());
					}
					catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			}
			// System.out.println("Done removing reserved courses...");
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
			return IBOLookup.getServiceInstance(iwac, CourseBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}
}