package is.idega.idegaweb.egov.course.data;


import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.IDOHome;
import com.idega.user.data.User;

public interface CourseCertificateHome extends IDOHome {
	
	public CourseCertificate create() throws CreateException;

	public CourseCertificate findByPrimaryKey(Object pk) throws FinderException;
	
	public Collection findAllCertificatesByUser(User user) throws FinderException;
}