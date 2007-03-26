package is.idega.idegaweb.egov.course.business;


import is.idega.idegaweb.egov.course.data.ApplicationHolder;
import java.util.Collection;
import com.idega.user.data.User;
import com.idega.business.IBOSession;
import is.idega.idegaweb.egov.course.data.Course;
import java.rmi.RemoteException;

public interface CourseApplicationSession extends IBOSession {
	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseApplicationSessionBean#getApplications
	 */
	public Collection getApplications() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseApplicationSessionBean#addApplication
	 */
	public void addApplication(ApplicationHolder holder) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseApplicationSessionBean#removedApplication
	 */
	public void removedApplication(ApplicationHolder holder)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseApplicationSessionBean#contains
	 */
	public boolean contains(User user, Course course) throws RemoteException;
}