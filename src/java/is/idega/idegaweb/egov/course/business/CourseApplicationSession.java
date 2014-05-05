package is.idega.idegaweb.egov.course.business;

import is.idega.idegaweb.egov.course.data.ApplicationHolder;
import is.idega.idegaweb.egov.course.data.Course;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import com.idega.business.IBOSession;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.user.data.User;

public interface CourseApplicationSession extends IBOSession, HttpSessionBindingListener {

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseApplicationSessionBean#valueBound
	 */
	@Override
	public void valueBound(HttpSessionBindingEvent arg0);

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseApplicationSessionBean#valueUnbound
	 */
	@Override
	public void valueUnbound(HttpSessionBindingEvent arg0);

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseApplicationSessionBean#getApplications
	 */
	public Map<User, Collection<ApplicationHolder>> getApplications() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseApplicationSessionBean#getUserApplications
	 */
	public Collection<ApplicationHolder> getUserApplications(User user) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseApplicationSessionBean#addApplication
	 */
	public void addApplication(User user, ApplicationHolder holder) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseApplicationSessionBean#removeApplication
	 */
	public void removeApplication(User user, ApplicationHolder holder) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseApplicationSessionBean#contains
	 */
	public boolean contains(User user, Course course) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseApplicationSessionBean#clear
	 */
	public void clear(IWApplicationContext iwac) throws RemoteException;
}