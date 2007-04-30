package is.idega.idegaweb.egov.course.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHome;
import java.rmi.RemoteException;

public interface CourseBusinessHome extends IBOHome {

	public CourseBusiness create() throws CreateException, RemoteException;
}