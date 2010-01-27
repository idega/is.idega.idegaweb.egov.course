package is.idega.idegaweb.egov.course.business;


import javax.ejb.CreateException;
import java.rmi.RemoteException;
import com.idega.business.IBOHome;

public interface CourseBusinessHome extends IBOHome {
	public CourseBusiness create() throws CreateException, RemoteException;
}