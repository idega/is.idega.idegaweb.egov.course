package is.idega.idegaweb.egov.course.business;


import is.idega.idegaweb.egov.course.data.CourseProvider;

import java.rmi.RemoteException;
import java.util.Collection;

import com.idega.business.IBOSession;

public interface CourseSession extends IBOSession {
	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseSessionBean#isSchoolProvider
	 */
	public boolean isSchoolProvider() throws RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseSessionBean#getProvider
	 */
	public CourseProvider getProvider() throws RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseSessionBean#setProvider
	 */
	public void setProvider(CourseProvider provider) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseSessionBean#setProviderPK
	 */
	public void setProviderPK(Object providerPK) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseSessionBean#getSchoolsForUser
	 */
	public Collection<CourseProvider> getSchoolsForUser() throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseSessionBean#getIsAllProvidersSelected
	 */
	public boolean getIsAllProvidersSelected() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseSessionBean#setIsAllProvidersSelected
	 */
	public void setIsAllProvidersSelected(boolean selected)
			throws RemoteException;
}