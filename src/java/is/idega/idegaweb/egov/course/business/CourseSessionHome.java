package is.idega.idegaweb.egov.course.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHome;
import java.rmi.RemoteException;

public interface CourseSessionHome extends IBOHome {
	public CourseSession create() throws CreateException, RemoteException;
}