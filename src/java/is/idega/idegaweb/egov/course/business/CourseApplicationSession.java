package is.idega.idegaweb.egov.course.business;


import is.idega.idegaweb.egov.course.data.ApplicationHolder;
import java.util.Collection;
import java.util.Map;
import com.idega.user.data.User;
import com.idega.business.IBOSession;
import is.idega.idegaweb.egov.course.data.Course;
import java.rmi.RemoteException;

public interface CourseApplicationSession extends IBOSession {

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseApplicationSessionBean#getApplications
	 */
	public Map getApplications() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseApplicationSessionBean#getUserApplications
	 */
	public Collection getUserApplications(User user) throws RemoteException;

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
	public void clear() throws RemoteException;
}