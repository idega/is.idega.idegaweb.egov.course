package is.idega.idegaweb.egov.course.data;


import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;
import com.idega.user.data.User;

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

	public Collection findAllCertificatesByUser(User user) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CourseCertificateBMPBean) entity).ejbFindAllByUser(user);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}