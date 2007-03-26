package is.idega.idegaweb.egov.course.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHome;
import java.rmi.RemoteException;

public interface CourseApplicationSessionHome extends IBOHome {
	public CourseApplicationSession create() throws CreateException,
			RemoteException;
}