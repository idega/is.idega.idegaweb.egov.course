package is.idega.idegaweb.egov.course.data;


import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import java.util.List;
import com.idega.user.data.User;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class CourseCertificateHomeImpl extends IDOFactory implements CourseCertificateHome {

	public Class getEntityInterfaceClass() {
		return CourseCertificate.class;
	}

	public CourseCertificate create() throws CreateException {
		return (CourseCertificate) super.createIDO();
	}

	public CourseCertificate findByPrimaryKey(Object pk) throws FinderException {
		return (CourseCertificate) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAllByUser(User user) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CourseCertificateBMPBean) entity).ejbFindAllByUser(user);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByUserAndCourse(User user, Course course) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CourseCertificateBMPBean) entity).ejbFindAllByUserAndCourse(user, course);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public CourseCertificate findByUserAndCourse(User user, Course course) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((CourseCertificateBMPBean) entity).ejbFindByUserAndCourse(user, course);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findByUsersAndValidityAndType(List usersIds, boolean onlyValidCertificates, String certificateTypeId) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CourseCertificateBMPBean) entity).ejbFindByUsersAndValidityAndType(usersIds, onlyValidCertificates, certificateTypeId);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public CourseCertificate findHighestNumberByType(CourseCertificateType type) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((CourseCertificateBMPBean) entity).ejbFindHighestNumberByType(type);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}
}