package is.idega.idegaweb.egov.course.data;


import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOFactory;

public class CourseCertificateHomeImpl extends IDOFactory implements CourseCertificateHome {

	private static final long serialVersionUID = -6942609966693835007L;

	public Class getEntityInterfaceClass() {
		return CourseCertificate.class;
	}

	public CourseCertificate create() throws CreateException {
		return (CourseCertificate) super.createIDO();
	}

	public CourseCertificate findByPrimaryKey(Object pk) throws FinderException {
		return (CourseCertificate) super.findByPrimaryKeyIDO(pk);
	}
}