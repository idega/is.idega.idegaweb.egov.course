package is.idega.idegaweb.egov.course.data;


import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;

public interface CourseCertificateHome extends IDOHome {
	
	public CourseCertificate create() throws CreateException;

	public CourseCertificate findByPrimaryKey(Object pk) throws FinderException;
}