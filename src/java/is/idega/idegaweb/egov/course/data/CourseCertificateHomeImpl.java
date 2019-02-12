package is.idega.idegaweb.egov.course.data;


import java.util.Collection;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;
import com.idega.user.data.User;

public class CourseCertificateHomeImpl extends IDOFactory implements CourseCertificateHome {

	private static final long serialVersionUID = -6942609966693835007L;

	@Override
	public Class getEntityInterfaceClass() {
		return CourseCertificate.class;
	}

	@Override
	public CourseCertificate create() throws CreateException {
		return (CourseCertificate) super.createIDO();
	}

	@Override
	public CourseCertificate findByPrimaryKey(Object pk) throws FinderException {
		return (CourseCertificate) super.findByPrimaryKeyIDO(pk);
	}

	@Override
	public Collection findAllCertificatesByUser(User user) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CourseCertificateBMPBean) entity).ejbFindAllByUser(user);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	@Override
	public Collection findAllCertificatesByUserAndCourse(User user, Course course) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CourseCertificateBMPBean) entity).ejbFindAllByUserAndCourse(user, course);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	@Override
	public CourseCertificate findByUserAndCourse(User user, Course course) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((CourseCertificateBMPBean) entity).ejbFindByUserAndCourse(user, course);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	@Override
	public Collection<CourseCertificate> findCertificatesByUsersAndValidityAndType(List usersIds, boolean onlyValidCertificates, String certificateTypeId) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection<?> ids = ((CourseCertificateBMPBean) entity).ejbFindByUsersAndValidityAndType(usersIds, onlyValidCertificates, certificateTypeId);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	@Override
	public CourseCertificate findHighestNumber() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((CourseCertificateBMPBean) entity).ejbFindHighestNumber();
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}
}