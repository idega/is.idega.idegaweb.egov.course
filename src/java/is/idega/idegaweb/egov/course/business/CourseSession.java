package is.idega.idegaweb.egov.course.business;


import is.idega.idegaweb.egov.course.CourseConstants;
import is.idega.idegaweb.egov.course.data.CourseProvider;
import is.idega.idegaweb.egov.course.data.CourseProviderCategory;
import is.idega.idegaweb.egov.course.data.CourseProviderType;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Collections;

import com.idega.business.IBOSession;
import com.idega.user.data.User;

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
	 * 
	 * @return entities connected to {@link User} or 
	 * all entities with {@link CourseProviderCategory#CATEGORY_AFTER_SCHOOL_CARE}
	 * category if current {@link User} is 
	 * {@link CourseConstants#SUPER_ADMINISTRATOR_ROLE_KEY} or 
	 * {@link Collections#emptyList()} on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public Collection<CourseProvider> getSchoolsForUser();

	/**
	 * @param type to filter, skipped if <code>null</code>;
	 * @return entities connected to {@link User} or 
	 * all entities with {@link CourseProviderCategory#CATEGORY_AFTER_SCHOOL_CARE}
	 * category if current {@link User} is 
	 * {@link CourseConstants#SUPER_ADMINISTRATOR_ROLE_KEY} or 
	 * {@link Collections#emptyList()} on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public Collection<CourseProvider> getSchoolsForUser(CourseProviderType type);

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