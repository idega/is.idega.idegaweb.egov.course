package is.idega.idegaweb.egov.course.business;

import java.rmi.RemoteException;

import javax.ejb.CreateException;

import com.idega.business.IBOHome;

public interface CourseProviderBusinessHome extends IBOHome {
	public CourseProviderBusiness create() throws CreateException, RemoteException;
}
